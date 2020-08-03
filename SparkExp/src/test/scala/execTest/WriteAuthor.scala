package execTest

import property._
import com.databricks.spark.xml.XmlDataFrameReader
import com.mongodb.spark.MongoSpark
import org.apache.spark.sql.functions.{explode, split}
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import org.scalatest.funsuite.AnyFunSuite

/**
 * 写入author信息,并对相同author的orcid进行合并
 */
class WriteAuthor extends AnyFunSuite {

  val AuthorTemp = "AuthorTemp"
  val onlyDoc = "onlyDoc"
  val DistinctAuthor = "DistinctAuthor"
  val indexPattern = "[\\s(),.]"
  /**
   * @deprecated 数据源更改为onlydoc，不从xml文件直接读取
   */
  test("article") {
    val subnode = "article"
    import ss.implicits.StringToColumn
    val ss: SparkSession = SparkSession
      .builder
      .appName(s"Write_${subnode}_author")
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

    println(s"write $subnode into mongodb")
    MongoSpark.save(res.write.mode(SaveMode.Append))
    ss.stop()
  }
  /**
   * @deprecated 数据源更改为onlydoc，不从xml文件直接读取
   */
  test("inproceedings") {
    val subnode = "inproceedings"
    import ss.implicits.StringToColumn
    val ss: SparkSession = SparkSession
      .builder
      .appName(s"Write_${subnode}_author")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$AuthorTemp")
      .getOrCreate()
    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.inproceedingsSchema)
      .xml(PropertiesObj.wholeDBLP_cvtSparkPath)
    val res = opt
      .select(explode($"author") as "author")
      .select($"author._VALUE" as "_VALUE",
        $"author._orcid" as "_orcid",
        $"author._aux" as "_aux"
      )

