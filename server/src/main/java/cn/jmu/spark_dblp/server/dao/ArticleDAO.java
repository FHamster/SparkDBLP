package cn.jmu.spark_dblp.server.dao;

import cn.jmu.spark_dblp.server.entity.Article;
import cn.jmu.spark_dblp.server.entity.sub.Author;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Repository
public interface ArticleDAO extends CrudRepository<Article, String> {
    List<Article> findAllByAuthorContaining(List<Author> author);

    Stream<Article> findAllByAuthorContaining(Author author);

    @Query("{" +
            "title: ?#{ [0].isEmpty() ?  {$exists :true} : {$regex: [0], $options: '$i'} }," +
            "'author._VALUE': ?#{ [1].size()==0 ?  {$exists :true} : {$in:[1]} }," +
            "'year': ?#{ [2].size()==0 ? {$exists :true} : {$in: [2]}  }"+
            "}")
//            "'year': ?#{ [2].length==0 ? {$exists :true} : {$in: [2]}}}")
    Stream<Article> findAllByTitleContainingAndAuthor__VALUEContainingAndYearIn(
            String title,
            List<Pattern> author,
            List<Integer> year
    );

    @Query("{'author._VALUE': ?0}")
    Stream<Article> findAllByAuthorContainingAccurate(String author);

//    @Query("{title: {$regex: ?0, $Option: '$i'}}")
//    Stream<Article> findAllByTitleContaining(String title);

    //    @Query("{title: ?#{ [title.exists] ? {$exists :true} : {$regex: [title], $Option: '$i'} }}")
//    @Query("{title: ?#{ [title] ? {$exists :true} : {$regex: [1], $Option: '$i'} }}")
    @Query("{title: ?#{ [0].isEmpty() ?  abc : {$regex: [0], $options: '$i'} }}")
    Stream<Article> findAllByTitleContaining(String title);
//    @Query("{'author._VALUE': ?#{ [0].isEmpty() ?  {$regex: '*'} : {$regex: [0], $options: '$i'} } }")
//    Stream<Article> findAllByAuthorContaining2(String author);

}
