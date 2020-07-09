package cn.jmu.spark_dblp.server.dao;

import cn.jmu.spark_dblp.server.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface BookDAO extends PagingAndSortingRepository<Book, String> {
    Page<Book> findAllByTitleContaining(String title,Pageable pageable);
//    Stream<Book> findAllByTitleContaining(String title);
}
