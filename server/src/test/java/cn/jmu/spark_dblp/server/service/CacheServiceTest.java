package cn.jmu.spark_dblp.server.service;

import cn.jmu.spark_dblp.server.dao.OnlyDocDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class CacheServiceTest {

    @Qualifier("cacheService")
    @Autowired
    CacheService cache;
    @Autowired
    OnlyDocDAO dao;

    @Test
    void recursiveSearchTest() {
        SetOperations<String, String> so = cache.template.opsForSet();
        so.add("hadoop", "year>2019");
        System.out.println(so.members("hadoop"));
//        Assertions.assertEquals(1, so.add("b", "year>2019"));
//        System.out.println(so.difference("hadoop", "a"));
//        Assertions.assertEquals(1, so.difference("hadoop", "a").size());
//        so.add("b", "title=re=^Ge");
//        Assertions.assertEquals(0, so.difference("hadoop", "a").size());
    }


}