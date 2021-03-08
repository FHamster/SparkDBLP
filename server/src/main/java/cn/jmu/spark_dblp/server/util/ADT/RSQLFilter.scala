package cn.jmu.spark_dblp.server.util.ADT

import com.github.rutledgepaulv.qbuilders.visitors.RSQLVisitor
import com.github.rutledgepaulv.rqe.pipes.QueryConversionPipeline

abstract class RSQLFilter[+A] {
  def head: A

  def tail: RSQLFilter[A]

  def ::[B >: A](x: B): RSQLFilter[B] = Cons(x, this)

  def apply[A](c: Class[A], s: String*): RSQLFilter[A] = {
    if (s.isEmpty) {
      Nil
    }
    else {
      QueryConversionPipeline.defaultPipeline()(s.head, c).query(new RSQLVisitor())
      //      Cons(s.head, apply(s.tail: _*))
      Nil
    }
  }
}

case object Nil extends RSQLFilter[Nothing] {
  override def head: Nothing = throw new NoSuchElementException("head of empty list")

  override def tail: RSQLFilter[Nothing] = throw new UnsupportedOperationException("tail of empty list")
}

case class Cons[A](override val head: A, t: RSQLFilter[A]) extends RSQLFilter[A] {
  override def tail: RSQLFilter[A] = t
}




