/*
package cn.jmu.spark_dblp.server.dao;

import cn.jmu.spark_dblp.server.entity.AggClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface AggregateDAO extends PagingAndSortingRepository<AggClass, String> {
    @Aggregation
    Page<AggClass> findAllByTitleContainingIgnoreCase(String title, Pageable pageable);

    Stream<AggClass> findAllByTitleContainingIgnoreCase(String title);
//    Page<BaseDoc> (String title, Pageable pageable);
}
*/
