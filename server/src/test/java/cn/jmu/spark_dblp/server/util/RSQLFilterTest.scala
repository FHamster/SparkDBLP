package cn.jmu.spark_dblp.server.util

import cn.jmu.spark_dblp.server.entity.OnlyDoc
import cn.jmu.spark_dblp.server.util.ADT.RSQLFilter
import org.scalatest.flatspec.AnyFlatSpec
import org.springframework.data.mongodb.core.query.Criteria

import java.util.function.Predicate


class RSQLFilterTest extends AnyFlatSpec {
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

  val e: RSQLFilter[OnlyDoc] = con(Nil)

  "A RSQLFilter Commutative Monoid" should "unsorted" in {
    assert(rsql2 equal rsql1)
  }
  it should "satisfy multiplication associativity" in {
    assert(
      ((rsql3 * rsql4) * rsql5) equal (rsql3 * (rsql4 * rsql5))
    )
  }
  it should "have a identity e" in {
    assert((e * rsql1) equal (rsql1 * e))
    assert((rsql1 * e) equal rsql1)
  }

  it can "be translated to String" in {
    assume(rsql1.toString.isInstanceOf[String])
  }

  it can "be translated to Java Predicate" in {
    assume(rsql1.toStreamPredicate.isInstanceOf[Predicate[OnlyDoc]])
  }

  it can "be translated to MongoDB Criteria" in {
    assume(rsql1.toMongo.isInstanceOf[Criteria])
  }

  it can "be translated to Cache Key" in {
    assume(rsql1.toString.isInstanceOf[String])
  }
}