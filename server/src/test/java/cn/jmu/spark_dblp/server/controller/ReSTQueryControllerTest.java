package cn.jmu.spark_dblp.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class ReSTQueryControllerTest {
    @Autowired
    MockMvc mockMvc;

//    @Autowired
//    Properties properties
    @BeforeEach
    @Disabled
    void before() {
        System.out.println("Test Start");
    }

    @AfterEach
    @Disabled
    void after() {
        System.out.println("Test end");
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
                .andReturn();

        ObjectMapper mapper = Jackson2ObjectMapperBuilder.json().build();
        String uuid = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.uuid");
        System.out.println("=================getQueryResut=================");
        mockMvc.perform(get("/onlyDocs/restquery/{queryid}", uuid))
                .andDo(print())
                .andExpect(jsonPath("$").isArray());
    }
}