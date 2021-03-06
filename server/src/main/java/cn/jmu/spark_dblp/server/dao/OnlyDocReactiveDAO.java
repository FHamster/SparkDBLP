package cn.jmu.spark_dblp.server.dao;

import cn.jmu.spark_dblp.server.entity.OnlyDocReactive;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import reactor.core.publisher.Flux;

/**
 * @deprecated 前端无法支持响应式特性
 */
@RepositoryRestResource(exported = false)
public interface OnlyDocReactiveDAO extends ReactiveMongoRepository<OnlyDocReactive, String> {
    //    @Query(value = "{title: {$regex: '?0', $options: 'i'}}")
//    Page<OnlyDoc> findAllByTitleMatchesRegex(String title, Pageable pageable);
//    List<OnlyDoc> findAllByTitleContainingIgnoreCase(String title);
    @Query(value = "{title: {$regex: '?0', $options: 'i'}}")
    Flux<OnlyDocReactive> findAllByTitleMatchesRegexReactive(String title);
//    List<OnlyDoc> findAllByTitleContainingIgnoreCase(String title);

//    @Query(value = "{$text: {$search: ?0}})", sort = "{ year : -1 }")
//    Page<OnlyDoc> findAllByTitleMatches(String title, Pageable pageable);

//    @Query(value = "{'author._VALUE': ?0}", sort = "{ year : -1 }")
//    Page<OnlyDoc> findAllByAuthor__VALUE(String author, Pageable pageable);
//    Page<OnlyDoc> findAllByTitleContainingIgnoreCase(String title, Pageable pageable);
//    Stream<Book> findAllByTitleContaining(String title);
}
