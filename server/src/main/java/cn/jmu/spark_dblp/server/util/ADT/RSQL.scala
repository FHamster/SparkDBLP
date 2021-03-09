package cn.jmu.spark_dblp.server.util.ADT

import cn.jmu.spark_dblp.server.entity.OnlyDoc
import com.github.rutledgepaulv.qbuilders.builders.GeneralQueryBuilder
import com.github.rutledgepaulv.qbuilders.conditions.Condition
import com.github.rutledgepaulv.qbuilders.visitors.RSQLVisitor
import com.github.rutledgepaulv.rqe.pipes.QueryConversionPipeline

import java.util.stream.Collectors
import scala.annotation.tailrec

class RSQL(private val l: List[String]) extends Monoid[RSQL] {
  override def zero: RSQL = new RSQL(scala.collection.immutable.Nil)

  override def *(that: RSQL): RSQL = new RSQL(l ++ that.l distinct)

  override def equal(that: RSQL): Boolean = {
    this.lexOrderL equals that.lexOrderL
  }

  /*
    private def lexOrderFun(list: List[String]): List[String] = {
      def lt = (o1: String, o2: String) => {
        o1 < o2
      }

      list.sortWith(lt)
    }
  */

  def timeLineL: List[String] = l

  def lexOrderL: List[String] = {
    def lt = (o1: String, o2: String) => {
      o1 < o2
    }

    l.sortWith(lt)
  }

  override def toString: String = this.lexOrderL.toString()

  def toCacheKey: String = toString

  def toCondition = {


  }
}

object RSQL {
  def apply[A](c: Class[A], rsql: String): RSQL = {
    QueryConversionPipeline.defaultPipeline()(rsql, c).query(new RSQLVisitor())
    new RSQL(List(rsql))
  }

  def apply[A](c: Class[A], rsql: String*): RSQL = {
    @tailrec
    def check(rsql: String*): Unit =
      if (rsql.isEmpty) Unit
      else {
        QueryConversionPipeline.defaultPipeline()(rsql.head, c).query(new RSQLVisitor())
        check(rsql.tail: _*)
      }

    check(rsql: _*)
    new RSQL(rsql.toList)
  }

  def apply[A](c: Class[A], rsql: List[String]): RSQL = {
    rsql.foreach(QueryConversionPipeline.defaultPipeline()(_, c).query(new RSQLVisitor()))
    new RSQL(rsql)
  }
}
