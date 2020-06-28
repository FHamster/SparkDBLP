import java.io.File

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.scalatest.funsuite.AnyFunSuite

/**
 * 对dblp进行实体转换的测试
 */
final class EntityConvertTest extends AnyFunSuite {
  val testText = "<title>&Uuml;ber Ans&auml;tze zur Darstellung von Konzepten und Prototypen</title>"
  test("test transfer character in line") {
    assertResult("<title>&#220;ber Ans&#228;tze zur Darstellung von Konzepten und Prototypen</title>") {
      ReplaceEntityUtil.parse(testText)
    }
  }

  val testTagString = "<title>The Many-Valued <i>Theorem</i> Prover<sub>3</sub>T<sup>A</sup>P.</title>"
  val testTag_cvtString = "<title>The Many-Valued (i)Theorem(/i) Prover(sub)3(/sub)T(sup)A(/sup)P.</title>"
  test("change Tag string") {
    assertResult(testTag_cvtString) {
      ReplaceTagUtil.atorParse(testTagString)
    }
  }

  test("change Tag string2") {
    assertResult(testTagString) {
      ReplaceTagUtil.rtoaAarse(testTagString)
    }
  }

  test("Convert whole dblp") {

    // 检查是否已经有转换后的文件
    // 如果有就删掉
    val testFile: File = new File(PropertiesObj.wholeDBLP_cvt)
    if (testFile.exists()) {
      println("delete " + testFile.delete())
    }

    val spark = SparkSession
      .builder
      .appName("ConvertWholeDBLP")
      .master("local[*]")
      .getOrCreate()

    //读取
    val DBLPLines: RDD[String] = spark.sparkContext.textFile(PropertiesObj.wholeDBLP_SparkPath)

    //转换实体
    val wholeDBLP_cvtRDD: RDD[String] = DBLPLines
      .map(ReplaceEntityUtil.parse)
      .map(ReplaceTagUtil.atorParse)


      wholeDBLP_cvtRDD.saveAsTextFile(PropertiesObj.wholeDBLP_cvtSparkPath)
  }

}