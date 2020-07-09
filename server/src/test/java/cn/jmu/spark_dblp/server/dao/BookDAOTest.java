package cn.jmu.spark_dblp.server.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookDAOTest {
    @Autowired
    BookDAO dao;

    @Test
    public void findAuthorByVALUE() {
//        dao.findAllByTitleContaining("Hadoop")
//                .limit(100)
//                .forEach(System.out::println);
    }
}