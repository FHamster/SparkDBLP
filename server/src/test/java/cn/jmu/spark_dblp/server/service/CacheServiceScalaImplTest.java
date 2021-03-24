package cn.jmu.spark_dblp.server.service;

import cn.jmu.spark_dblp.server.dao.WordCountDAO;
import cn.jmu.spark_dblp.server.entity.Authors;
import cn.jmu.spark_dblp.server.entity.OnlyDoc;
import cn.jmu.spark_dblp.server.entity.WordCount;
import cn.jmu.spark_dblp.server.entity.sub.Author;
import cn.jmu.spark_dblp.server.rsqlcache.ConditionGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CacheServiceScalaImplTest {

    @Autowired
    CacheServiceScalaImpl cache;
    @Autowired
    WordCountDAO wcDao;

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
        l.add(Arrays.asList("year>2015", "type_xml==inproceedings", "title=re=spark"));


        l.forEach(it -> {
            long startTime = System.currentTimeMillis();
            System.out.println("size = " + cache.getOnlyDocListCache(it).size());
            long endTime = System.currentTimeMillis();
            System.out.println(Arrays.toString(it.toArray()));
            System.out.println(Duration.ofMillis(endTime - startTime).toString());
        });
        cache.getOnlyDocListCache(Arrays.asList("title=re=spark", "type_xml==inproceedings", "year>2015"))
                .forEach(System.out::println);
    }

    @Test
    void LargeScaleTest() {
        Random random = new Random();
/*        ConditionGenerator yearGenerator = new ConditionGenerator(
                "year",
                Arrays.asList("<=", "=", "=>"),
                Arrays.asList("2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018")
        );*/

        //=================================================================================

        List<String> RSQLList = new ArrayList<>();
        List<OnlyDoc> onlyDocList;
        List<Integer> statisticRSQLListLength = new LinkedList<>();
        List<String> keyWordList = wcDao.findAll().stream()
                .sorted((o1, o2) -> Math.toIntExact(o2.getCount() - o1.getCount()))
                .map(WordCount::getWord)
                .skip(1000)
                .limit(100)
                .collect(Collectors.toList());
        for (String it : keyWordList) {
            for (int i = 0; i < random.nextInt(8); i++) {
                try {
                    //添加title字段
                    RSQLList.add("title=re=" + it);
                    RSQLList.toArray();
                    long startTime = System.currentTimeMillis();
                    onlyDocList = cache.getOnlyDocListCache(RSQLList);
                    long endTime = System.currentTimeMillis();
                    System.out.println(Duration.ofMillis(endTime - startTime).toString());

                    //添加prefix1
                    String prefix1 = onlyDocList.get(random.nextInt(onlyDocList.size())).getPrefix1();
                    RSQLList.add("prefix1==" + prefix1);
                    onlyDocList = cache.getOnlyDocListCache(RSQLList);
                    if (onlyDocList.size() <= 1) throw new Exception();

                    //添加type
                    String type = onlyDocList.get(random.nextInt(onlyDocList.size())).getType();
                    RSQLList.add(String.format("type==\"%s\"", type));
                    onlyDocList = cache.getOnlyDocListCache(RSQLList);
                    if (onlyDocList.size() <= 1) throw new Exception();

                    //添加year1字段 下限
                    String year1 = onlyDocList.get(random.nextInt(onlyDocList.size())).getYear().toString();
                    RSQLList.add("year>=" + year1);
                    onlyDocList = cache.getOnlyDocListCache(RSQLList);
                    if (onlyDocList.size() <= 1) throw new Exception();

                    //添加year1字段 上限
                    String year2 = onlyDocList.get(random.nextInt(onlyDocList.size())).getYear().toString();
                    RSQLList.add("year<=" + year2);
                    onlyDocList = cache.getOnlyDocListCache(RSQLList);
                    if (onlyDocList.size() <= 1) throw new Exception();

                    //添加prefix2
                    String prefix2 = onlyDocList.get(random.nextInt(onlyDocList.size())).getPrefix2();
                    RSQLList.add("prefix2==" + prefix2);
                    onlyDocList = cache.getOnlyDocListCache(RSQLList);
                    if (onlyDocList.size() <= 1) throw new Exception();

                    //添加author
                    List<Author> authorList;
                    authorList = onlyDocList.get(random.nextInt(onlyDocList.size())).getAuthorOption().orElse(new ArrayList<>());
                    RSQLList.add(String.format("author._VALUE==\"%s\"", authorList.get(random.nextInt(authorList.size())).get_VALUE()));
                    onlyDocList = cache.getOnlyDocListCache(RSQLList);
                    if (onlyDocList.size() <= 1) throw new Exception();

                    authorList = onlyDocList.get(random.nextInt(onlyDocList.size())).getAuthorOption().orElse(new ArrayList<>());
                    RSQLList.add(String.format("author._VALUE==\"%s\"", authorList.get(random.nextInt(authorList.size())).get_VALUE()));
                    onlyDocList = cache.getOnlyDocListCache(RSQLList);
                    if (onlyDocList.size() <= 1) throw new Exception();

                    authorList = onlyDocList.get(random.nextInt(onlyDocList.size())).getAuthorOption().orElse(new ArrayList<>());
                    RSQLList.add(String.format("author._VALUE==\"%s\"", authorList.get(random.nextInt(authorList.size())).get_VALUE()));
                    onlyDocList = cache.getOnlyDocListCache(RSQLList);
                    if (onlyDocList.size() <= 1) throw new Exception();


//                System.out.println(i);
//                System.out.println(RSQLList);


                } catch (Exception ignored) {
                    statisticRSQLListLength.add(RSQLList.size());
                    System.err.println(RSQLList.size());
                }
                RSQLList = new ArrayList<>();
            }
        }
        double avg = statisticRSQLListLength.stream().mapToInt(Integer::intValue).average().getAsDouble();
        System.out.println(avg);
/*        l.forEach(it -> {
            long startTime = System.currentTimeMillis();
            System.out.println("size = " + cache.getOnlyDocListCache(it).size());
            long endTime = System.currentTimeMillis();
            System.out.println(Arrays.toString(it.toArray()));
            System.out.println(Duration.ofMillis(endTime - startTime).toString());
        });*/
    }

    @Test
    void test() {
        wcDao.findAll().stream()
                .sorted((o1, o2) -> Math.toIntExact(o2.getCount() - o1.getCount()))
                .forEach(System.out::println);
    }
}