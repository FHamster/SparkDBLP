package cn.jmu.spark_dblp.server.service

import cn.jmu.spark_dblp.server.entity.OnlyDoc
import com.github.rutledgepaulv.qbuilders.visitors.PredicateVisitor
import com.github.rutledgepaulv.rqe.pipes.QueryConversionPipeline
import org.springframework.data.redis.core.{ListOperations, SetOperations}
import org.springframework.stereotype.Service

import java.util
import java.util.stream.Stream
import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer


@Service
class CacheServiceScalaImpl extends CacheService {
  val pipeline: QueryConversionPipeline = QueryConversionPipeline.defaultPipeline

  def push(md5: String, title: String): Unit = {
    val list: util.List[OnlyDoc] = dao.findAllByTextReturnListJPA(title)
//    redisTemplate.opsForList leftPushAll(md5, list)
    recursivePush(md5, Iterator(title))
  }

  def push(md5: String, predicates: util.List[String]): Unit = {
    val l: Iterator[String] = scala.collection.JavaConverters
      .collectionAsScalaIterable(predicates)
      .toIterator
    recursivePush(md5, l)
  }

  @tailrec
  final def recursivePush(key: String, predicates: Iterator[String])
  : (String, Iterator[String]) = {
    println(predicates.hasNext)
    if (predicates.hasNext) {
      template.opsForSet add(key, predicates.next)
      recursivePush(key, predicates)
    } else {
      (key, predicates)
    }

  }


  def getOnlyDocListCache(md5: String)
  : (util.List[OnlyDoc], util.List[String]) = {
    (new util.ArrayList[OnlyDoc], new util.ArrayList[String])
  }

  @tailrec
  final def recursiveSearch(docs: Stream[OnlyDoc], predicates: util.List[String])
  : (Stream[OnlyDoc], util.List[String]) = {
    if (predicates.isEmpty) {
      (docs, predicates)
    }
    else {
      val stringP = predicates.get(0)
      predicates.remove(0)
      val condition = pipeline.apply(stringP, classOf[OnlyDoc])
      val predicate = condition.query(new PredicateVisitor[OnlyDoc])
      recursiveSearch(docs.filter(predicate), predicates)
    }
  }
}
