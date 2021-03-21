package cn.jmu.spark_dblp.server.util

import cn.jmu.spark_dblp.server.entity.OnlyDoc
import cn.jmu.spark_dblp.server.util.ADT.RSQLFilter
import org.scalatest.funsuite.AnyFunSuite


class RSQLFilterTest extends AnyFunSuite {
  val con: Seq[String] => RSQLFilter[OnlyDoc] = RSQLFilter(classOf[OnlyDoc])

  val rsql1: RSQLFilter[OnlyDoc] = con(List(
    "year>2015",
    "type_xml==inproceedings"))
  val rsql2: RSQLFilter[OnlyDoc] = con(List(
    "type_xml==inproceedings",
    "year>2015"
  ))

  val rsql3: RSQLFilter[OnlyDoc] = con(List(
    "title=re=spark"
  ))

  val rsql4: RSQLFilter[OnlyDoc] = con(List(
    "title>2015"
  ))
  val rsql5: RSQLFilter[OnlyDoc] = con(List(
    "title<=2020"
  ))

  val e: RSQLFilter[OnlyDoc] = con(List.empty)

  test("Commutative Monoid multiplication commutativity") {
    assert(rsql2 equal rsql1)
  }
  test("monoid multiplication associativity") {
    assert(
      ((rsql3 * rsql4) * rsql5) equal (rsql3 * (rsql4 * rsql5))
    )
  }
  test("monoid e ") {
    assert((e * rsql1) equal (rsql1 * e))
  }


}