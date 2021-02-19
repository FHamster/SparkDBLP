package cn.jmu.spark_dblp.server.controller;

import cn.jmu.spark_dblp.server.entity.OnlyDoc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rutledgepaulv.qbuilders.builders.GeneralQueryBuilder;
import com.github.rutledgepaulv.qbuilders.conditions.Condition;
import com.github.rutledgepaulv.qbuilders.visitors.PredicateVisitor;
import com.github.rutledgepaulv.rqe.pipes.QueryConversionPipeline;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.UriTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.function.Predicate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class ReSTQueryControllerTest {
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
    void creatReSTQuery() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                post("/onlyDocs/restquery")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"hadoop\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").exists())
                .andExpect(jsonPath("$._links.queryHandler").exists())
                .andExpect(jsonPath("$._links.self").exists())
                .andReturn();
    }

    @Test
    void PatchReSTQuery() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                post("/onlyDocs/restquery")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"hadoop\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").exists())
                .andExpect(jsonPath("$._links.queryHandler").exists())
                .andExpect(jsonPath("$._links.self").exists())
                .andReturn();

        ObjectMapper mapper = Jackson2ObjectMapperBuilder.json().build();
        String uri = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$._links.queryHandler.href");


        MvcResult putResult = mockMvc.perform(
                patch(uri).param("rsql","year==2019;author._VALUE=re=\"^Ge*\"")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.queryHandler").exists())
                .andExpect(jsonPath("$._links.self").exists())
                .andReturn();

    }


    @Test
    void getQueryResult() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                post("/onlyDocs/restquery")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"hadoop\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").exists())
                .andExpect(jsonPath("$._links.queryHandler").exists())
                .andExpect(jsonPath("$._links.self").exists())
                .andReturn();

        ObjectMapper mapper = Jackson2ObjectMapperBuilder.json().build();
        String uri = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$._links.queryHandler.href");
        System.out.println("=================getQueryResut=================");


//        Properties values = new Properties();
//        values.put("size", 1);
//        values.put("number",1);

//        UriTemplate template = UriTemplate.of("http://localhost/onlyDocs/restquery/e34cc2c1ffdd4cf3b65dff1bffb65aac")
//        UriTemplate template = UriTemplate.of(uri);
//                .with(new TemplateVariable("size", VariableType.REQUEST_PARAM))
//                .with(new TemplateVariable("number", VariableType.REQUEST_PARAM));

//        System.out.println(UriTemplate.of(uri).expand(50, 1).toString());

        mockMvc.perform(get(UriTemplate.of(uri).expand(30, 0)))
                .andDo(print())
                .andExpect(jsonPath("$._embedded.onlyDocs").isArray());
    }
    @Test
    void PredicateEquatable()  {
        QueryConversionPipeline pipeline = QueryConversionPipeline.defaultPipeline();
        Condition<GeneralQueryBuilder> condition1 = pipeline.apply("year==2019", OnlyDoc.class);
        Condition<GeneralQueryBuilder> condition2 = pipeline.apply("year==2019", OnlyDoc.class);
        Predicate<OnlyDoc> predicate1 = condition1.query(new PredicateVisitor<>());
        Predicate<OnlyDoc> predicate2 = condition1.query(new PredicateVisitor<>());
        System.out.println(predicate1.equals(predicate2));
    }
}