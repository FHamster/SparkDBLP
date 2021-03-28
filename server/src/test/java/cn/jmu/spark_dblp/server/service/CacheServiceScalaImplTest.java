package cn.jmu.spark_dblp.server.service;

import cn.jmu.spark_dblp.server.dao.CacheTestRecordDAO;
import cn.jmu.spark_dblp.server.dao.WordCountDAO;
import cn.jmu.spark_dblp.server.entity.CacheTestRecord;
import cn.jmu.spark_dblp.server.entity.OnlyDoc;
import cn.jmu.spark_dblp.server.entity.WordCount;
import cn.jmu.spark_dblp.server.entity.sub.Author;
import cn.jmu.spark_dblp.server.rsqlcache.TimingMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CacheServiceScalaImplTest {

    @Autowired
//    CacheServiceScalaPlainImpl cache;
    CacheServiceScalaImpl cache;
    @Autowired
    WordCountDAO wcDao;
    @Autowired
    CacheTestRecordDAO ctDAO;

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
        List<String> RSQLList = new ArrayList<>();
        List<OnlyDoc> onlyDocList;
        List<Integer> statisticRSQLListLength = new LinkedList<>();
        List<String> keyWordList = wcDao.findAll().stream()
                .sorted((o1, o2) -> Math.toIntExact(o2.getCount() - o1.getCount()))
                .map(WordCount::getWord)
                .skip(2000)
                .limit(100)
                .collect(Collectors.toList());

        Function<List<String>, List<OnlyDoc>> c = (list) -> cache.getOnlyDocListCache(list);
        TimingMap<List<String>, List<OnlyDoc>> t1 = new TimingMap<>(c);
        for (String it : keyWordList) {
            int repeat = 10;
            for (int i = 0; i < repeat; i++) {
                try {
                    //添加title字段
                    RSQLList.add("title=re=" + it);
                    onlyDocList = t1.accept(RSQLList);
                    System.out.printf("%s,返回结果集大小%d\n", Duration.ofMillis(t1.get()).toString(), onlyDocList.size());
                    if (onlyDocList.size() <= 1) throw new Exception();
                    ctDAO.save(new CacheTestRecord(it, RSQLList.toString(), RSQLList.size(), i, true, t1.get()));

                    //添加prefix1
                    String prefix1 = onlyDocList.get(random.nextInt(onlyDocList.size())).getPrefix1();
                    RSQLList.add("prefix1==" + prefix1);
                    onlyDocList = t1.accept(RSQLList);
                    System.out.printf("%s,返回结果集大小%d\n", Duration.ofMillis(t1.get()).toString(), onlyDocList.size());
                    if (onlyDocList.size() <= 1) throw new Exception();
                    ctDAO.save(new CacheTestRecord(it, RSQLList.toString(), RSQLList.size(), i, true, t1.get()));

                    //添加type
                    String type = onlyDocList.get(random.nextInt(onlyDocList.size())).getType();
                    RSQLList.add(String.format("type==\"%s\"", type));
                    onlyDocList = t1.accept(RSQLList);
                    System.out.printf("%s,返回结果集大小%d\n", Duration.ofMillis(t1.get()).toString(), onlyDocList.size());
                    if (onlyDocList.size() <= 1) throw new Exception();
                    ctDAO.save(new CacheTestRecord(it, RSQLList.toString(), RSQLList.size(), i, true, t1.get()));

                    //添加year1字段 下限
                    String year1 = onlyDocList.get(random.nextInt(onlyDocList.size())).getYear().toString();
                    RSQLList.add("year>=" + year1);
                    onlyDocList = t1.accept(RSQLList);
                    System.out.printf("%s,返回结果集大小%d\n", Duration.ofMillis(t1.get()).toString(), onlyDocList.size());
                    if (onlyDocList.size() <= 1) throw new Exception();
                    ctDAO.save(new CacheTestRecord(it, RSQLList.toString(), RSQLList.size(), i, true, t1.get()));

                    //添加year1字段 上限
                    String year2 = onlyDocList.get(random.nextInt(onlyDocList.size())).getYear().toString();
                    RSQLList.add("year<=" + year2);
                    onlyDocList = t1.accept(RSQLList);
                    System.out.printf("%s,返回结果集大小%d\n", Duration.ofMillis(t1.get()).toString(), onlyDocList.size());
                    if (onlyDocList.size() <= 1) throw new Exception();
                    ctDAO.save(new CacheTestRecord(it, RSQLList.toString(), RSQLList.size(), i, true, t1.get()));

                    //添加prefix2
                    String prefix2 = onlyDocList.get(random.nextInt(onlyDocList.size())).getPrefix2();
                    RSQLList.add("prefix2==" + prefix2);
                    onlyDocList = t1.accept(RSQLList);
                    System.out.printf("%s,返回结果集大小%d\n", Duration.ofMillis(t1.get()).toString(), onlyDocList.size());
                    if (onlyDocList.size() <= 1) throw new Exception();
                    ctDAO.save(new CacheTestRecord(it, RSQLList.toString(), RSQLList.size(), i, true, t1.get()));

                    //添加author
                    List<Author> authorList;
                    authorList = onlyDocList.get(random.nextInt(onlyDocList.size())).getAuthorOption().orElse(new ArrayList<>());
                    RSQLList.add(String.format("author._VALUE==\"%s\"", authorList.get(random.nextInt(authorList.size())).get_VALUE()));
                    onlyDocList = t1.accept(RSQLList);
                    System.out.printf("%s,返回结果集大小%d\n", Duration.ofMillis(t1.get()).toString(), onlyDocList.size());
                    if (onlyDocList.size() <= 1) throw new Exception();
                    ctDAO.save(new CacheTestRecord(it, RSQLList.toString(), RSQLList.size(), i, true, t1.get()));

                    authorList = onlyDocList.get(random.nextInt(onlyDocList.size())).getAuthorOption().orElse(new ArrayList<>());
                    RSQLList.add(String.format("author._VALUE==\"%s\"", authorList.get(random.nextInt(authorList.size())).get_VALUE()));
                    onlyDocList = t1.accept(RSQLList);
                    System.out.printf("%s,返回结果集大小%d\n", Duration.ofMillis(t1.get()).toString(), onlyDocList.size());
                    if (onlyDocList.size() <= 1) throw new Exception();
                    ctDAO.save(new CacheTestRecord(it, RSQLList.toString(), RSQLList.size(), i, true, t1.get()));

                    authorList = onlyDocList.get(random.nextInt(onlyDocList.size())).getAuthorOption().orElse(new ArrayList<>());
                    RSQLList.add(String.format("author._VALUE==\"%s\"", authorList.get(random.nextInt(authorList.size())).get_VALUE()));
                    onlyDocList = t1.accept(RSQLList);
                    System.out.printf("%s,返回结果集大小%d\n", Duration.ofMillis(t1.get()).toString(), onlyDocList.size());
                    if (onlyDocList.size() <= 1) throw new Exception();
                    ctDAO.save(new CacheTestRecord(it, RSQLList.toString(), RSQLList.size(), i, true, t1.get()));
                } catch (Exception ignored) {
//                    System.err.println("返回结果集<=1");
                }
                statisticRSQLListLength.add(RSQLList.size());
                System.out.printf("最长继承链长度%d\n", RSQLList.size());
                System.out.printf("当前的同根重复查询次数%d\n", i);
                RSQLList = new ArrayList<>();
            }
        }
        double avg = statisticRSQLListLength.stream().mapToInt(Integer::intValue).average().getAsDouble();
        System.out.println(avg);
    }

    @Test
    void test() {
        wcDao.findAll().stream()
                .sorted((o1, o2) -> Math.toIntExact(o2.getCount() - o1.getCount()))
                .forEach(System.out::println);
    }
}