package cn.jmu.spark_dblp.server.dao;

import cn.jmu.spark_dblp.server.entity.Article;
import cn.jmu.spark_dblp.server.entity.sub.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;


;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleDAOTest {

    @Autowired
    ArticleDAO dao;

    @Test
    public void findArticle() {
        Optional<Article> oa = dao.findById("5eedea99310f336f41bf5ca0");
//        Assertions.assertTrue(oa.isPresent());
        Article a = oa.get();
        System.out.println(a.toString());
    }

    @Test
    public void findArticleByAuthorContaining() {
        List<Author> list = new ArrayList<>();
        list.add(new Author("Chen"));
        List<Article> oa = dao.findAllByAuthorContaining(list);
//        Assertions.assertTrue(oa.isPresent());
        oa.forEach(System.out::println);

    }

    @Test
    public void findAuthorByVALUE() {
        List<Author> list = new ArrayList<>();
        list.add(new Author("Chen"));
        List<Article> oa = dao.findAllByAuthorContaining(list);
//        Assertions.assertTrue(oa.isPresent());
        oa.forEach(System.out::println);

    }

    @Test
    public void findAllByTitleContainingAndAuthor__VALUEContainingAndYearBetween() {
        List<String> list = new ArrayList<>();
        list.add("2019");
        list.add("2018");
        Integer[] a = {2015, 2016};
        Integer[] b = {2018};
        Integer[] c = {};
//        List<Article> oa = dao
//                .findAllByTitleContainingAndAuthor__VALUEContainingAndYearIn("hadoop", "Reza Akbarinia")
//                .collect(Collectors.toList());
//        Assertions.assertTrue(oa.isPresent());
//        oa.forEach(System.out::println);

    }

    @Test
    public void findAllByTitleContaining() {
        Stream<Article> oa = dao.findAllByTitleContaining("hadoop");
//        Assertions.assertTrue(oa.isPresent());
//        String.
        oa.forEach(it -> System.out.println(it));
//        String
//        String a = "";
//        System.out.println(a.isEmpty());
//        System.out.println(a.isEmpty());
    }

    @Test
    public void findAllByAuthorContaining() {
        Stream<Article> oa = dao.findAllByAuthorContaining(new Author("Joon"));

//        Assertions.assertTrue(oa.isPresent());
//        String.
        oa.forEach(System.out::println);
//        String
//        String a = "";
//        System.out.println(a.isEmpty());
//        System.out.println(a.isEmpty());
    }

    @Test
    public void find() {
        List<String> author = new ArrayList<>();
        String title = "hadoop";
//        String title = "";
//        author.add("R");
//        author.add("Rob");
        List<Pattern> p = author.stream()
                .map(it -> Pattern.compile(it, Pattern.CASE_INSENSITIVE))
                .collect(Collectors.toList());
//        Integer[] year = {2019,2018};
        List<Integer> year = new ArrayList<>();
//        year.add(2016);
//        year.add(2015);
        dao.findAllByTitleContainingAndAuthor__VALUEContainingAndYearIn(title, p, year)
                .forEach(it -> {
                            System.out.println(it.getTitle());
                            System.out.println(it.getAuthor());
                            System.out.println(it.getYear());
                        });

//        year2.length
//        author.stream().reduce((s, s2) -> {
//            dao.findAllByTitleContainingAndAuthor__VALUEContainingAndYearIn(title, it, year))
//        }

    }
}