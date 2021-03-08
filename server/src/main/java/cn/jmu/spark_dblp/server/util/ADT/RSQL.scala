package cn.jmu.spark_dblp.server.util.ADT

import com.github.rutledgepaulv.qbuilders.visitors.RSQLVisitor
import com.github.rutledgepaulv.rqe.pipes.QueryConversionPipeline

class RSQL[A](val rsql: String) {
  override def toString: String = rsql
}

object RSQL {
  def apply[A](rsql: String, c: Class[A]): RSQL[A] = {
    QueryConversionPipeline.defaultPipeline()(rsql, c).query(new RSQLVisitor())
    new RSQL[A](rsql)
  }
}
