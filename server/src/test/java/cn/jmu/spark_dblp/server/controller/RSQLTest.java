package cn.jmu.spark_dblp.server.controller;

import cn.jmu.spark_dblp.server.dao.OnlyDocDAO;
import cn.jmu.spark_dblp.server.entity.OnlyDoc;
import com.github.rutledgepaulv.qbuilders.builders.GeneralQueryBuilder;
import com.github.rutledgepaulv.qbuilders.conditions.Condition;
import com.github.rutledgepaulv.qbuilders.visitors.PredicateVisitor;
import com.github.rutledgepaulv.rqe.pipes.QueryConversionPipeline;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class RSQLTest {

    QueryConversionPipeline pipeline = QueryConversionPipeline.defaultPipeline();
    @Autowired
    OnlyDocDAO dao;
    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void before() {
        System.out.println("=================Test Start=================");
    }

    @AfterEach
    void after() {
        System.out.println("=================Test end=================");
    }

    @Test
    void offlineQuery() throws Exception {
        Condition<GeneralQueryBuilder> condition = pipeline.apply("year>2015", OnlyDoc.class);
        Predicate<OnlyDoc> predicate = condition.query(new PredicateVisitor<>());
        List<OnlyDoc> docs = dao.findAllByTextReturnListJPA("Hadoop");

        List<OnlyDoc> personsNamedPaulAndAge23 = docs.stream().filter(predicate).collect(toList());
        personsNamedPaulAndAge23.forEach(System.out::println);
    }


    @Test
    void getQueryResult() {

    }
}