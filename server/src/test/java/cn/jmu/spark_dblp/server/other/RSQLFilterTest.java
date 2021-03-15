package cn.jmu.spark_dblp.server.other;

import cn.jmu.spark_dblp.server.dao.OnlyDocDAO;
import cn.jmu.spark_dblp.server.entity.OnlyDoc;
import com.github.rutledgepaulv.qbuilders.builders.GeneralQueryBuilder;
import com.github.rutledgepaulv.qbuilders.conditions.Condition;
import com.github.rutledgepaulv.qbuilders.visitors.MongoVisitor;
import com.github.rutledgepaulv.qbuilders.visitors.PredicateVisitor;
import com.github.rutledgepaulv.rqe.pipes.QueryConversionPipeline;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class RSQLFilterTest {

    QueryConversionPipeline pipeline = QueryConversionPipeline.defaultPipeline();
    @Autowired
    OnlyDocDAO dao;
//    @Autowired
//    MockMvc mockMvc;

    @BeforeEach
    void before() {
        System.out.println("=================Test Start=================");
    }

    @AfterEach
    void after() {
        System.out.println("=================Test end=================");
    }

    @Test
    void offlineQuery() {
        Condition<GeneralQueryBuilder> condition = pipeline.apply("year>2015", OnlyDoc.class);
        Predicate<OnlyDoc> predicate = condition.query(new PredicateVisitor<>());
        List<OnlyDoc> docs = dao.findAllByText("Hadoop");

        List<OnlyDoc> personsNamedPaulAndAge23 = docs.stream().filter(predicate).collect(toList());
        personsNamedPaulAndAge23.forEach(System.out::println);
    }


    @Test
    void getQueryResult() {
        Condition<GeneralQueryBuilder> condition = pipeline.apply("year>2015", OnlyDoc.class);
    }

    @Test
    void some() {
        //        Condition<PersonQuery> query = firstName().eq("Paul").or(and(firstName().ne("Richard"), age().gt(22)));
//        Condition<PersonQuery> q = new PersonQuery().firstName().pattern("Pa*").and().age().eq(23);
//        Condition<OnlyDoc> q = new PersonQuery().firstName().pattern("/pa*/i");
//        Condition<PersonQuery> q = new PersonQuery().
//        System.out.println(q.query(new RSQLVisitor()));
        Condition<GeneralQueryBuilder> condition = pipeline.apply("title=re=/hadoop/i", OnlyDoc.class);
        Criteria query = condition.query(new MongoVisitor());
        System.out.println(query.getCriteriaObject().toJson());
        Predicate<OnlyDoc> predicate = condition.query(new PredicateVisitor<>());

    }
    @Test
    void testArrayQuery() {
        //        Condition<PersonQuery> query = firstName().eq("Paul").or(and(firstName().ne("Richard"), age().gt(22)));
//        Condition<PersonQuery> q = new PersonQuery().firstName().pattern("Pa*").and().age().eq(23);
//        Condition<OnlyDoc> q = new PersonQuery().firstName().pattern("/pa*/i");
//        Condition<PersonQuery> q = new PersonQuery().
//        System.out.println(q.query(new RSQLVisitor()));
        Condition<GeneralQueryBuilder> condition = pipeline.apply("author._VALUE=re=abel", OnlyDoc.class);
        Criteria query = condition.query(new MongoVisitor());
        System.out.println(query.getCriteriaObject().toJson());
        Predicate<OnlyDoc> predicate = condition.query(new PredicateVisitor<>());

    }
}