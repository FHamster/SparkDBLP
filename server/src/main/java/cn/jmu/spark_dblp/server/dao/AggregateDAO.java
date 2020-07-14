package cn.jmu.spark_dblp.server.dao;

import cn.jmu.spark_dblp.server.entity.BaseDoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface AggregateDAO extends PagingAndSortingRepository<BaseDoc,String> {
    @Aggregation
    Page<BaseDoc> findAllByTitleContainingIgnoreCase(String title, Pageable pageable);

    Stream<BaseDoc> findAllByTitleContainingIgnoreCase(String title);
//    Page<BaseDoc> (String title, Pageable pageable);
}
