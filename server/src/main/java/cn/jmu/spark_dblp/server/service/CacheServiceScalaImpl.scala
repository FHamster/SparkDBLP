package cn.jmu.spark_dblp.server.service

import cn.jmu.spark_dblp.server.entity.OnlyDoc
import cn.jmu.spark_dblp.server.util.ADT.RSQLFilter
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.stereotype.Service

import java.time.Duration
import java.util
import java.util.stream.Collectors
import scala.annotation.tailrec


@Service
class CacheServiceScalaImpl extends CacheService {
  /**
   * 通过RSQL的上下文环境获取结果集
   *
   * @param RSQLContext RSQL上下文环境
   * @return 根据RSQL上下文环境返回的结果集
   */
  def getOnlyDocListCache(RSQLContext: util.List[String]): util.List[OnlyDoc] = {
    // 缓存格式
    // 字典序context=>结果集
    /**
     *
     * @param context       当前的查询上下文
     * @param contextBuffer 查询上下文的暂存处
     * @return 根据上下文环境返回的结果集
     */
    @tailrec
    def get(context: RSQLFilter[OnlyDoc], contextBuffer: RSQLFilter[OnlyDoc]): util.List[OnlyDoc] = {
      //将timeline顺序的rsql序列序列化以后 parse

      //为了保证上下文环境序列在映射缓存结果集的可交换性
      //这里要求使用字典序的字符串形式（关键是顺序要统一）
      //因为如果把集合作为key映射的时候由于集合无序性一个value会被多个key映射
      //会很麻烦
      val result: util.List[OnlyDoc] =
      if (context.nonEmpty) {
        //上下文环境非空的时候
        //尝试查缓存获取结果集
        soRedisTemplate
          .opsForValue()
          .get(context.toCacheKey)
          .asInstanceOf[util.List[OnlyDoc]]
      } else {
        //此时上下文结果为空
        null
      }


      if (result != null) {
        //此时缓存命中
        //根据缓存和buffer进行过滤
        val restContext = context * contextBuffer.reverse
        val temp: util.List[OnlyDoc] = result.parallelStream()
          .filter(restContext toStreamPredicate)
          .collect(Collectors.toList[OnlyDoc])

        //加入缓存
        soRedisTemplate.opsForValue().set(
          restContext.toCacheKey,
          temp
        )
        temp
      } else if (context.isEmpty) {
        //context没有断言的时候查MongoDB
        val restContext = context * contextBuffer.reverse
        //MongoDB的操作抽象
        val c: Criteria = restContext.toMongo

        val temp = mongoOps.find(query(c), classOf[OnlyDoc])

        //存入缓存
        soRedisTemplate.opsForValue().set(
          restContext toCacheKey,
          temp
        )
        temp
      } else {
        //context中至少一个断言时递归查找
        //传入删去尾部的context参数
        //buffer则需要暂存被删去的尾部
        get(
          context.take(context.size - 1),
          contextBuffer * context.last
        )
      }
    }
    //递归开始
    get(
      RSQLFilter(classOf[OnlyDoc], toScalaList(RSQLContext)),
      RSQLFilter.apply(classOf[OnlyDoc])
    )
  }

  def pushContext(key: String, context: util.List[String]): Unit = {
    soRedisTemplate.opsForValue.set(generateContextKey(key), context)
    template.expire(key, Duration.ofHours(2))
  }

  /**
   * @param key 作为获取
   * @return 当缓存中不存在key值时候返回null
   */
  def getContext(key: String): util.List[String] =
    soRedisTemplate.opsForValue.get(generateContextKey(key)).asInstanceOf[util.List[String]]

  def deleteContext(key: String): Unit = soRedisTemplate.delete(generateContextKey(key))

  def generateContextKey(key: String): String =
    "context:" + key

  //转换工具
  def toScalaList[A]: util.List[A] => List[A] = scala
    .collection
    .JavaConverters
    .collectionAsScalaIterableConverter(_)
    .asScala
    .toList

  //转换工具
  def toJavaList[A]: List[A] => util.List[A] = l => {
    scala
      .collection
      .JavaConverters
      .bufferAsJavaList(l.toBuffer)
  }

}
