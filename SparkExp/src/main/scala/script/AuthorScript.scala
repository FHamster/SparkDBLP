package script

import com.mongodb.spark.config.ReadConfig
import com.mongodb.spark.sql.toSparkSessionFunctions
import org.apache.spark.sql.functions.{explode, split}
import org.apache.spark.sql.{DataFrame, SparkSession}
import property.PropertiesObj

object AuthorScript {

  val AuthorTemp = "AuthorTemp"
  val onlyDoc = "onlyDoc"
  val DistinctAuthor = "DistinctAuthor"
  val indexPattern = "[\\s(),.]"
  val dataBaseName = PropertiesObj.dataBaseName

  lazy val spark: SparkSession = SparkSession
    .builder
    .appName("write author")
    .master("local[*]")
    //    .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/$dataBaseName.$DistinctAuthor")
    //    .config("spark.mongodb.input.uri", s"mongodb://127.0.0.1/$dataBaseName.$onlyDoc")
    .getOrCreate()
  spark.loadFromMongoDB(ReadConfig(
    Map("uri" -> s"mongodb://127.0.0.1/$dataBaseName.$onlyDoc")
  ))

  import spark.implicits._

  lazy val mongoDF: DataFrame = spark.loadFromMongoDB(ReadConfig(
    Map("uri" -> s"mongodb://127.0.0.1/$dataBaseName.$onlyDoc")
  ))
    .select($"author")
    .filter($"author" isNotNull)
    .select(explode($"author") as "author")
    .select($"author._VALUE" as "_VALUE",
      $"author._orcid" as "_orcid"
      //        $"author._aux" as "_aux"
    )
  lazy val orcidNotNull: DataFrame = mongoDF
    .filter($"_orcid" isNotNull)
    .dropDuplicates("_VALUE")
    .select($"_VALUE" as "noUseValue", $"_orcid")
  lazy val orcidNull: DataFrame = mongoDF
    .filter($"_orcid" isNull)
    .dropDuplicates("_VALUE")
    .select($"_VALUE")

  lazy val joinedRow: DataFrame = orcidNull
    .join(orcidNotNull, $"_VALUE" === $"noUseVALUE", "leftouter")
    .select($"_VALUE", $"_orcid")
    .withColumn("prefixIndex", split($"_VALUE", indexPattern))

}
