package execTest

import property.PropertiesObj
import com.mongodb.spark.MongoSpark
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.sql.functions.{lit, regexp_extract}
import org.scalatest.funsuite.AnyFunSuite
import com.databricks.spark.xml.XmlDataFrameReader

/**
 * 这个类记录了如何将spark的数据写入mongodb（使用手工设定的Schema,全部写入同一个集合）
 */
class WriteSubnodeIntoOneDoc extends AnyFunSuite {
  val onlyDoc = "onlyDoc"
  val prefixRegex2 = "^(\\S*?)/(\\S*?)/"
  val prefixRegex1 = "^(\\S*?)/"

  test("article") {
    import ss.implicits.StringToColumn
    val subnode = "article"
    val ss: SparkSession = SparkSession
      .builder
      .master("local[*]")
      .appName("Write_article")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$onlyDoc")
      .getOrCreate()

    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.articleSchema)
      .xml(PropertiesObj.wholeDBLP_cvtSparkPath)
      .withColumn("prefix1", regexp_extract($"_key", prefixRegex1, 0))
      .withColumn("prefix2", regexp_extract($"_key", prefixRegex2, 0))
      .withColumn("type_xml", lit(subnode))
    println(s"write $subnode into mongodb")
    MongoSpark.save(opt.write.mode(SaveMode.Append))
    ss.stop()
  }

  test("inproceedings") {
    import ss.implicits.StringToColumn
    val subnode = "inproceedings"
    val ss: SparkSession = SparkSession
      .builder
      .appName("Write_article")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$onlyDoc")
      .getOrCreate()

    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.inproceedingsSchema)
      .xml(PropertiesObj.wholeDBLP_cvtSparkPath)
      .withColumn("prefix1", regexp_extract($"_key", prefixRegex1, 0))
      .withColumn("prefix2", regexp_extract($"_key", prefixRegex2, 0))
      .withColumn("type_xml", lit(subnode))
    println(s"write $subnode into mongodb")
    MongoSpark.save(opt.write.mode(SaveMode.Append))
    ss.stop()
  }
  test("proceedings") {
    import ss.implicits.StringToColumn
    val subnode = "proceedings"
    val ss: SparkSession = SparkSession
      .builder
      .appName("Write_article")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$onlyDoc")
      .getOrCreate()

    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.proceedingsSchema)
      .xml(PropertiesObj.wholeDBLP_cvtSparkPath)
      .withColumn("prefix1", regexp_extract($"_key", prefixRegex1, 0))
      .withColumn("prefix2", regexp_extract($"_key", prefixRegex2, 0))
      .withColumn("type_xml", lit(subnode))
    println(s"write $subnode into mongodb")
    MongoSpark.save(opt.write.mode(SaveMode.Append))
    ss.stop()
  }
  test("book") {
    import ss.implicits.StringToColumn
    val subnode = "book"
    val ss: SparkSession = SparkSession
      .builder
      .appName("Write_article")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$onlyDoc")
      .getOrCreate()

    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.bookSchema)
      .xml(PropertiesObj.wholeDBLP_cvtSparkPath)
      .withColumn("prefix1", regexp_extract($"_key", prefixRegex1, 0))
      .withColumn("prefix2", regexp_extract($"_key", prefixRegex2, 0))
      .withColumn("type_xml", lit(subnode))
    println(s"write $subnode into mongodb")
    MongoSpark.save(opt.write.mode(SaveMode.Append))
    ss.stop()
  }
  test("incollection") {
    import ss.implicits.StringToColumn
    val subnode = "incollection"
    val ss: SparkSession = SparkSession
      .builder
      .appName("Write_article")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$onlyDoc")
      .getOrCreate()

    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.incollectionSchema)
      .xml(PropertiesObj.wholeDBLP_cvtSparkPath)
      .withColumn("prefix1", regexp_extract($"_key", prefixRegex1, 0))
      .withColumn("prefix2", regexp_extract($"_key", prefixRegex2, 0))
      .withColumn("type_xml", lit(subnode))
    println(s"write $subnode into mongodb")
    MongoSpark.save(opt.write.mode(SaveMode.Append))
    ss.stop()
  }
  test("phdthesis") {
    import ss.implicits.StringToColumn
    val subnode = "phdthesis"
    val ss: SparkSession = SparkSession
      .builder
      .appName("Write_article")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$onlyDoc")
      .getOrCreate()

    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.phdthesisSchema)
      .xml(PropertiesObj.wholeDBLP_cvtSparkPath)
      .withColumn("prefix1", regexp_extract($"_key", prefixRegex1, 0))
      .withColumn("prefix2", regexp_extract($"_key", prefixRegex2, 0))
      .withColumn("type_xml", lit(subnode))
    println(s"write $subnode into mongodb")
    MongoSpark.save(opt.write.mode(SaveMode.Append))
    ss.stop()
  }
  test("mastersthesis") {
    import ss.implicits.StringToColumn
    val subnode = "mastersthesis"
    val ss: SparkSession = SparkSession
      .builder
      .appName("Write_article")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$onlyDoc")
      .getOrCreate()

    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.mastersthesisSchema)
      .xml(PropertiesObj.wholeDBLP_cvtSparkPath)
      .withColumn("prefix1", regexp_extract($"_key", prefixRegex1, 0))
      .withColumn("prefix2", regexp_extract($"_key", prefixRegex2, 0))
      .withColumn("type_xml", lit(subnode))
      .withColumn("type_dblp", $"type_xml")
//        .when())


    opt.printSchema()
    println(s"write $subnode into mongodb")
    MongoSpark.save(opt.write.mode(SaveMode.Append))
    ss.stop()
  }
  /*  test("www") {
      import ss.implicits.StringToColumn
      val subnode = "www"
      val ss: SparkSession = SparkSession
        .builder
        .appName("Write_article")
        .master("local[*]")
        .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$onlyDoc")
        .getOrCreate()

      val opt = ss.read
        .option("rootTag", "dblp")
        .option("rowTag", subnode)
        .schema(PropertiesObj.wwwSchema)
        .xml(PropertiesObj.wholeDBLP_cvtSparkPath)
        .withColumn("prefix1", regexp_extract($"_key", prefixRegex1, 0))
        .withColumn("prefix2", regexp_extract($"_key", prefixRegex2, 0))

      println(s"write $subnode into mongodb")
      MongoSpark.save(opt.write.mode(SaveMode.Append))
      ss.stop()
    }*/

}