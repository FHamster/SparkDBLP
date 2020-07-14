package cn.jmu.spark_dblp.server.dao;

import cn.jmu.spark_dblp.server.entity.MastersThesis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

//@Repository
public interface MastersThesisDAO /*extends PagingAndSortingRepository<MastersThesis, String> */{
//    Stream<MastersThesis> findAllByTitleContaining(String title);
}
