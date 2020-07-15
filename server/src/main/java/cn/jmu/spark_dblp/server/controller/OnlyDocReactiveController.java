package cn.jmu.spark_dblp.server.controller;

import cn.jmu.spark_dblp.server.dao.OnlyDocReactiveDAO;
import cn.jmu.spark_dblp.server.entity.OnlyDocReactive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
@RequestMapping(value = "/onlyDocReactive", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
//@RequestMapping(value = "/onlyDocReactive")
public class OnlyDocReactiveController {
    @Autowired
    OnlyDocReactiveDAO dao;

    @GetMapping(value = "/test")
    public Flux<OnlyDocReactive> findAll() {
        return dao.findAllByTitleMatchesRegexReactive("hadoop").delayElements(Duration.ofSeconds(1));
//        return dao.findAllByTitleMatchesRegexReactive("hadoop");
    }

    @GetMapping(value = "/findAllByTitleMatchesRegexReactive")
    public Flux<OnlyDocReactive> findAllByTitleMatchesRegexReactive(
            @RequestParam String title
    ) {
        return dao.findAllByTitleMatchesRegexReactive(title);
//        return dao.findAllByTitleMatchesRegexReactive("hadoop");
    }

}
