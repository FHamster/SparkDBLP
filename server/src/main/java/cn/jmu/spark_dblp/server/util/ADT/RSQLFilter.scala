package cn.jmu.spark_dblp.server.util.ADT

import cn.jmu.spark_dblp.server.entity.OnlyDoc
import cn.jmu.spark_dblp.server.util.{InsensitiveMongoVisitor, InsensitivePredicateVisitor}
import com.github.rutledgepaulv.qbuilders.visitors.{MongoVisitor, PredicateVisitor, RSQLVisitor}
import com.github.rutledgepaulv.rqe.pipes.QueryConversionPipeline
import org.springframework.data.mongodb.core.query.Criteria

import java.util
import java.util.function.Predicate
import scala.annotation.tailrec

/**
 * RSQL过滤器
 *
 * @param l RSQL语句序列，按照插入先后顺序排序
 * @param c 查询对象的Class
 * @tparam A 查询对象
 */
class RSQLFilter[A](private val l: List[String], private val c: Class[A]) extends Monoid[RSQLFilter[A]] {
  /**
   * 代数系统单位元
   */
  override final def zero: RSQLFilter[A] = new RSQLFilter[A](scala.collection.immutable.Nil, c)

  /**
   * 代数系统的运算
   */
  override def *(that: RSQLFilter[A]): RSQLFilter[A] = new RSQLFilter(l ++ that.l distinct, c)

  /**
   * RSQLRSQLFilter[A]除了满足幺半群的特性以外还满足交换性
   * 事实上，这是一个交换幺半群
   * 为了实现交换性在进行比较时统一以自然偏序进行比较
   *
   * @return 两个代数元素是否相等
   */
  override def equal(that: RSQLFilter[A]): Boolean = {
    this.lexOrderL equals that.lexOrderL
  }

  def take(n: Int): RSQLFilter[A] = new RSQLFilter(l.take(n), c)

  def last: RSQLFilter[A] = new RSQLFilter(List(l.last), c)

  def isEmpty: Boolean = l.isEmpty

  def nonEmpty: Boolean = l.nonEmpty

  def size: Int = l.size

  def reverse: RSQLFilter[A] = new RSQLFilter(l.reverse, c)

  def timeLineL: List[String] = l

  /**
   * 自然偏序的比较器
   * 自然偏序按照字典序
   */
  private def lt = (o1: String, o2: String) => {
    o1 < o2
  }

  /**
   * 字典序的RSQL序列
   */
  def lexOrderL: List[String] = l.sortWith(lt)

  override def toString: String = this.lexOrderL.toString()

  /**
   * 转换为缓存key的方法
   *
   * @return key
   */
  def toCacheKey: String = toString

  /**
   * 转换为Java8函数式API的断言（Predicate）
   *
   * @return predicate
   */
  def toStreamPredicate: Predicate[A] = {
    val pVisitor: PredicateVisitor[A] = new InsensitivePredicateVisitor[A]()
    val string2P: String => Predicate[A] = (l: String) => QueryConversionPipeline.defaultPipeline.apply(l, c)
      .query(pVisitor)

    val pTrue: Predicate[A] = _ => true
    l.map(string2P).fold(pTrue)(_ and _)
  }

  /**
   * 转换为MongoDB的查询对象
   *
   * @return criteria
   */
  def toMongo: Criteria = {
    val mVisitor: MongoVisitor = new InsensitiveMongoVisitor()
    val string2M: String => Criteria = (l: String) => QueryConversionPipeline.defaultPipeline.apply(l, c)
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
//  def apply[A](c:Class[A])(rsql: String*):RSQLFilter[A]={}

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
