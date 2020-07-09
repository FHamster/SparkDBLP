package cn.jmu.spark_dblp.server.dao;

import cn.jmu.spark_dblp.server.entity.Authors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorsDAO extends PagingAndSortingRepository<Authors, String> {
    Page<Authors> findAllBy_VALUEContainingIgnoreCase(String author, Pageable pageable);

    Page<Authors> findBy_VALUEStartingWith(String prefix, Pageable pageable);
}
