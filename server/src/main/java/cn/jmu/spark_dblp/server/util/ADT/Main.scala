package cn.jmu.spark_dblp.server.util.ADT

import cn.jmu.spark_dblp.server.entity.OnlyDoc

object Main extends App {
  val con = RSQLFilter(classOf[OnlyDoc])
  val a1: RSQLFilter[OnlyDoc] = con(List("year==2010", "title=re=hadoop"))
  val a2: RSQLFilter[OnlyDoc] = con(List(
    "year==2010",
    "year==2012"))
  val a3: RSQLFilter[OnlyDoc] = con(List(
    "year==2012",
    "year==2010"
  ))

  println(a2 * a3)
  println(a2 equal a3)
  println((a1.e * a1) equal (a1 * a1.e))


  val af = a1.toStreamPredicate

  println(a1.toMongo)
}
