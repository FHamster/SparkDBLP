import com.mongodb.spark.MongoSpark
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.regexp_extract
import org.scalatest.funsuite.AnyFunSuite


/**
 * 这个类记录了如何将spark的数据写入mongodb（使用手工设定的Schema）
 */
class WriteSubnodeIntoMongo extends AnyFunSuite {
  val charTest = "src/test/resources/article_CharTest.xml"

  test("article") {
    import com.databricks.spark.xml.XmlDataFrameReader
    import ss.implicits.StringToColumn
    val subnode = "article"
    val ss: SparkSession = SparkSession
      .builder
      .appName("Write_article")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$subnode")
      .getOrCreate()

    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.articleSchema)
      .xml(PropertiesObj.wholeDBLP_cvtSparkPath)
      .withColumn("prefix", regexp_extract($"_key", "\\S*/\\S*/", 0))
      .cache()
    opt.show()
    opt.printSchema()

    println(s"write $subnode into mongodb")
    MongoSpark.save(opt)
    ss.stop()
  }

  test("inproceedings") {
    import com.databricks.spark.xml.XmlDataFrameReader
    import ss.implicits.StringToColumn
    val subnode = "inproceedings"
    val ss: SparkSession = SparkSession
      .builder
      .appName("Write_article")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$subnode")
      .getOrCreate()
    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.inproceedingsSchema)
      .xml(PropertiesObj.wholeDBLP_cvtSparkPath)
      .withColumn("prefix", regexp_extract($"_key", "\\S*/\\S*/", 0))
      .cache()
    opt.show()
    opt.printSchema()

    println(s"write $subnode into mongodb")
    MongoSpark.save(opt)
    ss.stop()
  }
  test("proceedings") {
    import com.databricks.spark.xml.XmlDataFrameReader
    import ss.implicits.StringToColumn
    val subnode = "proceedings"
    val ss: SparkSession = SparkSession
      .builder
      .appName("Write_article")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$subnode")
      .getOrCreate()

    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.proceedingsSchema)
      .xml(PropertiesObj.wholeDBLP_cvtSparkPath)
      .withColumn("prefix", regexp_extract($"_key", "\\S*/\\S*/", 0))
      .cache()
    opt.show()
    opt.printSchema()

    println(s"write $subnode into mongodb")
    MongoSpark.save(opt)
    ss.stop()
  }
  test("book") {
    import com.databricks.spark.xml.XmlDataFrameReader
    import ss.implicits.StringToColumn
    val subnode = "book"
    val ss: SparkSession = SparkSession
      .builder
      .appName("Write_article")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$subnode")
      .getOrCreate()

    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.bookSchema)
      .xml(PropertiesObj.wholeDBLP_cvtSparkPath)
      .withColumn("prefix", regexp_extract($"_key", "\\S*/\\S*/", 0))
      .cache()
    opt.show()
    opt.printSchema()

    println(s"write $subnode into mongodb")
    MongoSpark.save(opt)
    ss.stop()
  }
  test("incollection") {
    import com.databricks.spark.xml.XmlDataFrameReader
    import ss.implicits.StringToColumn
    val subnode = "incollection"
    val ss: SparkSession = SparkSession
      .builder
      .appName("Write_article")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$subnode")
      .getOrCreate()

    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.incollectionSchema)
      .xml(PropertiesObj.wholeDBLP_cvtSparkPath)
      .withColumn("prefix", regexp_extract($"_key", "\\S*/\\S*/", 0))
      .cache()
    opt.show()
    opt.printSchema()

    println(s"write $subnode into mongodb")
    MongoSpark.save(opt)
    ss.stop()
  }
  test("phdthesis") {
    import com.databricks.spark.xml.XmlDataFrameReader
    import ss.implicits.StringToColumn
    val subnode = "phdthesis"
    val ss: SparkSession = SparkSession
      .builder
      .appName("Write_article")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$subnode")
      .getOrCreate()

    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.phdthesisSchema)
      .xml(PropertiesObj.wholeDBLP_cvtSparkPath)
      .withColumn("prefix", regexp_extract($"_key", "\\S*/\\S*/", 0))
      .cache()
    opt.show()
    opt.printSchema()

    println(s"write $subnode into mongodb")
    MongoSpark.save(opt)
    ss.stop()
  }
  test("mastersthesis") {
    import com.databricks.spark.xml.XmlDataFrameReader
    import ss.implicits.StringToColumn
    val subnode = "incollection"
    val ss: SparkSession = SparkSession
      .builder
      .appName("Write_article")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$subnode")
      .getOrCreate()

    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.mastersthesisSchema)
      .xml(PropertiesObj.wholeDBLP_cvtSparkPath)
      .withColumn("prefix", regexp_extract($"_key", "\\S*/\\S*/", 0))
      .cache()
    opt.show()
    opt.printSchema()

    println(s"write $subnode into mongodb")
    MongoSpark.save(opt)
    ss.stop()
  }
  test("www") {
    import com.databricks.spark.xml.XmlDataFrameReader
    import ss.implicits.StringToColumn
    val subnode = "www"
    val ss: SparkSession = SparkSession
      .builder
      .appName("Write_article")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$subnode")
      .getOrCreate()

    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.wwwSchema)
      .xml(PropertiesObj.wholeDBLP_cvtSparkPath)
      .withColumn("prefix", regexp_extract($"_key", "\\S*/\\S*/", 0))
      .cache()
    opt.show()
    opt.printSchema()

    println(s"write $subnode into mongodb")
    MongoSpark.save(opt)
    ss.stop()
  }

}