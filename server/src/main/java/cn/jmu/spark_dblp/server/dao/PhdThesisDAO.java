package cn.jmu.spark_dblp.server.dao;

import cn.jmu.spark_dblp.server.entity.PhdThesis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

//@Repository
public interface PhdThesisDAO /*extends PagingAndSortingRepository<PhdThesis, String> */{
    Stream<PhdThesis> findAllByTitleContaining(String title);
}
