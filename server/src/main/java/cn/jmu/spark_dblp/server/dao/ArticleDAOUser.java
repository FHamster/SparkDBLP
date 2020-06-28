package cn.jmu.spark_dblp.server.dao;

import cn.jmu.spark_dblp.server.entity.Article;
import cn.jmu.spark_dblp.server.entity.sub.Author;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface ArticleDAOUser {
    //    Stream<Article> findAllByAuthorContaining(List<Author> author);
    @Query("{ 'title' : ?0 }")
    Stream<Article> findAllB(String title, List<String> authors);

}
