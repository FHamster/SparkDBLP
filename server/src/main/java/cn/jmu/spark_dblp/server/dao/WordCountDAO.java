package cn.jmu.spark_dblp.server.dao;

import cn.jmu.spark_dblp.server.entity.WordCount;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface WordCountDAO extends PagingAndSortingRepository<WordCount, String> {

    //    @Query(sort = "{title: {$regex: '?0', $options: 'i'}}")
//    @Query(value = "{}"
    List<WordCount> findAll();
}

