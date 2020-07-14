package cn.jmu.spark_dblp.server.dao;

import cn.jmu.spark_dblp.server.entity.BaseDoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface BaseDAO {
    Page<BaseDoc> findAllByTitleContainingIgnoreCase(String title, Pageable pageable);

//    Stream<BaseDoc> findAllByTitleContainingIgnoreCase(String title);
//    Page<BaseDoc> (String title, Pageable pageable);
}
