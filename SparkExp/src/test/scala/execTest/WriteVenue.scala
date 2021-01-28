package execTest

import com.mongodb.spark.MongoSpark
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import org.scalatest.funsuite.AnyFunSuite
import org.apache.spark.sql.functions._

/**
 * 这个类记录了如何将spark的数据写入mongodb（使用手工设定的Schema,全部写入同一个集合）
 */
class WriteVenue extends AnyFunSuite {
  val onlyDoc = "onlyDoc"
  val venue = "venue"
  val notvenue = "notvenue"

  import util.UDFObject

  val writeNotNull: UserDefinedFunction = udf(UDFObject.writeNotNull _)

  /**
   * @deprecated 需要对venue进行分组以后入库，这测试完成了不分组方式的探索，结束使命
   */
  test("venue") {
    val sparkSession: SparkSession = SparkSession
      .builder
      .appName("venue")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.allSet")
      .config("spark.mongodb.input.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$onlyDoc")
      .getOrCreate()
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

//    MongoSpark.save(crossReffed.write.mode(SaveMode.Overwrite))
  }

  test("venue group") {
    val sparkSession: SparkSession = SparkSession
      .builder
      .appName("venue")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$venue")
      .config("spark.mongodb.input.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$onlyDoc")
      .getOrCreate()
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
      .drop("booktitle1", "booktitle2")
      .withColumnRenamed("newbooktitle", "booktitle")
      .filter($"prefix1" equalTo "conf/")
      .groupBy("prefix2")
      .agg(collect_list($"booktitle") as "booktitle",
        collect_list($"_key") as "_key",
        collect_list($"title") as "title",
        collect_list($"year") as "year",
        collect_list($"type") as "type",
        collect_list($"type_xml") as "type_xml",
        collect_list($"ref") as "crossref",
        collect_set($"title") as "titleset",
        collect_set($"booktitle") as "booktitleset"
      )
      .select(
        $"prefix2",
        $"titleset" as "title",
        $"booktitleset" as "booktitle",
        arrays_zip($"booktitle", $"_key", $"title", $"year", $"type", $"type_xml", $"crossref") as "venue"
      )
    crossReffed.show(100)
    MongoSpark.save(crossReffed.write.mode(SaveMode.Overwrite))
  }

  test("book&ref group") {
    val sparkSession: SparkSession = SparkSession
      .builder
      .appName("venue")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.book&ref")
      .config("spark.mongodb.input.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$onlyDoc")
      .getOrCreate()
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
      .filter(($"prefix1" equalTo "reference/") || ($"prefix1" equalTo "books/"))
      .groupBy("prefix1", "prefix2")
      .agg(collect_list($"booktitle") as "booktitle",
        collect_list($"_key") as "_key",
        collect_list($"year") as "year",
        collect_list($"type") as "type",
        collect_list($"type_xml") as "type_xml",
        collect_list($"title") as "title"
      )
      .select(
        $"prefix1",
        $"prefix2",
        arrays_zip($"booktitle", $"_key", $"title", $"year", $"type", $"type_xml") as "venue"
      )

    MongoSpark.save(crossReffed.write.mode(SaveMode.Overwrite))
  }
  test("journals index") {
    val sparkSession: SparkSession = SparkSession
      .builder
      .appName("venue")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.journalIndex")
      .config("spark.mongodb.input.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$onlyDoc")
      .getOrCreate()
    import sparkSession.implicits._
    val mongoDF: DataFrame = MongoSpark.load(sparkSession)


    val journals = mongoDF
      //      .filter($"prefix1" equalTo lit("journals/"))
      .filter($"journal" isNotNull)
      .filter($"volume" isNotNull)
      .groupBy("prefix1", "prefix2")
      .agg(
        array_sort(collect_set("volume")) as "volume", //volume的数组元素有的是int有的是string
        collect_set("journal") as "journal" //需要去重
      )
      /*  .agg(
          collect_list($"_key") as "_key",
          collect_list($"author") as "author",
          collect_list($"ee") as "ee",
          collect_list($"number") as "number",
          collect_list($"pages") as "pages",
          collect_list($"title") as "title",
          collect_list($"year") as "year",
          collect_list($"type") as "type",
          collect_list($"type_xml") as "type_xml",
        )*/
      .select(
        $"prefix1",
        $"prefix2",
        $"volume",
        $"journal",
        //        arrays_zip($"_key",$"author",$"ee", $"number",$"pages", $"title", $"year", $"type", $"type_xml") as "group"
      )

    MongoSpark.save(journals.write.mode(SaveMode.Overwrite))
  }
  test("venue index") {
    val sparkSession: SparkSession = SparkSession
      .builder
      .appName("venue")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.venueIndex")
      .config("spark.mongodb.input.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$onlyDoc")
      .getOrCreate()
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
      .drop("booktitle1", "booktitle2")
      .withColumnRenamed("newbooktitle", "booktitle")
      .filter($"prefix1" equalTo "conf/")
      .groupBy("prefix2")
      .agg(collect_list($"booktitle") as "booktitle",
        collect_list($"_key") as "_key",
        collect_list($"title") as "title",
        collect_list($"year") as "year",
        collect_list($"type") as "type",
        collect_list($"type_xml") as "type_xml",
        collect_list($"ref") as "crossref",
        collect_set($"title") as "titleset",
        collect_set($"booktitle") as "booktitleset"
      )
      .select(
        $"prefix2",
        $"titleset" as "title",
        $"booktitleset" as "booktitle",
        arrays_zip($"booktitle", $"_key", $"title", $"year", $"type", $"type_xml", $"crossref") as "venue"
      )

    crossReffed.show(100)
    MongoSpark.save(crossReffed.write.mode(SaveMode.Overwrite))
  }
}
