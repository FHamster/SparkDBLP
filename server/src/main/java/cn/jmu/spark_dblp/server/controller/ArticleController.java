package cn.jmu.spark_dblp.server.controller;

import cn.jmu.spark_dblp.server.dao.ArticleDAO;
import cn.jmu.spark_dblp.server.entity.Article;
import cn.jmu.spark_dblp.server.entity.sub.Author;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.util.resources.cldr.vai.CalendarData_vai_Latn_LR;

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
    public Stream<Article> combineSearch(
            @RequestBody Map<String, Object> body
    ) {
//        System.out.println(body.get("title"));
//        System.out.println(body.get("msg"));
//        System.out.println(body.get("author"));
//        System.out.println(body.get("year"));
        List<String> author = (List<String>) body.getOrDefault("author", new ArrayList<>());
        List<Integer> finalYear = ((List<String>) body.getOrDefault("year", new ArrayList<>())).stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        String title = (String) body.getOrDefault("title", null);
        if (title == null) {
            title = "";
        }
        System.out.println(title);
        List<Pattern> p = author.stream()
                .map(it -> Pattern.compile(it, Pattern.CASE_INSENSITIVE))
                .collect(Collectors.toList());
        return dao.findAllByTitleContainingAndAuthor__VALUEContainingAndYearIn(title, p, finalYear);

    }

    @GetMapping("/accurateAuthor")
    public Stream<Article> findAllByAuthor(
            @RequestParam("author") String _VALUE
    ) {
//        Author author = new Author(_VALUE);
//        System.out.println(_VALUE);
        return dao.findAllByAuthorContainingAccurate(_VALUE);
    }


}
