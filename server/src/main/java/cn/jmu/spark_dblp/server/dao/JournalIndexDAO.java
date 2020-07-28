package cn.jmu.spark_dblp.server.dao;

import cn.jmu.spark_dblp.server.entity.JournalIndex;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JournalIndexDAO extends PagingAndSortingRepository<JournalIndex, String> {
    @Query(value="{journal:{$regex: '?0', $options: 'i'}}")
    List<JournalIndex> findAllByJournalContaining(String journal);

//    Page<Venue> findBy_VALUEStartingWithIgnoreCase(String prefix, Pageable pageable);
}
