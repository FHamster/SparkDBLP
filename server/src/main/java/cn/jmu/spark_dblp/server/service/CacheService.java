package cn.jmu.spark_dblp.server.service;


import cn.jmu.spark_dblp.server.dao.OnlyDocDAO;
import cn.jmu.spark_dblp.server.entity.OnlyDoc;
import cn.jmu.spark_dblp.server.util.ADT.RSQLFilter;
import com.github.rutledgepaulv.rqe.pipes.QueryConversionPipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 这个Service是为了实现对关键字的搜索结果进行缓存并将数据过滤工作由数据库上推至服务器层面
 */
@Service
public abstract class CacheService {
    @Autowired
    OnlyDocDAO dao;
    @Autowired
    public StringRedisTemplate template;

    @Autowired
    @Qualifier("redisTemplate")
    public RedisTemplate<Object, Object> RedisTemplate;

    @Autowired
    @Qualifier("soRedisTemplate")
    public RedisTemplate<String, Object> soRedisTemplate;

    @Autowired
//    @Qualifier("SparkDBLPMongoOperations")
    MongoOperations mongoOps;

    @Autowired
    QueryConversionPipeline pipeline;

    abstract List<OnlyDoc> getOnlyDocListCache(List<String> RSQLContext);
    abstract void writeCache(RSQLFilter<OnlyDoc> context, List<OnlyDoc> list);
    abstract List<OnlyDoc> readCache(RSQLFilter<OnlyDoc> context);
    abstract List<OnlyDoc> readDB(RSQLFilter<OnlyDoc> context);
}