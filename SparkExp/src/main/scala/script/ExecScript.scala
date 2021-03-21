package script

import com.databricks.spark.xml.XmlDataFrameReader
import com.mongodb.spark.MongoSpark
import com.mongodb.spark.config.ReadConfig
import com.mongodb.spark.sql.toSparkSessionFunctions
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import property.PropertiesObj
import script.AuthorScript.DistinctAuthor

/**
 * 导入数据的执行脚本
 */
object ExecScript {
  val onlyDoc = "onlyDoc"
  val prefixRegex2 = "^(\\S*?)/(\\S*?)/"
  val prefixRegex1 = "^(\\S*?)/"
  val indexPattern = "[\\s(),.]"

  import util.UDFObject

  //  val initDblpType: UserDefinedFunction = udf((_publType: String, type_xml: String, prefix1: String) => "S")
  val initDblpType: UserDefinedFunction = udf(UDFObject.dblpType)
  //  val paren2tag: UserDefinedFunction = udf((_:String) => "S")
  val paren2tag: UserDefinedFunction = udf(UDFObject.rtoaAarse)
  val ipAddress: String = PropertiesObj.ipAddress
  val dataBaseName: String = PropertiesObj.dataBaseName

  def main(args: Array[String]): Unit = {
    Logger.getRootLogger.setLevel(Level.INFO)
    writeOnlydoc()
    //    writeVenue()
    writeAuthor()
  }

  def writeOnlydoc(): Unit = {
    //写入所有子节点到多个集合
    SparkSession.builder()

    val subnode = "article"
    val spark: SparkSession = SparkSession
      .builder
      .appName(s"writeIntoOnlydoc_$subnode")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://$ipAddress/$dataBaseName.$onlyDoc")
      .getOrCreate()
    import spark.implicits.StringToColumn
    PropertiesObj.subNode.foreach(subnode => {


      val onlyDoc = spark.read
        .option("rootTag", "dblp")
        .option("rowTag", subnode)
        .schema(PropertiesObj.articleSchema)
        .xml(PropertiesObj.wholeDBLP_cvtSparkPath)
        .withColumn("prefix1", regexp_extract($"_key", prefixRegex1, 0))
        .withColumn("prefix2", regexp_extract($"_key", prefixRegex2, 0))
        .withColumn("type_xml", lit(subnode))
        .withColumn("type", initDblpType($"_publType", $"type_xml", $"prefix1"))
        .withColumn("cvtTitle", paren2tag($"title"))
        .drop($"title")
        .withColumnRenamed("cvtTitle", "title")
        .withColumn("prefixIndex", split($"title", indexPattern))

      println(s"write $subnode into mongodb")
      //      opt.show()
      //      opt.printSchema()
      MongoSpark.save(onlyDoc.write.option("collection", "onlyDoc").mode(SaveMode.Append))

      //      MongoSpark.save(onlyDoc.write.mode(SaveMode.Append))
    })

  }

  def writeAuthor(): Unit = {
    val spark: SparkSession = SparkSession
      .builder
      .appName("write author")
      .master("local[*]")
      //      .config("spark.mongodb.input.uri", s"mongodb://127.0.0.1/$dataBaseName.$onlyDoc")
      //      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/$dataBaseName.$DistinctAuthor")
      .getOrCreate()
    import spark.implicits._
    val mongoDF: DataFrame = spark.loadFromMongoDB(ReadConfig(
      Map("uri" -> s"mongodb://$ipAddress/$dataBaseName.$onlyDoc")
    ))
      .select($"author")
      .filter($"author" isNotNull)
      .select(explode($"author") as "author")
      .select($"author._VALUE" as "_VALUE",
        $"author._orcid" as "_orcid"
        //        $"author._aux" as "_aux"
      )
    val orcidNotNull = mongoDF
      .filter($"_orcid" isNotNull)
      .dropDuplicates("_VALUE")
      .select($"_VALUE" as "noUseValue", $"_orcid")
    val orcidNull = mongoDF
      .filter($"_orcid" isNull)
      .dropDuplicates("_VALUE")
      .select($"_VALUE")
    //      .select($"_VALUE", $"_aux")

    val joinedRow = orcidNull
      .join(orcidNotNull, $"_VALUE" === $"noUseVALUE", "leftouter")
      .select($"_VALUE", $"_orcid")
      .withColumn("prefixIndex", split($"_VALUE", indexPattern))
    //      .select($"_VALUE", $"_orcid", $"_aux")
    //      .cache()
    //    joinedRow.show()
    //    joinedRow.printSchema()
    //    joinedRow.filter($"_orcid".isNotNull).show(300)

    MongoSpark.save(joinedRow.write.option("collection", s"$DistinctAuthor").mode(SaveMode.Overwrite))
  }

/*  def writeVenue(): Unit = {
    //venue group

    SparkSession.clearActiveSession()
    val sparkSession: SparkSession = SparkSession
      .builder
      .appName("venue group")
      .master("local[*]")
//      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$venue")
      .config("spark.mongodb.input.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$onlyDoc")
      .getOrCreate()
      .newSession()
    import sparkSession.implicits._
    val mongoDF: DataFrame = MongoSpark.load(sparkSession)
      .select("_key", "booktitle", "crossref",
        "journal", "prefix1", "prefix2",
        "title", "type", "type_xml", "year")

    val crossRef = mongoDF
      .filter($"crossref" isNotNull)
      .select($"crossref" as "ref", $"booktitle" as "booktitle2")
      .dropDuplicates("ref")
    val crossReffed = crossRef
      .join(mongoDF, $"_key" === $"ref", "left")
      .withColumnRenamed("booktitle", "booktitle1")
      .withColumn("newbooktitle", writeNotNull($"booktitle1", $"booktitle2", $"title"))
      .drop("booktitle1", "booktitle2", "ref")
      .withColumnRenamed("newbooktitle", "booktitle")
      .groupBy("prefix2")
      .agg(collect_list($"booktitle") as "booktitle",
        collect_list($"_key") as "_key",
        collect_list($"year") as "year",
        collect_list($"type") as "type",
        collect_list($"type_xml") as "type_xml",
        collect_list($"title") as "title"
      ).select(
      $"prefix2",
      arrays_zip($"booktitle", $"_key", $"title", $"year", $"type", $"type_xml") as "venue"
    )
    MongoSpark.save(crossReffed.write.mode(SaveMode.Overwrite))
    Logger.getLogger("UserLogger").info(s"writing venue into mongodb")
  }*/
}
