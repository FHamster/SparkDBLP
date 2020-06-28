package cn.jmu.spark_dblp.server.dao;

import cn.jmu.spark_dblp.server.entity.Article;
import cn.jmu.spark_dblp.server.entity.Authors;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorsDAO extends CrudRepository<Authors, String> {
    List<Authors> findAllBy_VALUEContaining(String author);
}
