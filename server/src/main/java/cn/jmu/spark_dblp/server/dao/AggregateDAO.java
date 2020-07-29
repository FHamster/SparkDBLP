package cn.jmu.spark_dblp.server.dao;

import cn.jmu.spark_dblp.server.entity.AggClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;

import java.util.stream.Stream;

/**
 * @deprecated 不需要使用mongodb使用聚合
 */
//@Repository
//@RepositoryRestResource(exported = false)
public interface AggregateDAO /*extends PagingAndSortingRepository<AggClass, String>*/ {
    @Aggregation
    Page<AggClass> findAllByTitleContainingIgnoreCase(String title, Pageable pageable);

    Stream<AggClass> findAllByTitleContainingIgnoreCase(String title);
//    Page<BaseDoc> (String title, Pageable pageable);
}
