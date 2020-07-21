package cn.jmu.spark_dblp.server.dao;

import cn.jmu.spark_dblp.server.entity.VenueGroup;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VenueGroupDAO extends PagingAndSortingRepository<VenueGroup, String> {
    @Query(value = "{'venue.booktitle':{$regex: '?0', $options: 'i'}}")
    List<VenueGroup> findAllByVenue_BooktitleContainingIgnoreCase(String booktitle);

//    Page<Venue> findBy_VALUEStartingWithIgnoreCase(String prefix, Pageable pageable);
}
