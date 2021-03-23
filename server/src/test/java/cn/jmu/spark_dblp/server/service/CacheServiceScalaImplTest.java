package cn.jmu.spark_dblp.server.service;

import cn.jmu.spark_dblp.server.entity.OnlyDoc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CacheServiceScalaImplTest {

    @Autowired
    CacheServiceScalaImpl cache;
    //缓存可继承性测试
    @Test
    void pushTest() {
        List<String> p1 = new ArrayList<>();
        p1.add("title=re=hadoop");
        List<String> p2 = new ArrayList<>();
        p2.add("title=re=hadoop");
        p2.add("year>2015");

        long startTime = System.currentTimeMillis();
        List<OnlyDoc> l1 = cache.getOnlyDocListCache(p1);
//        l1.forEach(System.out::println);
        System.out.printf("如上为 title=re=hadoop 集合 size = %d \n", l1.size());

        long t1 = System.currentTimeMillis();
        List<OnlyDoc> l2 = cache.getOnlyDocListCache(p2);
//        l2.forEach(System.out::println);
        System.out.printf("如上为 title=re=hadoop;year>2015 集合 size = %d \n", l2.size());

        long t2 = System.currentTimeMillis();
        System.out.println(Duration.ofMillis(t1 - startTime).toString());
        System.out.println(Duration.ofMillis(t2 - t1).toString());
        Assertions.assertTrue(t1 - startTime > t2 - t1);
    }

    //更复杂的可继承性测试
    @Test
    void pushMoreTest() {
        List<List<String>> l = new ArrayList<>();
        l.add(Collections.singletonList("title=re=spark"));
        l.add(Arrays.asList("title=re=spark", "year>2015"));
        l.add(Arrays.asList("title=re=spark", "year>2015", "year<=2020"));
        l.add(Arrays.asList("title=re=spark", "type_xml==inproceedings"));
        l.add(Arrays.asList("title=re=spark", "year>2015", "type_xml==inproceedings"));
        l.add(Arrays.asList("title=re=spark", "type_xml==inproceedings", "year>2015"));
        l.add(Arrays.asList( "year>2015", "type_xml==inproceedings","title=re=spark"));

        l.forEach(it -> {
            long startTime = System.currentTimeMillis();
            System.out.println("size = " + cache.getOnlyDocListCache(it).size());
            long endTime = System.currentTimeMillis();
            System.out.println(Arrays.toString(it.toArray()));
            System.out.println(Duration.ofMillis(endTime - startTime).toString());
        });
    }

}