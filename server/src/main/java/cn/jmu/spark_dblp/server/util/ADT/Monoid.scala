package cn.jmu.spark_dblp.server.util.ADT

import cn.jmu.spark_dblp.server.entity.OnlyDoc

trait Monoid[A] {
  def zero: A

  def op(a1: A, a2: A): A

  def equal(a1: A, a2: A): Boolean
}

object Monoid {
  /*
  def RSQLMonoid[A]: Monoid[List[RSQL[A]]] = new Monoid[List[RSQL[A]]] {
    override def zero: List[RSQL[A]] = List(RSQL("", classOf[OnlyDoc]))

    override def op(a1: List[RSQL[A]], a2: List[RSQL[A]]): List[RSQL[A]] =
      a1 ++ a2

    override def equal(a1: List[RSQL[A]], a2: List[RSQL[A]]): Boolean = {
      def compare = (o1: RSQL[A], o2: RSQL[A]) => {
        o1.rsql > o2.rsql
      }

      a1.sortWith(compare) equals a2.sortWith(compare)
    }
  }
*/
  def RSQLMonoid2: Monoid[List[String]] = new Monoid[List[String]] {
    override def zero: List[String] = scala.collection.immutable.Nil

    override def op(a1: List[String], a2: List[String]): List[String] = a1 ++ a2 distinct

    override def equal(a1: List[String], a2: List[String]): Boolean = {
      def lt = (o1: String, o2: String) => {
        o1 < o2
      }

      a1.sortWith(lt) equals a2.sortWith(lt)
    }
  }

  /* def listMonoid[A]: Monoid[List[A]] = new Monoid[List[A]] {
     def op(a1: List[A], a2: List[A]): List[A] = a1 ++ a2

     val zero: List[Nothing] = scala.collection.immutable.List.empty

     override def equal(a1: List[A], a2: List[A]): Boolean = ???
   }*/

}

