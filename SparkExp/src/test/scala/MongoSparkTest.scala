import com.mongodb.spark.MongoSpark
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.explode
import org.scalatest.funsuite.AnyFunSuite


/**
 * 这个类记录了如何将spark的数据写入mongodb
 */
class MongoSparkTest extends AnyFunSuite {
  val testFile = "src/test/resources/article_after.xml"
  val charTest = "src/test/resources/article_CharTest.xml"
  val subNodeName = "article"

  val spark: SparkSession = SparkSession
    .builder
    .appName("XML_WriteTest")
    .master("local[*]")
    .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$subNodeName")
    .getOrCreate()
  test("write article subnode into mongodb") {
    import com.databricks.spark.xml._
    val opt = spark.read
      //      .schema(PropertiesObj.ManualArticleSchema)//手动指定schema
      .option("rootTag", "dblp")
      .option("rowTag", subNodeName)
      //      .option("charset", "ISO-8859-1")
      .xml(PropertiesObj.wholeDBLP_cvtSparkPath)

    opt.show()
    opt.printSchema()

    println("write into mongodb")
    MongoSpark.save(opt)
  }

  test("write all subnode into mongodb") {
    import com.databricks.spark.xml._
    PropertiesObj.subNode.foreach(it => {
      val ss: SparkSession = SparkSession
        .builder
        .appName("XML_WriteTest")
        .master("local[*]")
        //        .config("treatEmptyValuesAsNulls", "nullValue")
        .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$it")
        .getOrCreate()

      val opt = ss.read
        .option("rootTag", "dblp")
        .option("rowTag", it)
        .xml(PropertiesObj.wholeDBLP_cvtSparkPath)
      opt.show()
      opt.printSchema()

      println(s"write $it into mongodb")
      MongoSpark.save(opt)
      ss.stop()
    })

  }
  test("read all subnode schema") {
    import com.databricks.spark.xml._
    PropertiesObj.subNode.foreach(it => {
      val ss: SparkSession = SparkSession
        .builder
        .appName("readAllSchema")
        .master("local[*]")
        //        .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$it")
        .getOrCreate()

      val opt = ss.read
        .option("rootTag", "dblp")
        .option("rowTag", it)
        .xml(PropertiesObj.wholeDBLP_cvtSparkPath)
      opt.show()
      opt.printSchema()

      println(s"write $it into mongodb")
      MongoSpark.save(opt)
      ss.stop()
    })

  }
  test("write article subnode into mongodb(use chartest)") {
    import com.databricks.spark.xml._
    val opt = spark.read
      //      .schema(PropertiesObj.articleSchema)
      .option("rootTag", "dblp")
      .option("rowTag", subNodeName)
      .xml(charTest)

    opt.show()
    opt.printSchema()

    println("write into mongodb")
    MongoSpark.save(opt)
  }


  val AuthorTest = "src/test/resources/article_authorTest.xml"
  test("article chartest") {
    import com.databricks.spark.xml._
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
      .xml(AuthorTest)
    import spark.implicits._
    val res = opt
      .select(explode($"author") as "author")
      .select($"author._VALUE" as "_VALUE",
        $"author._orcid" as "_orcid",
        $"author._aux" as "_aux"
      ).distinct()
      .sort($"_VALUE")
    res.show(1000)
    res.printSchema()

    ss.stop()
  }
}