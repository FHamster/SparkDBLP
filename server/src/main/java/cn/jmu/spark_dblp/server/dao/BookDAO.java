package cn.jmu.spark_dblp.server.dao;

import cn.jmu.spark_dblp.server.entity.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface BookDAO extends PagingAndSortingRepository<Book, String> {
    Stream<Book> findAllByTitleContaining(String title);
}
