import java.util.Properties

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite


/**
 * 连接jdbc的测试
 * @deprecated 这个东西写着玩的
 */
final class jdbcdump extends AnyFunSuite with BeforeAndAfterAll {
  private lazy val spark: SparkSession = {
    // It is intentionally a val to allow import implicits.
    SparkSession.builder()
      .master("local[*]")
      .appName("DBLPTest")
      .config("spark.ui.enabled", value = false)
      .getOrCreate()
  }

  private lazy val conPro: Properties = {
    import scala.collection.JavaConverters._
    val conPro = Map(
      "user" -> "root",
      "password" -> "Gaoxin459716010@163"
    )

    val a: Properties = new Properties()
    a.putAll(mapAsJavaMapConverter[String, String](conPro).asJava)
    a
  }


  override protected def beforeAll(): Unit = {
    super.beforeAll()
  }


  override protected def afterAll(): Unit = {
    try {
      spark.stop()
    } finally {
      super.afterAll()
    }
  }

  /*test("admin_region") {
    val jdbcDataFrame: DataFrame = spark.read.jdbc(
      url = "jdbc:mysql://114.116.39.130:3306/dolldrobe",
      table = "administrativeregion",
      properties = conPro
    ).cache()


    jdbcDataFrame.write.jdbc(
      url = "jdbc:mysql://114.116.39.130:3306/dolldrobe3",
      table = "administrativeregion",
      connectionProperties = conPro
    )
  }*/

 /* test("commodity") {
    val jdbcDataFrame: DataFrame = spark.read.jdbc(
      url = "jdbc:mysql://114.116.39.130:3306/dolldrobe",
      table = "commodity",
      properties = conPro
    ).cache()

    jdbcDataFrame.printSchema()

    jdbcDataFrame.show()


    import spark.implicits._
    jdbcDataFrame.select(
      $"C_Num" as "c_num",
      $"S_Num" as "s_num",
      $"C_Name" as "c_name",
      $"C_MinMoney" as "c_minmoney",
      $"C_Img" as "c_img"
    ).write.jdbc(
      url = "jdbc:mysql://114.116.39.130:3306/dolldrobe3",
      table = "commodity3",
      connectionProperties = conPro
    )
  }*/
}
