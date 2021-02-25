package cn.jmu.spark_dblp.server.service

import cn.jmu.spark_dblp.server.entity.OnlyDoc
import cn.jmu.spark_dblp.server.util.{ConditionUtil, InsensitiveMongoVisitor, InsensitivePredicateVisitor}
import com.github.rutledgepaulv.qbuilders.builders.GeneralQueryBuilder
import com.github.rutledgepaulv.qbuilders.conditions.Condition
import com.github.rutledgepaulv.rqe.pipes.QueryConversionPipeline
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.stereotype.Service

import java.util
import java.util.stream.Collectors
import scala.annotation.tailrec


@Service
class CacheServiceScalaImpl extends CacheService {
  val pipeline: QueryConversionPipeline = QueryConversionPipeline.defaultPipeline

  /*  def push(predicates: util.List[String]): Unit = {
      val l: Iterator[String] = scala.collection.JavaConverters
        .collectionAsScalaIterable(predicates)
        .toIterator
      //    recursivePush(md5, l)
    }*/
  /*

    @nowarn
    def push(md5: String, title: String): Unit = {
      val list: util.List[OnlyDoc] = dao.findAllByTextReturnListJPA(title)
      //    redisTemplate.opsForList leftPushAll(md5, list)
      recursivePush(md5, Iterator(title))
    }
  */


  /* @tailrec
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

 */
  /**
   * 获取缓存，或者查库
   *
   * @param context
   * @return
   */
  def getOnlyDocListCache(RSQLContext: util.List[String]): util.List[OnlyDoc] = {
    // 缓存格式
    // 字典序context=>结果集
    @tailrec
    def get(context: List[String], contextBuffer: List[String]): util.List[OnlyDoc] = {
      //将timeline顺序的rsql序列序列化以后 parse
      //      val condition = parseCondition(context)

      //这里要求使用字典序的字符串形式
      val result: util.List[OnlyDoc] =
        if (context.nonEmpty) {
          soRedisTemplate
            .opsForValue()
            .get(ConditionUtil.Condition2LexOrderString(toJavaList(context)))
            .asInstanceOf[util.List[OnlyDoc]]
        }
        else null


      if (result != null) {
        //此时缓存命中
        // 还要实现根据缓存和buffer进行过滤
        val restContext = parseCondition(context ++ contextBuffer.reverse)
        val temp: util.List[OnlyDoc] = result.parallelStream()
          .filter(restContext.query(new InsensitivePredicateVisitor[OnlyDoc]()))
          .collect(Collectors.toList[OnlyDoc])

        // 加入缓存

        val cacheKey: String = ConditionUtil
          .Condition2LexOrderString(
            toJavaList(context ++ contextBuffer.reverse)
          )
        soRedisTemplate.opsForValue().set(cacheKey, temp)
        temp
      } else if (context.isEmpty) {
        //context没有断言的时候查库
        // MongoDB搜索

        val c: Criteria = parseCondition(contextBuffer)
          .query(new InsensitiveMongoVisitor)

        val temp = mongoOps.find(query(c), classOf[OnlyDoc])
        val cacheKey: String = ConditionUtil
          .Condition2LexOrderString(
            toJavaList(context ++ contextBuffer.reverse)
          )
        soRedisTemplate.opsForValue().set(cacheKey, temp)
        temp
      } else {
        //context中至少一个断言时递归查找
        get(
          context.take(context.size - 1),
          contextBuffer :+ context.last
        )
      }
    }

    //转换工具
    def toScalaList[A]: util.List[A] => List[A] = scala
      .collection
      .JavaConverters
      .collectionAsScalaIterableConverter(_)
      .asScala
      .toList

    //转换工具
    def toJavaList[A]: List[A] => util.List[A] =
      l => {
        scala
          .collection
          .JavaConverters
          .bufferAsJavaList(l.toBuffer)
      }


    //断言语法分析工具
    def parseCondition: List[String] => Condition[GeneralQueryBuilder] = (l: List[String]) => pipeline
      .apply(
        toJavaList(l)
          .stream
          .collect(Collectors.joining(";")),
        classOf[OnlyDoc]
      )

    get(toScalaList(RSQLContext), List.empty[String])
  }

  /*
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
    }*/
}
