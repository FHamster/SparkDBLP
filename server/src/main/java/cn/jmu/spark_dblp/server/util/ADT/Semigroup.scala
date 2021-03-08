package cn.jmu.spark_dblp.server.util.ADT

trait Semigroup[A] {
  def op(a1: Semigroup[A], a2: Semigroup[A]): Semigroup[A]

  def equal(a1: Semigroup[A], a2: Semigroup[A]): Boolean
}
