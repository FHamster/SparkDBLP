package cn.jmu.spark_dblp.server.controller;

import cn.jmu.spark_dblp.server.dao.AuthorsDAO;
import cn.jmu.spark_dblp.server.entity.Authors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Stream;

@RestController
@RequestMapping("/authors")
public class AuthorsController {
    @Autowired
    AuthorsDAO dao;

    @GetMapping
    public Stream<Authors> getAuthorsBy_VALUE(
            @RequestParam("_VALUE") String _VALUE
    ) {
//        return dao.findAllBy_VALUEContaining(_VALUE);
        return null;
    }

}
