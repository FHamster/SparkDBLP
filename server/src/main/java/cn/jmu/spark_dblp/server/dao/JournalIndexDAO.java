package cn.jmu.spark_dblp.server.dao;

import cn.jmu.spark_dblp.server.entity.JournalIndex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalIndexDAO extends PagingAndSortingRepository<JournalIndex, String> {
    /**
     * 根据journal进行模糊匹配
     *
     * @param journal  journal名称
     * @param pageable 分页信息
     * @return JournalIndex的分页
     */
    @Query(value = "{journal:{$regex: '?0', $options: 'i'}}")
    Page<JournalIndex> findAllByJournalContaining(String journal, Pageable pageable);

//    Page<Venue> findBy_VALUEStartingWithIgnoreCase(String prefix, Pageable pageable);
}
