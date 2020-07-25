package script

import com.databricks.spark.xml.XmlDataFrameReader
import com.mongodb.spark.MongoSpark
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import property.PropertiesObj

/**
 * 导入数据的执行脚本
 */
object ExecScript {
  val prefixRegex2 = "^(\\S*?)/(\\S*?)/"
  val prefixRegex1 = "^(\\S*?)/"
  val onlyDoc = "onlyDoc"
  val DistinctAuthor = "DistinctAuthor"
  val venue = "venue"
  //  val AuthorTemp = "AuthorTemp"

  import util.UDFObject

  val writeNotNull: UserDefinedFunction = udf(UDFObject.writeNotNull _)
  val initDblpType: UserDefinedFunction = udf(UDFObject.dblpType _)
  val paren2tag: UserDefinedFunction = udf(UDFObject.rtoaAarse _)


  def main(args: Array[String]): Unit = {
    Logger.getRootLogger.setLevel(Level.INFO)
//    writeOnlydoc()
//    writeVenue()
    writeAuthor()
  }

  def writeOnlydoc(): Unit = {
    //写入所有子节点到多个集合
    SparkSession.builder()
    PropertiesObj.subNode.foreach(subnode => {
      import ss.implicits.StringToColumn
      val ss: SparkSession = SparkSession
        .builder
        .appName(s"writeIntoOnlydoc_$subnode")
        .master("local[*]")
        .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$onlyDoc")
        .getOrCreate()
        .newSession()

      val opt = ss.read
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

      MongoSpark.save(opt.write.mode(SaveMode.Append))
      Logger.getLogger("UserLogger").info(s"write $subnode into mongodb")
      ss.stop()
    })

  }

  def writeAuthor(): Unit = {
    //对author去重，并存入DistinctAuthor集合
    SparkSession.clearActiveSession()
    val sparkSession: SparkSession = SparkSession
      .builder
      .appName("write author")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$DistinctAuthor")
      .config("spark.mongodb.input.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$onlyDoc")
      .getOrCreate()
      .newSession()
    import sparkSession.implicits._

    val mongoDF: DataFrame = MongoSpark
      .load(sparkSession)
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
    //      .select($"_VALUE", $"_orcid", $"_aux")
    //      .cache()
    //    joinedRow.show()
    //    joinedRow.printSchema()
    //    joinedRow.filter($"_orcid".isNotNull).show(300)
    MongoSpark.save(joinedRow.write.mode(SaveMode.Overwrite))
    Logger.getLogger("UserLogger").info(s"writing author into mongodb")
  }

  def writeVenue(): Unit = {
    //venue group

    SparkSession.clearActiveSession()
    val sparkSession: SparkSession = SparkSession
      .builder
      .appName("venue group")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$venue")
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
  }
}
