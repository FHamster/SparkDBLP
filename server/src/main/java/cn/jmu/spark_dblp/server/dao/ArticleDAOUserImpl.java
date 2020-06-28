/*
package cn.jmu.spark_dblp.server.dao;


import cn.jmu.spark_dblp.server.entity.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.stream.Stream;

public class ArticleDAOUserImpl extends ArticleDAOUser {
    @Autowired
    MongoTemplate template;

    @Override
    public Stream<Article> findAllByTitleContainingAndAuthorContaining(String title, List<String> authors) {
//        Class<Article> a = new Article();
        template.findAll(a);
        return null;
    }
}
*/