    println(s"write $subnode into mongodb")
    MongoSpark.save(res.write.mode(SaveMode.Append))
    ss.stop()
  }
  /**
   * @deprecated 数据源更改为onlydoc，不从xml文件直接读取
   */
  test("proceedings") {
    val subnode = "proceedings"
    import ss.implicits.StringToColumn
    val ss: SparkSession = SparkSession
      .builder
      .appName(s"Write_${subnode}_author")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$AuthorTemp")
      .getOrCreate()

    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.proceedingsSchema)
      .xml(PropertiesObj.wholeDBLP_cvtSparkPath)

    val res = opt
      .select(explode($"author") as "author")
      .select($"author._VALUE" as "_VALUE",
        $"author._orcid" as "_orcid",
        $"author._aux" as "_aux"
      )

    println(s"write $subnode into mongodb")
    MongoSpark.save(res.write.mode(SaveMode.Append))
    ss.stop()
  }
  /**
   * @deprecated 数据源更改为onlydoc，不从xml文件直接读取
   */
  test("book") {
    val subnode = "book"
    import ss.implicits.StringToColumn
    val ss: SparkSession = SparkSession
      .builder
      .appName(s"Write_${subnode}_author")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$AuthorTemp")
      .getOrCreate()

    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.bookSchema)
      .xml(PropertiesObj.wholeDBLP_cvtSparkPath)

    val res = opt
      .select(explode($"author") as "author")
      .select($"author._VALUE" as "_VALUE",
        $"author._orcid" as "_orcid",
        $"author._aux" as "_aux"
      )

    println(s"write $subnode into mongodb")
    MongoSpark.save(res.write.mode(SaveMode.Append))
    ss.stop()
  }
  /**
   * @deprecated 数据源更改为onlydoc，不从xml文件直接读取
   */
  test("incollection") {
    val subnode = "incollection"
    import ss.implicits.StringToColumn
    val ss: SparkSession = SparkSession
      .builder
      .appName(s"Write_${subnode}_author")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$AuthorTemp")
      .getOrCreate()

    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.incollectionSchema)
      .xml(PropertiesObj.wholeDBLP_cvtSparkPath)

    val res = opt
      .select(explode($"author") as "author")
      .select($"author._VALUE" as "_VALUE",
        $"author._orcid" as "_orcid",
        $"author._aux" as "_aux"
      )
    println(s"write $subnode into mongodb")
    MongoSpark.save(res.write.mode(SaveMode.Append))
    ss.stop()
  }
  /**
   * @deprecated 数据源更改为onlydoc，不从xml文件直接读取
   */
  test("phdthesis") {
    val subnode = "phdthesis"
    import ss.implicits.StringToColumn
    val ss: SparkSession = SparkSession
      .builder
      .appName(s"Write_${subnode}_author")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$AuthorTemp")
      .getOrCreate()

    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.phdthesisSchema)
      .xml(PropertiesObj.wholeDBLP_cvtSparkPath)

    val res = opt
      .select(explode($"author") as "author")
      .select($"author._VALUE" as "_VALUE",
        $"author._orcid" as "_orcid",
        $"author._aux" as "_aux"
      )

    println(s"write $subnode into mongodb")
    MongoSpark.save(res.write.mode(SaveMode.Append))
    ss.stop()
  }
  /**
   * @deprecated 数据源更改为onlydoc，不从xml文件直接读取
   */
  test("mastersthesis") {
    val subnode = "mastersthesis"

    import ss.implicits.StringToColumn
    val ss: SparkSession = SparkSession
      .builder
      .appName(s"Write_${subnode}_author")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$AuthorTemp")
      .getOrCreate()

    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.mastersthesisSchema)
      .xml(PropertiesObj.wholeDBLP_cvtSparkPath)

    val res = opt
      .select(explode($"author") as "author")
      .select($"author._VALUE" as "_VALUE",
        $"author._orcid" as "_orcid",
        $"author._aux" as "_aux"
      )

    println(s"write $subnode into mongodb")
    MongoSpark.save(res.write.mode(SaveMode.Append))
    ss.stop()
  }
  /**
   * @deprecated 数据源更改为onlydoc，不从xml文件直接读取
   */
  test("www") {
    val subnode = "www"
    val ss: SparkSession = SparkSession
      .builder
      .appName(s"Write_${subnode}_author")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$AuthorTemp")
      .getOrCreate()

    val opt = ss.read
      .option("rootTag", "dblp")
      .option("rowTag", subnode)
      .schema(PropertiesObj.wwwSchema)
      .xml(PropertiesObj.wholeDBLP_cvtSparkPath)
    import ss.implicits.StringToColumn
    val res = opt
      .select(explode($"author") as "author")
      .select($"author._VALUE" as "_VALUE",
        $"author._orcid" as "_orcid",
        $"author._aux" as "_aux"
      )

    println(s"write $subnode into mongodb")
    MongoSpark.save(res.write.mode(SaveMode.Append))
    ss.stop()
  }
  /**
   * @deprecated 数据源更改为onlydoc，不从xml文件直接读取
   */
  test("distinct author") {
    val sparkSession: SparkSession = SparkSession
      .builder
      .appName("in")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$DistinctAuthor")
      .config("spark.mongodb.input.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$onlyDoc")
      .getOrCreate()
    import sparkSession.implicits._
    val mongoDF: DataFrame = MongoSpark
      .load(sparkSession)
      .select($"author")
      .filter($"author" isNotNull)
      .select(explode($"author") as "author")
      .select($"author._VALUE" as "_VALUE",
        $"author._orcid" as "_orcid"
        //        $"author._aux" as "_aux"
      ).cache()
    mongoDF.show()

    val orcidNotNull = mongoDF
      .filter($"_orcid" isNotNull)
      .dropDuplicates("_VALUE")
      .select($"_VALUE" as "noUseValue", $"_orcid")
    val orcidNull = mongoDF
      .filter($"_orcid" isNull)
      .dropDuplicates("_VALUE")
      .select($"_VALUE")
    //      .select($"_VALUE", $"_aux")

    val joinedRow = orcidNull
      .join(orcidNotNull, $"_VALUE" === $"noUseVALUE", "leftouter")
      .select($"_VALUE", $"_orcid")
      //      .select($"_VALUE", $"_orcid", $"_aux")
      .cache()
    joinedRow.show()
    joinedRow.printSchema()
    //    joinedRow.filter($"_orcid".isNotNull).show(300)

    MongoSpark.save(joinedRow.write.mode(SaveMode.Overwrite))
  }

  test("write author") {
    val sparkSession: SparkSession = SparkSession
      .builder
      .appName("write author")
      .master("local[*]")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$DistinctAuthor")
      .config("spark.mongodb.input.uri", s"mongodb://127.0.0.1/SparkDBLPTest.$onlyDoc")
      .getOrCreate()
    import sparkSession.implicits._
    val mongoDF: DataFrame = MongoSpark
      .load(sparkSession)
      .select($"author")
      .filter($"author" isNotNull)
      .select(explode($"author") as "author")
      .select($"author._VALUE" as "_VALUE",
        $"author._orcid" as "_orcid"
        //        $"author._aux" as "_aux"
      )
    val orcidNotNull = mongoDF
      .filter($"_orcid" isNotNull)
      .dropDuplicates("_VALUE")
      .select($"_VALUE" as "noUseValue", $"_orcid")
    val orcidNull = mongoDF
      .filter($"_orcid" isNull)
      .dropDuplicates("_VALUE")
      .select($"_VALUE")
    //      .select($"_VALUE", $"_aux")

    val joinedRow = orcidNull
      .join(orcidNotNull, $"_VALUE" === $"noUseVALUE", "leftouter")
      .select($"_VALUE", $"_orcid")
      .withColumn("prefixIndex",split($"_VALUE",indexPattern) )
      //      .select($"_VALUE", $"_orcid", $"_aux")
//      .cache()
//    joinedRow.show()
//    joinedRow.printSchema()
//    joinedRow.filter($"_orcid".isNotNull).show(300)

    MongoSpark.save(joinedRow.write.mode(SaveMode.Overwrite))
  }
}