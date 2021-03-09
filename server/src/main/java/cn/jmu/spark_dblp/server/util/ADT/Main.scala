package cn.jmu.spark_dblp.server.util.ADT

import cn.jmu.spark_dblp.server.entity.OnlyDoc

object Main extends App {
  val a1: RSQL = RSQL(classOf[OnlyDoc], "year==2010")
  val a2: RSQL = RSQL(classOf[OnlyDoc],
    "year==2010",
    "year==2012")
  val a3: RSQL = RSQL(classOf[OnlyDoc], List(
    "year==2012",
    "year==2010"
  ))

  println(a2 * a3)
  println(a2 equal a3)
  println((a1.zero * a1) equal (a1 * a1.zero))

}
