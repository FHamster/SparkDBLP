package cn.jmu.spark_dblp.server.service;

import cn.jmu.spark_dblp.server.dao.OnlyDocDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class CacheServiceScalaImplTest {

    @Autowired
    CacheServiceScalaImpl cache;
    @Autowired
    OnlyDocDAO dao;

    String title = "hadoop";

    @Test
    void recursiveSearchTest() {
//        Stream<OnlyDoc> onlyDocStream = dao.findAllByTextReturnListJPA("hadoop").stream();
//        List<String> p = new ArrayList<>();
//        p.add("year>2010");
//        p.add("year<2015");
//        cache.recursiveSearch(onlyDocStream, p)._1.forEach(System.out::println);
    }

    SetOperations<String, String> so;
    String hadoop = "hadoop";
    String a = "a";
    String b = "b";
    String md5 = DigestUtils.md5DigestAsHex(title.getBytes(StandardCharsets.UTF_8));
    @BeforeEach
    void before() {
        System.out.println("=================Test Start=================");

        so = cache.template.opsForSet();

        so.add(hadoop, "year>2019", "title=re=hadoop");
        so.add(a, "year>2019");
        so.add(b, "year>2019", "title=re=hadoop");
    }

    @AfterEach
    void after() {
        so.getOperations().delete(hadoop);
        so.getOperations().delete(a);
        so.getOperations().delete(b);
        so.getOperations().delete(md5);
        System.out.println("=================Test end=================");
    }

    @Test
    void pushTest() {
        cache.push(md5, title);
    }

    @Test
    void pushMoreTest() {
        List<String> p = new ArrayList<>();
        p.add("year>2010");
        p.add("year<2015");

        cache.push(md5, p);
    }

}