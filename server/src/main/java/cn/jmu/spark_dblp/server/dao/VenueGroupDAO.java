package cn.jmu.spark_dblp.server.dao;

import cn.jmu.spark_dblp.server.entity.VenueGroup;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VenueGroupDAO extends PagingAndSortingRepository<VenueGroup, String> {
    /**
     * 模糊匹配
     *
     * @param booktitle 需要匹配的booktitle
     * @return 匹配成功的VenueGroup列表
     * @deprecated 速度太慢
     */
    @Query(value = "{'venue.booktitle':{$regex: '?0', $options: 'i'}}")
    List<VenueGroup> findAllByVenue_BooktitleContainingIgnoreCase(String booktitle);

    /**
     * 根据文本索引进行匹配
     *
     * @param booktitle 文本索引匹配串
     * @return 匹配成功的VenueGroup列表
     * @apiNote mongodb数据库在VenueGroup建立的text为{title,booktitle}
     */
    @Query(value = "{$text: {$search: ?0}})")
    List<VenueGroup> findAllByTextReturnList(String booktitle);
//    Page<Venue> findBy_VALUEStartingWithIgnoreCase(String prefix, Pageable pageable);
}
