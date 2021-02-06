package execTest

import com.databricks.spark.xml.XmlDataFrameReader
import com.mongodb.spark.MongoSpark
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.scalatest.funsuite.AnyFunSuite
import property.PropertiesObj

/**
 * 这个类记录了如何将spark的数据写入mongodb（使用手工设定的Schema,全部写入同一个集合）
 */
class WriteSubnodeIntoOneDoc extends AnyFunSuite {
  val onlyDoc = "onlyDoc"
  val prefixRegex2 = "^(\\S*?)/(\\S*?)/"
  val prefixRegex1 = "^(\\S*?)/"
  val indexPattern = "[\\s(),.]"
  import util.UDFObject

  val initDblpType = udf(UDFObject.dblpType _)
  val paren2tag = udf(UDFObject.rtoaAarse _)
  val ipAddress = PropertiesObj.ipAddress
  test("article") {
    import ss.implicits.StringToColumn
    val subnode = "article"
    val ss: SparkSession = SparkSession
      .builder
      .appName(s"writeIntoOnlydoc_$subnode")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://${ipAddress}/SparkDBLPTest.$onlyDoc")
      .getOrCreate()

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
      .withColumn("prefixIndex", split($"title", indexPattern))

    println(s"write $subnode into mongodb")
    opt.show()
    opt.printSchema()
    MongoSpark.save(opt.write.mode(SaveMode.Append))
    ss.stop()
  }

  test("inproceedings") {
    import ss.implicits.StringToColumn
    val subnode = "inproceedings"
    val ss: SparkSession = SparkSession
      .builder
      .appName(s"writeIntoOnlydoc_$subnode")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://${ipAddress}/SparkDBLPTest.$onlyDoc")
      .getOrCreate()

    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.inproceedingsSchema)
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
    opt.show()
    opt.printSchema()
    MongoSpark.save(opt.write.mode(SaveMode.Append))
    ss.stop()
  }
  test("proceedings") {
    import ss.implicits.StringToColumn
    val subnode = "proceedings"
    val ss: SparkSession = SparkSession
      .builder
      .appName(s"writeIntoOnlydoc_$subnode")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://${ipAddress}/SparkDBLPTest.$onlyDoc")
      .getOrCreate()

    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.proceedingsSchema)
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
    opt.show()
    opt.printSchema()

    MongoSpark.save(opt.write.mode(SaveMode.Append))
    ss.stop()
  }
  test("book") {
    import ss.implicits.StringToColumn
    val subnode = "book"
    val ss: SparkSession = SparkSession
      .builder
      .appName(s"writeIntoOnlydoc_$subnode")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://${ipAddress}/SparkDBLPTest.$onlyDoc")
      .getOrCreate()

    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.bookSchema)
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
    opt.show()
    opt.printSchema()
    MongoSpark.save(opt.write.mode(SaveMode.Append))
    ss.stop()
  }
  test("incollection") {
    import ss.implicits.StringToColumn
    val subnode = "incollection"
    val ss: SparkSession = SparkSession
      .builder
      .appName(s"writeIntoOnlydoc_$subnode")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://${ipAddress}/SparkDBLPTest.$onlyDoc")
      .getOrCreate()

    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.incollectionSchema)
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
    opt.show()
    opt.printSchema()
    MongoSpark.save(opt.write.mode(SaveMode.Append))
    ss.stop()
  }
  test("phdthesis") {
    import ss.implicits.StringToColumn
    val subnode = "phdthesis"
    val ss: SparkSession = SparkSession
      .builder
      .appName(s"writeIntoOnlydoc_$subnode")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://${ipAddress}/SparkDBLPTest.$onlyDoc")
      .getOrCreate()

    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.phdthesisSchema)
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
    opt.show()
    opt.printSchema()
    MongoSpark.save(opt.write.mode(SaveMode.Append))
    ss.stop()
  }
  test("mastersthesis") {
    import ss.implicits.StringToColumn
    val subnode = "mastersthesis"
    val ss: SparkSession = SparkSession
      .builder
      .appName(s"writeIntoOnlydoc_$subnode")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://${ipAddress}/SparkDBLPTest.$onlyDoc")
      .getOrCreate()

    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.mastersthesisSchema)
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
    opt.show()
    opt.printSchema()
    MongoSpark.save(opt.write.mode(SaveMode.Append))
    ss.stop()
  }
  /*  test("www") {
      import ss.implicits.StringToColumn
      val subnode = "www"
      val ss: SparkSession = SparkSession
        .builder
        .appName(s"writeIntoOnlydoc_$subnode")
        .master("local[*]")
        .config("spark.mongodb.output.uri", s"mongodb://${ipAddress}/SparkDBLPTest.$onlyDoc")
        .getOrCreate()

      val opt = ss.read
        .option("rootTag", "dblp")
        .option("rowTag", subnode)
        .schema(PropertiesObj.wwwSchema)
        .xml(PropertiesObj.wholeDBLP_cvtSparkPath)
        .withColumn("prefix1", regexp_extract($"_key", prefixRegex1, 0))
        .withColumn("prefix2", regexp_extract($"_key", prefixRegex2, 0))

      println(s"write $subnode into mongodb")
    opt.show()
    opt.printSchema()
      MongoSpark.save(opt.write.mode(SaveMode.Append))
      ss.stop()
    }*/

}