import java.util.Properties

import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite

import scala.collection.mutable

//本地的小规模Dataframe操作测试
final class DBLPTestClass extends AnyFunSuite with BeforeAndAfterAll {
  val testRes: String = "SparkExp/src/test/resources/article_after.xml"
  //  val testRes: String = "src/test/resources/article_CharTest.xml"

  //  val testRes: String = "hdfs://localhost:9000/testdata/hadoop_namenode/article_after.xml"
  private lazy val spark: SparkSession = {
    SparkSession.builder()
      .master("local[*]")
      .appName("DBLPTest")
      .config("spark.ui.enabled", value = false)
      .getOrCreate()
  }


  private lazy val dblpArticle: DataFrame = {
    import com.databricks.spark.xml._
    //    PropertiesObj.ManualArticleSchema.printTreeString()
    spark.read
      //.schema(PropertiesObj.ManualArticleSchema)//手动设定schema
      .option("rootTag", "dblp")
      .option("rowTag", "article")
      .xml(testRes)
      //      .xml(PropertiesObj.wholeDBLP_cvtSparkPath)
      .cache()
  }

  import spark.implicits._
  override protected def afterAll(): Unit = {
    try {
      spark.stop()
    } finally {
      super.afterAll()
    }
  }

  test("show article dataframe") {
    dblpArticle.printSchema()
    dblpArticle.show()
  }

  test("list all") {
    dblpArticle.show(100)
  }

  test("Select some column from article") {
    dblpArticle.select($"title", $"author", $"url")
      .show()
  }
  test("filter with number of long type") {
    dblpArticle.select($"title", $"author", $"year").filter($"year" > 2000)
      .show()
  }
  test("filter with exact string match") {
    dblpArticle.filter($"title" <=> "Knowledge in Operation")
      .show()
  }
  test("filter with sql like") {
    dblpArticle.filter($"title" like "%Knowledge%")
      .show()
  }

  test("filter with regex string") {
    dblpArticle.select($"title", $"author", $"url").filter($"title" rlike "^Knowledge").show()
  }

  test("filter with complex struct") {
    dblpArticle.filter(array_contains($"author._VALUE", "Paul Kocher"))
      .show()
  }

  test("export into array") {
    val cache: DataFrame = dblpArticle
      .filter(array_contains($"author._VALUE", "Paul Kocher"))
      .cache()

    cache.printSchema()
    val a: Array[Row] = cache.take(1)

    //    println(a)

    //    a(0).getList()
    a(0).schema.fieldNames.foreach(println(_));
    println(a(0).getAs(fieldName = "_key"));

    println(a(0).getAs(fieldName = "author"));
  }

  test("read dataframe schma") {
    val cache: DataFrame = dblpArticle
      .filter(array_contains($"author._VALUE", "Paul Kocher"))
      .cache()

    cache.printSchema()
    val a: Array[Row] = cache.take(1)

    val fields: Array[StructField] = a(0).schema.fields
    var map: mutable.Map[String, Any] = mutable.Map()
    fields.foreach((field: StructField) => {
      println(s"${field.name} = ${a(0).getAs(fieldName = field.name)} ")
      map += (field.name -> a(0).getAs(fieldName = field.name))
    })

    println(map)
  }

  test("writh json") {
    val cache: DataFrame = dblpArticle
      .filter(array_contains($"author._VALUE", "Paul Kocher"))
      .cache()
  }
  test("test if there is corrupt_record") {
    assert(!dblpArticle.columns.contains("_corrupt_record"))
  }
  test("explode author array") {
    //测试explode
    dblpArticle.select(explode($"author._VALUE") as "author2", $"_key", $"title")
      .show(100)
  }
  test("add column with regex expression") {
    //新增column
    dblpArticle
      .withColumn("prefix", regexp_extract($"_key", "\\S*/\\S*/", 0))
      .show(100)
  }
  test("merge author") {
    //测试join功能
    val orcidNotNull = dblpArticle
      .select(explode($"author") as "author")
      .select($"author._VALUE" as "_VALUE",
        $"author._orcid" as "_orcid",
        $"author._aux" as "_aux")
      .filter($"_orcid".isNotNull)
      .dropDuplicates("_VALUE")

    val orcidNull = dblpArticle
      .select(explode($"author") as "author")
      .select($"author._VALUE" as "_VALUE",
        $"author._orcid" as "_orcid",
        $"author._aux" as "_aux")
      .filter($"_orcid".isNull)
      .dropDuplicates("_VALUE")

    val joinedRow = orcidNull
      .join(orcidNotNull, orcidNull("_VALUE") === orcidNotNull("_VALUE"), "left")
      .select(orcidNull("_VALUE"), orcidNotNull("_orcid"))
      .show(200)
  }

  test("convert tag") {
    import util.UDFObject

    val rtoaAarse: UserDefinedFunction = udf(UDFObject.rtoaAarse _)
    val cvted = dblpArticle
      .select(rtoaAarse($"title"))
      .show()
  }
  test("write jdbc") {
    val cache: DataFrame = dblpArticle
      .select($"_key", $"url")
      .cache()
    cache.show(100)

    val properties: Properties = new Properties()

    properties.put("user", "root")
    properties.put("password", "Gaoxin459716010@163")

    //    cache.write.jdbc(url = "jdbc:mysql://114.116.39.130:3306/dblp", table = "dblp", connectionProperties = properties)
  }


  test("filter with xpath") {

    //xpath函数是sparksql的内置函数
    //由于需要把xml字符串作为入参传进去，觉得不太好用
    /*
    +-----------------------------------------------------------------------+
    |xpath(<a><b>b1</b><b>b2</b><b>b3</b><c>c1</c><c>c2</c></a>, a/b/text())|
    +-----------------------------------------------------------------------+
    |                                                           [b1, b2, b3]|
    +-----------------------------------------------------------------------+
     */
    spark.sqlContext.sql("""SELECT xpath('<a><b>b1</b><b>b2</b><b>b3</b><c>c1</c><c>c2</c></a>','a/b/text()')""").show()
  }
  test("read dblp by string") {
    //我尝试将数据源以字符串的形式读入，再转换为scala的xml字面量做处理
    //这需要进行一些处理，因为spark读取字符串是以行为单位读取的。
    //spark-xml似乎是用stax做的

    val a: xml.Elem =
      <article mdate="2018-01-07" key="tr/meltdown/s18" publtype="informal">
        <author>Paul Kocher</author>
        <author>Daniel Genkin</author>
        <title>Spectre Attacks: Exploiting Speculative Execution.</title>
        <journal>meltdownattack.com</journal>
        <year>2018</year>
        <ee>https://spectreattack.com/spectre.pdf</ee>
      </article>

    val s = a \ "author"
    println(s)

  }
}
