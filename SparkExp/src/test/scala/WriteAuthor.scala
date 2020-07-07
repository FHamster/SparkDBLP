import com.mongodb.spark.MongoSpark
import org.apache.spark.sql.functions.explode
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.scalatest.funsuite.AnyFunSuite


/**
 * 写入author信息,并对相同author的orcid进行合并
 */
class WriteAuthor extends AnyFunSuite {
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
      )

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
      )

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
      )

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
      )

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
      )
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
      )

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
      )

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
      )

    println(s"write $subnode into mongodb")
    MongoSpark.save(res)
    ss.stop()
  }

  test("distinct author") {
    val sparkSession: SparkSession = SparkSession
      .builder
      .appName("in")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.DistinctAuthor")
      .config("spark.mongodb.input.uri", s"mongodb://127.0.0.1/SparkDBLPTest.Author")
      .getOrCreate()
    import sparkSession.implicits._
    val mongoDF: DataFrame = MongoSpark.load[Author](sparkSession).cache()
    mongoDF.show()

    val orcidNotNull = mongoDF
      .filter($"_orcid".isNotNull)
      .dropDuplicates("_VALUE")
      .select($"_VALUE" as "noUseValue", $"_orcid")
      .cache()
    val orcidNull = mongoDF
      .filter($"_orcid".isNull)
      .dropDuplicates("_VALUE")
      .select($"_VALUE", $"_aux")
      .cache()
    orcidNotNull.show()
    println("orcidNotNull")
    orcidNull.show()
    println("orcidNull")

    val joinedRow = orcidNull
      .join(orcidNotNull, $"_VALUE" === $"noUseVALUE", "leftouter")
      .select($"_VALUE", $"_orcid", $"_aux")
      .cache()

    joinedRow.show()
    joinedRow.printSchema()
    joinedRow.filter($"_orcid".isNotNull).show(300)

    MongoSpark.save(joinedRow.write.mode("overwrite"))
  }
}