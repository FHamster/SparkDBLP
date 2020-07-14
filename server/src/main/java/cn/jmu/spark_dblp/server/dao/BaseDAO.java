package cn.jmu.spark_dblp.server.dao;

import cn.jmu.spark_dblp.server.entity.Article;
import cn.jmu.spark_dblp.server.entity.BaseDoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;

import java.util.stream.Stream;

public interface BaseDAO {
    Page<BaseDoc> findAllByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query("{'author._VALUE': ?0}")
    Page<BaseDoc> findAllByAuthorContainingAccurate(String author, Pageable pageable);

//    Stream<BaseDoc> findAllByTitleContainingIgnoreCase(String title);
//    Page<BaseDoc> (String title, Pageable pageable);
}
