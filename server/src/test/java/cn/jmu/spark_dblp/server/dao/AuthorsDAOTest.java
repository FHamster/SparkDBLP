package cn.jmu.spark_dblp.server.dao;

import cn.jmu.spark_dblp.server.entity.Authors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthorsDAOTest {

    @Autowired
    AuthorsDAO dao;


    @Test
    public void findAuthorByVALUE() {
//        List<Author> list = new ArrayList<>();
//        list.add(new Author("Chen"));
//        Stream<Authors> oa = dao.findAllBy_VALUEContaining("Chen");
//        Assertions.assertTrue(oa.isPresent());
//        oa.forEach(System.out::println);

    }
}