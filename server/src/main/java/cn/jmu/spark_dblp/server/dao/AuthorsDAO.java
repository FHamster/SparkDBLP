package cn.jmu.spark_dblp.server.dao;

import cn.jmu.spark_dblp.server.entity.Authors;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface AuthorsDAO extends PagingAndSortingRepository<Authors, String> {
    Stream<Authors> findAllBy_VALUEContaining(String author);

    Stream<Authors> findTop10By_VALUEStartingWith(String prefix);
}
