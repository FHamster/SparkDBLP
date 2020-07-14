import com.databricks.spark.xml.XmlDataFrameReader
import com.mongodb.spark.MongoSpark
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.functions.{explode, regexp_extract}
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import property.{Author, PropertiesObj}

/**
 * 导入数据的执行脚本
 */
object ExecScript extends App {
  val prefixRegex2 = "^(\\S*?)/(\\S*?)/"
  val prefixRegex1 = "^(\\S*?)/"
  val onlyDoc = "onlyDoc"
  val DistinctAuthor = "DistinctAuthor"
  val AuthorTemp = "AuthorTemp"
  Logger.getRootLogger.setLevel(Level.INFO)
  //写入所有子节点到多个集合
  PropertiesObj.subNode.foreach(subnode => {
    import ss.implicits.StringToColumn
//    ss.
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
      .withColumn("prefix1", regexp_extract($"_key", prefixRegex1, 0))
      .withColumn("prefix2", regexp_extract($"_key", prefixRegex2, 0))

    Logger.getLogger("UserLogger").warn(s"write $subnode into mongodb")
    MongoSpark.save(opt.write.mode(SaveMode.Overwrite))
    ss.stop()
  })

  //写入所有的author至同一集合
  PropertiesObj.subNode.foreach(subnode => {
    import ss.implicits.StringToColumn
    val ss: SparkSession = SparkSession
      .builder
      .appName("Write_article")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$AuthorTemp")
      .getOrCreate()

    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.articleSchema)
      .xml(PropertiesObj.wholeDBLP_cvtSparkPath)


    val res = opt
      .select(explode($"author") as "author")
      .select($"author._VALUE" as "_VALUE",
        $"author._orcid" as "_orcid",
        $"author._aux" as "_aux"
      )
    Logger.getLogger("UserLogger").warn(s"write $subnode into mongodb")
    MongoSpark.save(res.write.mode(SaveMode.Append))
    ss.stop()
  })

  //对author去重，并存入DistinctAuthor集合
  val sparkSession: SparkSession = SparkSession
    .builder
    .appName("in")
    .master("local[*]")
    .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$DistinctAuthor")
    .config("spark.mongodb.input.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$AuthorTemp")
    .getOrCreate()

  import sparkSession.implicits._

  val mongoDF: DataFrame = MongoSpark.load[Author](sparkSession).cache()
  mongoDF.show()

  val orcidNotNull = mongoDF
    .filter($"_orcid".isNotNull)
    .dropDuplicates("_VALUE")
    .select($"_VALUE" as "noUseValue", $"_orcid")
  val orcidNull = mongoDF
    .filter($"_orcid".isNull)
    .dropDuplicates("_VALUE")
    .select($"_VALUE", $"_aux")

  val joinedRow = orcidNull
    .join(orcidNotNull, $"_VALUE" === $"noUseVALUE", "leftouter")
    .select($"_VALUE", $"_orcid", $"_aux")
    .cache()
  Logger.getLogger("UserLogger").warn(s"join complete")
  MongoSpark.save(joinedRow.write.mode(SaveMode.Overwrite))

  //写入所有数据至同一集合
  PropertiesObj.subNode.foreach(subnode => {
    import ss.implicits.StringToColumn
    val ss: SparkSession = SparkSession
      .builder
      .appName("Write_article")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$onlyDoc")
      .getOrCreate()

    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.articleSchema)
      .xml(PropertiesObj.wholeDBLP_cvtSparkPath)
      .withColumn("prefix1", regexp_extract($"_key", prefixRegex1, 0))
      .withColumn("prefix2", regexp_extract($"_key", prefixRegex2, 0))

    Logger.getLogger("UserLogger").warn(s"write $subnode into only set")
    MongoSpark.save(opt.write.mode(SaveMode.Append))
    ss.stop()
    ss.stop()
  })
}
