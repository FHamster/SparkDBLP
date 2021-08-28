package script

import com.mongodb.spark.MongoSpark
import com.mongodb.spark.config.ReadConfig
import com.mongodb.spark.sql.toSparkSessionFunctions
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.scalatest.funsuite.AnyFunSuite
import property.PropertiesObj.dataBaseName

object TitleWordCount extends App {
  val spark: SparkSession = SparkSession
    .builder
    .appName("TitleWordCount")
    .master("local[*]")
    .getOrCreate()

  import spark.implicits._

  val noMeanWord = List(
    "use", "Use", "case", "Case",
    "why", "Why", "what", "What", "which", "Which", "why", "Why", "where", "Where", "whose", "Whose",
    "How",
    "how",
    "From",
    "from",
    "for", "For",
    "and",
    "the",
    "with",
    "Using",
    "The",
    "using",
    "Based",
    "based",
    "via",
    "New",
    "With",
    "through",
    "over",
    "Case",
    "new",
    "under",
    "via",
    "New",
    "With",
    "through",
    "over",
    "Case",
    "new",
    "under",
    "be",
    "am",
    "is",
    "are",
    "feel",
    "look",
    "smell",
    "sound",
    "taste",
    "seem",
    "appear",
    "get",
    "become",
    "turn",
    "grow",
    "make",
    "come",
    "go",
    "fall",
    "run",
    "remain",
    "keep",
    "stay",
    "continue",
    "stand",
    "rest",
    "lie",
    "hold",
    "become",
    "make",
    "look",
    "sound",
    "fall",
    "turn",
    "He",
    "turned",
    "teacher",
    "prove",
    "remain",
    "be",
    "have",
    "has",
    "do",
    "does",
    "did",
    "shall",
    "should",
    "will",
    "would",
    "will",
    "would",
    "shall",
    "should",
    "can",
    "could",
    "may",
    "might",
    "must",
    "need",
    "dare",
    "ought",
    "to",
    "used",
    "to",
    "had",
    "better")


  val titleWords = spark.loadFromMongoDB(ReadConfig(
    Map("uri" -> s"mongodb://localhost/SparkDBLP.onlyDoc")
  ))
    .select(explode($"prefixIndex") as "word")
    .groupBy($"word")
    .count()
    .withColumn("wordLength", length($"word"))
    .filter($"count" > 50)
    .filter($"wordLength" > 2)
    .filter($"word" rlike "^\\w")
    .filter($"word" rlike "^[^\\d-:]+$")
    .filter(($"word" rlike "</?i>") notEqual true)
    .filter(($"word" rlike "</?tt>") notEqual true)
    .filter(($"word" rlike "</?sub>") notEqual true)
    .filter(($"word" rlike "</?sup>") notEqual true)
    .filter(($"word" isInCollection noMeanWord) notEqual true)

  titleWords.sample(0.1).show(100)

  MongoSpark.save(titleWords.write
    .option("uri", s"mongodb://127.0.0.1/$dataBaseName.wordCount")
    .mode(SaveMode.Overwrite)
  )
  spark.close()
}
