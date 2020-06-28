package cn.jmu.spark_dblp.server.controller;

import cn.jmu.spark_dblp.server.dao.ArticleDAO;
import cn.jmu.spark_dblp.server.entity.Article;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    ArticleDAO dao;

    @PostMapping("/search")
    public Stream<Article> getAuthorsBy_VALUE(
            @RequestBody Map<String, Object> body
    ) {
        System.out.println(body.get("title"));
        System.out.println(body.get("msg"));
//        System.out.println(body.get("author"));
//        System.out.println(body.get("year"));
        List<String> author = (List<String>) body.getOrDefault("author", new ArrayList<>());
//        author.forEach(System.out::println);
        List<Integer> finalYear = ((List<String>) body.getOrDefault("year", new ArrayList<>())).stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
//        finalYear.forEach(System.out::println);
        String title = (String) body.get("title");
        List<Pattern> p = author.stream()
                .map(it -> Pattern.compile(it, Pattern.CASE_INSENSITIVE))
                .collect(Collectors.toList());
        return dao.findAllByTitleContainingAndAuthor__VALUEContainingAndYearIn(title, p, finalYear);

    }


}
