package cn.jmu.spark_dblp.server.controller;

import cn.jmu.spark_dblp.server.dao.ArticleDAO;
import cn.jmu.spark_dblp.server.entity.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    ) throws Exception {
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
        if (title.equals("") && author.size() == 0 && finalYear.size() == 0) {
            throw new Exception("搜索关键字全空错误");
        }
        List<Pattern> p = author.stream()
                .map(it -> Pattern.compile(it, Pattern.CASE_INSENSITIVE))
                .collect(Collectors.toList());
        return dao.findAllByTitleContainingAndAuthor__VALUEContainingAndYearIn(title, p, finalYear);

    }

    @GetMapping("/accurateAuthor")
    public Stream<Article> findAllByAuthor(
            @RequestParam("author") String _VALUE
    ) {
        System.out.println(_VALUE);
        return dao.findAllByAuthorContainingAccurate(_VALUE);
    }


}
