package cn.jmu.spark_dblp.server.dao;

import cn.jmu.spark_dblp.server.entity.OnlyDoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface OnlyDocDAO extends MongoRepository<OnlyDoc, String> {
    @Query(value = "{title: {$regex: '?0', $options: 'i'}}")
    Page<OnlyDoc> findAllByTitleMatches(String title, Pageable pageable);
//    List<OnlyDoc> findAllByTitleContainingIgnoreCase(String title);
    @Query(value = "{title: {$regex: '?0', $options: 'i'}}")
    Stream<OnlyDoc> findAllByTitleMatchesRegexReturnStream(String title);
//    List<OnlyDoc> findAllByTitleContainingIgnoreCase(String title);

    @Query(value = "{$text: {$search: ?0}})", sort = "{ year : -1 }")
    Page<OnlyDoc> findAllByTitleMatchesText(String title, Pageable pageable);

    @Query(value = "{'author._VALUE': ?0}", sort = "{ year : -1 }")
    Page<OnlyDoc> findAllByAuthor__VALUE(String author, Pageable pageable);
//    Page<OnlyDoc> findAllByTitleContainingIgnoreCase(String title, Pageable pageable);
//    Stream<Book> findAllByTitleContaining(String title);
}
