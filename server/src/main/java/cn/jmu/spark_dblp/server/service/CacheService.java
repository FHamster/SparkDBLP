package cn.jmu.spark_dblp.server.service;


import cn.jmu.spark_dblp.server.dao.OnlyDocDAO;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @CachePut(cacheNames = "context", key = "#md5")
    public List<String> pushContext(String md5, List<String> context) {
//        soRedisTemplate.opsForValue().set(md5, context);
//        template.expire(md5, Duration.ofHours(2));
        return context;
    }

    @Cacheable(cacheNames = "context", key = "#md5")
    public List<String> getContext(String md5) {
        return new ArrayList<>();
    }

    //    @Autowired
    MongoOperations mongoOps = new MongoTemplate(
            new SimpleMongoClientDatabaseFactory(MongoClients.create(),
                    "SparkDBLPTest")
    );


}