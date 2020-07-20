package execTest

import com.mongodb.spark.MongoSpark
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import org.scalatest.funsuite.AnyFunSuite
import org.apache.spark.sql.functions._

/**
 * 这个类记录了如何将spark的数据写入mongodb（使用手工设定的Schema,全部写入同一个集合）
 */
class WriteVenue extends AnyFunSuite {
  val onlyDoc = "onlyDoc"
  val venue = "venue"

  import util.UDFObject

  val writeNotNull = udf(UDFObject.writeNotNull _)

  test("venue") {
    val sparkSession: SparkSession = SparkSession
      .builder
      .appName("venue")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$venue")
      .config("spark.mongodb.input.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$onlyDoc")
      .getOrCreate()
    import sparkSession.implicits._
    val mongoDF: DataFrame = MongoSpark.load(sparkSession)
      .select("_key", "booktitle", "crossref", "crossref", "journal", "prefix1", "prefix2", "title", "type", "type_xml", "year")
      .cache()
    mongoDF.show()
    mongoDF.printSchema()

    val crossRef = mongoDF
      .filter($"crossref" isNotNull)
      .select($"crossref" as "ref", $"booktitle" as "booktitle2")
      .dropDuplicates("ref")
    //      .cache()
    crossRef.show(100)
    crossRef.printSchema()
    val crossReffed = crossRef
      .join(mongoDF, $"_key" === $"ref", "left")
      .withColumnRenamed("booktitle", "booktitle1")
      .withColumn("newbooktitle", writeNotNull($"booktitle1", $"booktitle2", $"title"))
//      .drop("booktitle1", "booktitle2")
      .withColumnRenamed("newbooktitle", "booktitle")
    crossReffed.show(500)
    crossReffed.printSchema()

    MongoSpark.save(crossReffed.write.mode(SaveMode.Overwrite))
  }

}
