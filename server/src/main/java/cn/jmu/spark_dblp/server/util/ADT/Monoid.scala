package cn.jmu.spark_dblp.server.util.ADT

/**
 * 幺半群代数系统
 */
trait Monoid[A] {

  /**
   * 代数系统的单位元e
   * 应满足 x*e = e*x = x
   */
  def e: A

  /**
   * 代数系统的操作
   * 应该满足结合律 (xy)z=x(yz)
   */
  def *(a: A): A

  /**
   * 判断代数元素相等的方法
   *
   * @return 两个代数元素是否相等
   */
  def equal(a: A): Boolean
}