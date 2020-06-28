object  LogFilter {

  def main(args: Array[String]): Unit = {
    import scala.io.Source
    //以指定的UTF-8字符集读取文件，第一个参数可以是字符串或者是java.io.File
    val source = Source.fromFile("./whole/mongodbLog.txt", "UTF-8")
    //或取文件中所有行
    val lineIterator = source.getLines()

    lineIterator.filter(it=>{
      !(it.contains("20/05/03") || it.contains("20/04/17"))
    }).foreach(println)

  }
}
