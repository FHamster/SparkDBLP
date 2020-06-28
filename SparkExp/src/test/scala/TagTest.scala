import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.scalatest.funsuite.AnyFunSuite

final class TagTest extends AnyFunSuite {


  test("change Tag") {
    val spark = SparkSession
      .builder
      .appName("XML_Test")
      .master("local[*]")
      //      .config("driver-memory", "4096M")
      //      .config("spark.executor.memory", "4G")
      //.config("spark.mongodb.output.uri","mongodb://127.0.0.1/scalaTest.after")
      //.config("spark.mongodb.input.uri","mongodb://127.0.0.1/scalaTest.after")
      .getOrCreate()

    val fileRDD: RDD[String] = spark.sparkContext.textFile(s"${PropertiesObj.pwd}/article.xml")

    val changeTagFile: RDD[String] = fileRDD.map(ReplaceTagUtil.atorParse)

    changeTagFile.saveAsTextFile(s"${PropertiesObj.pwd}/article_after_Tag.xml")
  }
}
