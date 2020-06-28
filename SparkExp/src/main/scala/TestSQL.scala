import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

/**
 * htprj20191023
 * 通过SQLContext测试XPath
 *
 * http://spark.apache.org/docs/2.3.0/api/sql/#xpath
 */

object TestSQL {

  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("TestMysql").setMaster("local")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)

    // sqlContext.sql("SELECT xpath('<a><b>b1</b><b>b2</b><b>b3</b><c>c1</c><c>c2</c></a>','a/b/text()')").show()   //OK!
    val result = sqlContext.sql("SELECT xpath('<root><X><A><F/></A></X><A><B><E/></B></A><A><C></C></A><A1><D><A><B s=\"abcabc\">" +
      "<Y p=\"1111\" q=\"2222\">yyyyyyy</Y></B></A></D></A1><A><X><B><T>tttttt" +
      "<M>mmmm</M></T></B></X></A></root>',       '//A'      )")

    // result.show()

    //    result.foreach(row => println(row))
    //result.write.format("json").save("c:/out/rrr.txt") //xxxxxxx
    //    result.write.xml("rrr.txt") //OK

    //测试发现，//和/是有区分的。
    //如有结果的查询： //A，root/A，//A[B]，//M/text()，//T/text()，//@p

  }

}