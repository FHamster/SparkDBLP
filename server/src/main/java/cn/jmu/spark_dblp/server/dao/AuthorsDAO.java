package cn.jmu.spark_dblp.server.dao;

import cn.jmu.spark_dblp.server.entity.Authors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Distinct的DAO
 */
@Repository
public interface AuthorsDAO extends PagingAndSortingRepository<Authors, String> {
    /**
     * 根据作者名称进行模糊匹配 （忽略大小写）
     *
     * @param author   作者名称
     * @param pageable 分页信息
     * @return 匹配成功的Authors分页
     */
    Page<Authors> findAllBy_VALUEContainingIgnoreCase(String authorRegex, Pageable pageable);

    /**
     * 根据作者名称进行精确前缀匹配
     *
     * @param prefix   作者姓名前缀
     * @param pageable 分页信息
     * @return 匹配成功的Authors
     */
//    @Query("{prefixIndex:{$regex: '?0', $options: 'i'}}")
//    List<Authors> findBy_VALUEStartingWithIgnoreCase(String prefix, Pageable pageable);
    Page<Authors> findAllBy_VALUEStartingWithIgnoreCase(String prefix, Pageable pageable);
}

