package script

import com.databricks.spark.xml.XmlDataFrameReader
import com.mongodb.spark.MongoSpark
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.{lit, regexp_extract, split, udf}
import org.apache.spark.sql.{SaveMode, SparkSession}
import property.PropertiesObj

object Import2MongoDB extends App {
  val onlyDoc = "onlyDoc"
  val prefixRegex2 = "^(\\S*?)/(\\S*?)/"
  val prefixRegex1 = "^(\\S*?)/"
  val indexPattern = "[\\s(),.]"

  import util.UDFObject

  val initDblpType: UserDefinedFunction = udf(UDFObject.dblpType)
  val paren2tag: UserDefinedFunction = udf(UDFObject.rtoaAarse)
  val ipAddress: String = PropertiesObj.ipAddress
  val dataBaseName: String = PropertiesObj.dataBaseName
  //写入所有子节点到多个集合
  //  SparkSession.builder()

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
  spark.close()
}
