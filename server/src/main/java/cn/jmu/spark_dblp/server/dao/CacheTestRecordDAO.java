package cn.jmu.spark_dblp.server.dao;

import cn.jmu.spark_dblp.server.entity.CacheTestRecord;
import cn.jmu.spark_dblp.server.entity.WordCount;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CacheTestRecordDAO extends PagingAndSortingRepository<CacheTestRecord, String> {

}


