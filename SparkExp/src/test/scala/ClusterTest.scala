import java.nio.file.Path
import java.util.Properties

import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.scalatest.{BeforeAndAfterAll, FunSuite}

import scala.collection.mutable.Map

/*
"""
root
-- _key: string (nullable = true)
-- _mdate: string (nullable = true)
-- _publtype: string (nullable = true)
-- author: array (nullable = true)
    |-- element: struct (containsNull = true)
    |    |-- _VALUE: string (nullable = true)
    |    |-- _orcid: string (nullable = true)
-- cdrom: string (nullable = true)
-- ee: string (nullable = true)
-- journal: string (nullable = true)
-- month: string (nullable = true)
-- note: string (nullable = true)
-- publisher: string (nullable = true)
-- title: string (nullable = true)
-- url: string (nullable = true)
-- volume: string (nullable = true)
-- year: long (nullable = true)
"""
 */
//DataFrame可进行的操作的测试
//该测试类用于连接远程集群
//需要经过一些配置后该测试才可用
final class ClusterTest extends FunSuite with BeforeAndAfterAll {
  val sparkClusterUrl = "spark://localhost:7077"
  val hdfsURL: String = "hdfs://namenode:8020/data/article_after.xml"
  private lazy val spark: SparkSession = {
    // It is intentionally a val to allow import implicits.
    SparkSession.builder()
      //      .master("local[*]")
      .master(sparkClusterUrl)
      .appName("DBLPTest")
      .getOrCreate()
  }

  private lazy val dblpArticle: DataFrame = {
    import com.databricks.spark.xml._

    spark.read
      .option("rootTag", "dblp")
      .option("rowTag", "article")
      .xml(hdfsURL)
      .cache()
  }
  private var tempDir: Path = _

  override protected def beforeAll(): Unit = {
    super.beforeAll()

    //    spark // Initialize Spark session
    //    tempDir = Files.createTempDirectory("DBLPTestDir")
    //    tempDir.toFile.deleteOnExit()
  }


  override protected def afterAll(): Unit = {
    try {
      spark.stop()
    } finally {
      super.afterAll()
    }
  }

  test("1") {
    println(spark.sessionState)

    println("go")
    spark.read.textFile(hdfsURL).show()
    spark.read.textFile("hdfs://namenode:8020/data/article_after.xml").show()
  }
  test("show article dataframe") {
    dblpArticle.printSchema()
    dblpArticle.show()

    //    dblpArticle.
  }

  test("list all") {
    dblpArticle.show(100)
  }


}
