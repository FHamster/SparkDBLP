import org.apache.spark.sql.SparkSession

/*
(article,2093906)
(inproceedings,2450274)
(proceedings,41782)
(book,17714)
(incollection,59477)
(phdthesis,73252)
(mastersthesis,12)
(www,2346866)
(person,0)
(data,0)
 */


//统计dblp下的二级分类记录的数量
/** *******************************************
 * 该代码需要很长时间用于统计dblp下各个子节点的数量 *
 * ********************************************/
object CountSubClassNum extends App {

  val pwd = "/Users/gaoxin/IdeaProjects/SparkExp/"
  val wholeDBLP = "whole/dblp.xml"
  val wholeDBLP_Converted = "whole/dblp_cvt.xml"

  val spark = SparkSession
    .builder
    .appName("CountSubClassNum")
    .master("local[*]")
    .getOrCreate()
  // 子节点的列表
  val subNode = Seq("article", "inproceedings", "proceedings", "book",
    "incollection", "phdthesis", "mastersthesis", "www", "person", "data")

  import com.databricks.spark.xml._

  val subNodeRowNum = subNode.map(it => {
    val rowNum: Long = spark.read
      .option("rootTag", "dblp")
      .option("rowTag", it)
      .xml(wholeDBLP_Converted)
      .count()

    (it, rowNum)
  })

  subNodeRowNum.foreach(println)
  spark.stop()
}
