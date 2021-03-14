package cn.jmu.spark_dblp.server.service;

import cn.jmu.spark_dblp.server.dao.OnlyDocDAO;
import cn.jmu.spark_dblp.server.entity.OnlyDoc;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class PlayWithRedis {

    @Autowired
    CacheServiceScalaImpl cache;
    @Autowired
    OnlyDocDAO dao;

    SetOperations<String, String> so;
    String sparksql = "spark sql";
    String a = "a";
    String b = "b";

    @BeforeEach
    void before() {
        System.out.println("=================Test Start=================");

        so = cache.template.opsForSet();


    }

    @AfterEach
    void after() {
    }

    @Test
    void set() {
        so.add(sparksql, "year>2019", "title=re=hadoop");
        so.add(a, "year>2019");
        so.add(b, "year>2019", "title=re=hadoop");

        System.out.println(so.members(sparksql));
        Assertions.assertEquals(2, so.members(sparksql).size());
        Assertions.assertEquals(1, so.members(a).size());
        Assertions.assertEquals(2, so.members(b).size());

        Assertions.assertEquals(1, so.difference(sparksql, a).size());
        Assertions.assertEquals(1, so.intersect(sparksql, a).size());
        Assertions.assertEquals(2, so.union(sparksql, a).size());


        so.getOperations().delete(sparksql);
        so.getOperations().delete(a);
        so.getOperations().delete(b);
        System.out.println("=================Test end=================");
    }

    /**
     * 生存时间测试
     */
    @Test
    void expire() {
        so.add("expireTest", "year>2019", "title=re=hadoop");
        cache.template.expire("expireTest", Duration.ofSeconds(3));

        Assertions.assertTrue(cache.template.hasKey("expireTest"));
        try {
            Thread.sleep(Duration.ofSeconds(3).toMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assertions.assertFalse(cache.template.hasKey("expireTest"));
    }

    @Test
    void pushOnlyDocs() {
        List<OnlyDoc> list = dao.findAllByText(sparksql);
        System.out.println(list.size());
        cache.soRedisTemplate.opsForValue().set(sparksql, list);
        cache.soRedisTemplate.expire(sparksql, Duration.ofSeconds(10));
        List<OnlyDoc> list2 = (List<OnlyDoc>) cache.soRedisTemplate.opsForValue().get(sparksql);
        Assertions.assertEquals(
                list.size(),
                list2.size()
        );
        list2.forEach(System.out::println);
    }


}