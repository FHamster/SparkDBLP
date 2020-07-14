package cn.jmu.spark_dblp.server.dao;

import cn.jmu.spark_dblp.server.entity.Proceeding;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.webmvc.support.PagingAndSortingTemplateVariables;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface ProceedingDAO/* extends PagingAndSortingRepository<Proceeding, String> */{
    Stream<Proceeding> findAllByTitleContaining(String title);
}
