package cn.jmu.spark_dblp.server.dao;

import cn.jmu.spark_dblp.server.entity.Venue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @deprecated venue的数据模式不符合功能预期
 * 迁移至VenueGroupDAO @see {@link VenueGroupDAO}
 */
@Repository
public interface VenueDAO extends PagingAndSortingRepository<Venue, String> {
    @Query(sort = "{booktitle:1}")
    Page<Venue> findAllByBooktitleContainingIgnoreCase(String booktitle, Pageable pageable);

//    Page<Venue> findBy_VALUEStartingWithIgnoreCase(String prefix, Pageable pageable);
}
