package cn.jmu.spark_dblp.server.dao;

import cn.jmu.spark_dblp.server.entity.Article;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Repository
public interface ArticleDAO extends PagingAndSortingRepository<Article, String>,BaseDAO {
    @Query("{" +
            "title: ?#{ [0].isEmpty() ?  {$exists :true} : {$regex: [0], $options: '$i'} }," +
            "'author._VALUE': ?#{ [1].size()==0 ?  {$exists :true} : {$in:[1]} }," +
            "'year': ?#{ [2].size()==0 ? {$exists :true} : {$in: [2]}  }"+
            "}")
    Stream<Article> findAllByTitleContainingAndAuthor__VALUEContainingAndYearIn(
            String title,
            List<Pattern> author,
            List<Integer> year
    );

    @Query("{'author._VALUE': ?0}")
    Stream<Article> findAllByAuthorContainingAccurate(String author);



}
