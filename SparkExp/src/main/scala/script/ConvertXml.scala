package script

import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import property.PropertiesObj
import util.{ReplaceEntityUtil, ReplaceTagUtil}

object ConvertXml extends App {

  // 检查是否已经有转换后的文件
  // 如果有就删掉
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
  spark.close()
}
