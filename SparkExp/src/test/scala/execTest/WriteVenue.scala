package execTest

import com.mongodb.spark.MongoSpark
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import org.apache.spark.storage.StorageLevel
import org.scalatest.funsuite.AnyFunSuite

/**
 * 这个类记录了如何将spark的数据写入mongodb（使用手工设定的Schema,全部写入同一个集合）
 */
class WriteVenue extends AnyFunSuite {
  val onlyDoc = "onlyDoc"
  val venue = "venue"

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
    mongoDF.show()
    mongoDF.printSchema()

    val crossRef = mongoDF
      .filter($"crossref" isNotNull)
      .select($"crossref" as "ref")
      .dropDuplicates("ref")
      .cache()

    val crossReffed = crossRef
      .join(mongoDF, $"_key" === $"ref", "left")

    crossReffed.show(300)
    crossReffed.printSchema()

    MongoSpark.save(crossReffed.write.mode(SaveMode.Overwrite))
  }

}
