package cn.jmu.spark_dblp.server.util.ADT

import cn.jmu.spark_dblp.server.entity.OnlyDoc

object Main extends App {
  val a: RSQL[OnlyDoc] = RSQL("year==2010", classOf[OnlyDoc])

  val al = List(
    "year==2010",
    "year==2011"
  )

  val bl = List(
    "year==2011",
    "year==2010"
  )

  val m = Monoid.RSQLMonoid2

  println(m.op(al, m.zero))
  println(m.op(al, bl))
  println(m.equal(al, bl))
}
