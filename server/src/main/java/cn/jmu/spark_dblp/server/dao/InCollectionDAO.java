package cn.jmu.spark_dblp.server.dao;

import cn.jmu.spark_dblp.server.entity.InCollection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface InCollectionDAO extends PagingAndSortingRepository<InCollection, String> {
    Stream<InCollection> findAllByTitleContaining(String title);
}
