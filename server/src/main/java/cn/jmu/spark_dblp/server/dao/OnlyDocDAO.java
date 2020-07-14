package cn.jmu.spark_dblp.server.dao;

import cn.jmu.spark_dblp.server.entity.OnlyDoc;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnlyDocDAO extends PagingAndSortingRepository<OnlyDoc, String>, BaseDAO {
//    Page<OnlyDoc> findAllByTitleContainingIgnoreCase(String title, Pageable pageable);
//    Stream<Book> findAllByTitleContaining(String title);
}
