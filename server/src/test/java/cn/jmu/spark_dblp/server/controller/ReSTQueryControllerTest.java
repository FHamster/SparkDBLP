package cn.jmu.spark_dblp.server.controller;

import com.alibaba.fastjson.JSON;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.Collections;

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
    void PostReSTQuery() throws Exception {

        String content = JSON.toJSONString(Collections.singletonList("title=re=hadoop"));
        mockMvc.perform(
                post("/onlyDocs/restquery")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.contextID").isString())
                .andExpect(jsonPath("$.context").isArray())
                .andExpect(jsonPath("$.onlyDocList").doesNotExist())
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void PatchReSTQuery() throws Exception {
        String content1 = JSON.toJSONString(Collections.singletonList("title=re=hadoop"));
        String content2 = JSON.toJSONString(Collections.singletonList("year>2010"));
        MvcResult mvcResult = mockMvc.perform(
                post("/onlyDocs/restquery")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content1))
                .andReturn();

        String uri = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$._links.self.href");


        mockMvc.perform(patch(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content2)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contextID").isString())
                .andExpect(jsonPath("$.context").isArray())
                .andExpect(jsonPath("$.onlyDocList").doesNotExist())
                .andExpect(jsonPath("$._links.self.href").exists());

    }

    @Test
    void PutReSTQuery() throws Exception {
        String content1 = JSON.toJSONString(Collections.singletonList("title=re=hadoop"));
        String content2 = JSON.toJSONString(Arrays.asList("title=re=spark", "year<2020", "year>2017"));
        MvcResult mvcResult = mockMvc.perform(
                post("/onlyDocs/restquery")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content1))
                .andReturn();

        String uri = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$._links.self.href");


        mockMvc.perform(put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content2)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contextID").isString())
                .andExpect(jsonPath("$.context").isArray())
                .andExpect(jsonPath("$.onlyDocList").doesNotExist())
                .andExpect(jsonPath("$._links.self.href").exists());

    }


    @Test
    void getQueryResult() throws Exception {
        String content1 = JSON.toJSONString(Collections.singletonList("title=re=hadoop"));
        String content2 = JSON.toJSONString(Arrays.asList("title=re=spark", "year==2020"));
        MvcResult mvcResult = mockMvc.perform(
                post("/onlyDocs/restquery")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content1))
                .andReturn();

        String uri = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$._links.self.href");


        mockMvc.perform(put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content2)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contextID").isString())
                .andExpect(jsonPath("$.context").isArray())
                .andExpect(jsonPath("$.onlyDocList").doesNotExist())
                .andExpect(jsonPath("$._links.self.href").exists());


        mockMvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.contextID").isString())
                .andExpect(jsonPath("$.context").isArray())
                .andExpect(jsonPath("$.onlyDocList").exists())
                .andExpect(jsonPath("$._links.self.href").exists());
    }

}