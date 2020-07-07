package cn.jmu.spark_dblp.server.dao;

import cn.jmu.spark_dblp.server.entity.InProceeding;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface InProceedingDAO extends PagingAndSortingRepository<InProceeding, String> {
    Stream<InProceeding> findAllByTitleContaining(String title);
}
