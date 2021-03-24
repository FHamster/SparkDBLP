import com.mongodb.spark.MongoSpark
import com.mongodb.spark.config.ReadConfig
import com.mongodb.spark.sql.toSparkSessionFunctions
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.sql.functions.explode
import org.scalatest.funsuite.AnyFunSuite
import property.PropertiesObj.dataBaseName

class StatisticTitle extends AnyFunSuite {
  val spark: SparkSession = SparkSession
    .builder
    .appName("StatisticTitle")
    .master("local[*]")
    //    .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$subNodeName")
    .getOrCreate()

  import spark.implicits._

  test("Statistic word appearance time") {
    val titleWords = spark.loadFromMongoDB(ReadConfig(
      Map("uri" -> s"mongodb://localhost/SparkDBLP.onlyDoc")
    ))
      .select(explode($"prefixIndex") as "word")
      .groupBy($"word")
      .count()
//      .sort($"count")

    MongoSpark.save(titleWords.write
      .option("uri", s"mongodb://127.0.0.1/$dataBaseName.wordCount")
      .mode(SaveMode.Overwrite)
    )
//    titleWords.show(30000)
  }
}
