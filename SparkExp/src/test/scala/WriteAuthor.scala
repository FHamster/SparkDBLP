import com.mongodb.spark.MongoSpark
import com.mongodb.spark.rdd.MongoRDD
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.explode
import org.scalatest.funsuite.AnyFunSuite


/**
 * 写入author信息
 */
class WriteAuthor extends AnyFunSuite {


  test("distinct author") {
    import com.mongodb.spark.config._
    val sparkSession: SparkSession = SparkSession
      .builder
      .appName("in")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.Author")
      .config("spark.mongodb.input.uri", s"mongodb://127.0.0.1/SparkDBLPTest.Author")
      .getOrCreate()

    import com.mongodb.spark.config._
    //    import com.mongodb.spark
    import sparkSession.implicits._
    val customReadConfig = ReadConfig(Map(
      "readPreference.name" -> "secondaryPreferred"),
      Some(ReadConfig(sparkSession)))
    val df = sparkSession.read.format("mongo").options(customReadConfig.asOptions).load()

    println(df.count())
    val df2 = df.dropDuplicates("_VALUE")
      .select($"_VALUE", $"_orcid")
      .cache()
    println(df2.count())
    df2.show(100)
    import com.mongodb.spark.config._

    MongoSpark.save(df2.write.option("collection", "Author").mode("overwrite"))
  }

  test("article") {
    import com.databricks.spark.xml._
    val subnode = "article"
    val ss: SparkSession = SparkSession
      .builder
      .appName("Write_article")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.Author")
      .getOrCreate()

    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.articleSchema)
      .xml(PropertiesObj.wholeDBLP_cvtSparkPath)

    import ss.implicits._
    val res = opt
      .select(explode($"author") as "author")
      .select($"author._VALUE" as "_VALUE",
        $"author._orcid" as "_orcid",
        $"author._aux" as "_aux"
      ).distinct()
      .sort($"_VALUE")

    println(s"write $subnode into mongodb")
    MongoSpark.save(res)
    ss.stop()
  }

  test("inproceedings") {
    import com.databricks.spark.xml._
    val subnode = "inproceedings"
    val ss: SparkSession = SparkSession
      .builder
      .appName("Write_article")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.Author")
      .getOrCreate()

    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.inproceedingsSchema)
      .xml(PropertiesObj.wholeDBLP_cvtSparkPath)
    import ss.implicits._
    val res = opt
      .select(explode($"author") as "author")
      .select($"author._VALUE" as "_VALUE",
        $"author._orcid" as "_orcid",
        $"author._aux" as "_aux"
      ).distinct()
      .sort($"_VALUE")

    println(s"write $subnode into mongodb")
    MongoSpark.save(res)
    ss.stop()
  }
  test("proceedings") {
    import com.databricks.spark.xml._
    val subnode = "proceedings"
    val ss: SparkSession = SparkSession
      .builder
      .appName("Write_article")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.Author")
      .getOrCreate()

    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.proceedingsSchema)
      .xml(PropertiesObj.wholeDBLP_cvtSparkPath)
    import ss.implicits._
    val res = opt
      .select(explode($"author") as "author")
      .select($"author._VALUE" as "_VALUE",
        $"author._orcid" as "_orcid",
        $"author._aux" as "_aux"
      ).distinct()
      .sort($"_VALUE")

    println(s"write $subnode into mongodb")
    MongoSpark.save(res)
    ss.stop()
  }
  test("book") {
    import com.databricks.spark.xml._
    val subnode = "book"
    val ss: SparkSession = SparkSession
      .builder
      .appName("Write_article")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.Author")
      .getOrCreate()

    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.bookSchema)
      .xml(PropertiesObj.wholeDBLP_cvtSparkPath)
    import ss.implicits._
    val res = opt
      .select(explode($"author") as "author")
      .select($"author._VALUE" as "_VALUE",
        $"author._orcid" as "_orcid",
        $"author._aux" as "_aux"
      ).distinct()
      .sort($"_VALUE")

    println(s"write $subnode into mongodb")
    MongoSpark.save(res)
    ss.stop()
  }
  test("incollection") {
    import com.databricks.spark.xml._
    val subnode = "incollection"
    val ss: SparkSession = SparkSession
      .builder
      .appName("Write_article")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.Author")
      .getOrCreate()

    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.incollectionSchema)
      .xml(PropertiesObj.wholeDBLP_cvtSparkPath)
    import ss.implicits._
    val res = opt
      .select(explode($"author") as "author")
      .select($"author._VALUE" as "_VALUE",
        $"author._orcid" as "_orcid",
        $"author._aux" as "_aux"
      ).distinct()
      .sort($"_VALUE")

    println(s"write $subnode into mongodb")
    MongoSpark.save(res)
    ss.stop()
  }
  test("phdthesis") {
    import com.databricks.spark.xml._
    val subnode = "phdthesis"
    val ss: SparkSession = SparkSession
      .builder
      .appName("Write_article")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.Author")
      .getOrCreate()

    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.phdthesisSchema)
      .xml(PropertiesObj.wholeDBLP_cvtSparkPath)
    import ss.implicits._
    val res = opt
      .select(explode($"author") as "author")
      .select($"author._VALUE" as "_VALUE",
        $"author._orcid" as "_orcid",
        $"author._aux" as "_aux"
      ).distinct()
      .sort($"_VALUE")

    println(s"write $subnode into mongodb")
    MongoSpark.save(res)
    ss.stop()
  }
  test("mastersthesis") {
    import com.databricks.spark.xml._
    val subnode = "mastersthesis"
    val ss: SparkSession = SparkSession
      .builder
      .appName("Write_article")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.Author")
      .getOrCreate()

    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.mastersthesisSchema)
      .xml(PropertiesObj.wholeDBLP_cvtSparkPath)
    import ss.implicits._
    val res = opt
      .select(explode($"author") as "author")
      .select($"author._VALUE" as "_VALUE",
        $"author._orcid" as "_orcid",
        $"author._aux" as "_aux"
      ).distinct()
      .sort($"_VALUE")

    println(s"write $subnode into mongodb")
    MongoSpark.save(res)
    ss.stop()
  }
  test("www") {
    import com.databricks.spark.xml._
    val subnode = "www"
    val ss: SparkSession = SparkSession
      .builder
      .appName("Write_article")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.Author")
      .getOrCreate()

    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.wwwSchema)
      .xml(PropertiesObj.wholeDBLP_cvtSparkPath)
    import ss.implicits._
    val res = opt
      .select(explode($"author") as "author")
      .select($"author._VALUE" as "_VALUE",
        $"author._orcid" as "_orcid",
        $"author._aux" as "_aux"
      ).distinct()
      .sort($"_VALUE")

    println(s"write $subnode into mongodb")
    MongoSpark.save(res)
    ss.stop()
  }
}