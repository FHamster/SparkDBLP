package cn.jmu.spark_dblp.server.util.ADT

import cn.jmu.spark_dblp.server.entity.OnlyDoc
import cn.jmu.spark_dblp.server.util.{InsensitiveMongoVisitor, InsensitivePredicateVisitor}
import com.github.rutledgepaulv.qbuilders.visitors.{MongoVisitor, PredicateVisitor, RSQLVisitor}
import com.github.rutledgepaulv.rqe.pipes.QueryConversionPipeline
import org.springframework.data.mongodb.core.query.Criteria

import java.util
import java.util.function.Predicate
import scala.annotation.tailrec

class RSQLFilter[A](private val l: List[String], private val c: Class[A]) extends Monoid[RSQLFilter[A]] {
  override final def zero: RSQLFilter[A] = new RSQLFilter[A](scala.collection.immutable.Nil, c)

  override def *(that: RSQLFilter[A]): RSQLFilter[A] = new RSQLFilter(l ++ that.l distinct, c)

  override def equal(that: RSQLFilter[A]): Boolean = {
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

  def toStreamPredicate: Predicate[A] = {
    val pVisitor: PredicateVisitor[A] = new InsensitivePredicateVisitor[A]()
    val string2P: String => Predicate[A] = (l: String) => QueryConversionPipeline.defaultPipeline.apply(l, classOf[OnlyDoc])
      .query(pVisitor)

    val pTrue: Predicate[A] = _ => true
    l.map(string2P).fold(pTrue)(_ and _)
  }


  def toMongo: Criteria = {
    val mVisitor: MongoVisitor = new InsensitiveMongoVisitor()
    val string2M: String => Criteria = (l: String) => QueryConversionPipeline.defaultPipeline.apply(l, classOf[OnlyDoc])
      .query(mVisitor)

    def toJL[A]: List[A] => util.List[A] = l => {
      scala
        .collection
        .JavaConverters
        .bufferAsJavaList(l.toBuffer)
    }

    string2M(String.join(";", toJL(l)))
  }
}

object RSQLFilter {
  def apply[A](c: Class[A], rsql: String): RSQLFilter[A] = {
    QueryConversionPipeline.defaultPipeline()(rsql, c).query(new RSQLVisitor())
    new RSQLFilter(List(rsql), c)
  }

  def apply[A](c: Class[A], rsql: String*): RSQLFilter[A] = {
    @tailrec
    def check(rsql: String*): Unit =
      if (rsql.isEmpty) Unit
      else {
        QueryConversionPipeline.defaultPipeline()(rsql.head, c).query(new RSQLVisitor())
        check(rsql.tail: _*)
      }

    check(rsql: _*)
    new RSQLFilter(rsql.toList, c)
  }

  def apply[A](c: Class[A], rsql: List[String]): RSQLFilter[A] = {
    rsql.foreach(QueryConversionPipeline.defaultPipeline()(_, c).query(new RSQLVisitor()))
    new RSQLFilter(rsql, c)
  }

  def apply[A](c: Class[A]): RSQLFilter[A] = {
    new RSQLFilter[A](scala.collection.immutable.Nil, c)
  }
}
