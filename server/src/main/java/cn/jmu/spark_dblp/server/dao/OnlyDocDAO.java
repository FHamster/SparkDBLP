package cn.jmu.spark_dblp.server.dao;

import cn.jmu.spark_dblp.server.entity.OnlyDoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface OnlyDocDAO extends MongoRepository<OnlyDoc, String> {
    /**
     * 正则模糊匹配onlyDoc.title（忽略大小写）
     *
     * @param title    模糊匹配的关键字
     * @param pageable 分页信息
     * @return 根据title正则匹配成功的onlyDoc分页
     * @deprecated 正则匹配速度太慢废弃
     */
    @Query(value = "{title: {$regex: '?0', $options: 'i'}}")
    Page<OnlyDoc> findAllByTitleMatches(String title, Pageable pageable);

    //    List<OnlyDoc> findAllByTitleContainingIgnoreCase(String title);

    /**
     * 正则模糊匹配onlyDoc.title（忽略大小写）
     *
     * @param title 模糊匹配的关键字
     * @return 根据title正则匹配成功的onlyDoc Stream
     * @deprecated 正则匹配速度太慢、返回流的传输速度太慢废弃
     */
    @Query(value = "{title: {$regex: '?0', $options: 'i'}}")
    Stream<OnlyDoc> findAllByTitleMatchesRegexReturnStream(String title);
//    List<OnlyDoc> findAllByTitleContainingIgnoreCase(String title);

    /**
     * 根据文本索引进行匹配
     *
     * @param title    匹配关键字
     * @param pageable 分页信息
     * @return 根据title进行text索引匹配成功的onlyDoc分页
     * @deprecated 这种方式无法进行缓存 现在使用OnlyDocDAO.findAllByTextReturnListJPA
     */
    @Query(value = "{$text: {$search: ?0}})", sort = "{ year : -1 }")
    Page<OnlyDoc> findAllByText(String title, Pageable pageable);


    /**
     * 根据prefix2&volume进行精确匹配
     *
     * @param prefix2 精确的prefix2字段
     * @param volume  精确的volume字段
     * @return 匹配成功的列表
     */
    List<OnlyDoc> findAllByPrefix2AndVolume(String prefix2, String volume);

    /**
     * 根据文本索引进行匹配
     *
     * @param title 匹配关键字
     * @return
     */
    @Query(value = "{$text: {$search: ?0}})", sort = "{ year : -1 }")
    List<OnlyDoc> findAllByTextReturnListJPA(String title);

    /**
     * 根据crossref进行精确匹配
     *
     * @param crossref 需要精确匹配的crossref
     * @return 匹配成功的OnlyDoc列表
     */
    List<OnlyDoc> findAllByCrossref(String crossref);


    /**
     * 根据作者名称进行精确匹配
     *
     * @param author   作者名称
     * @param pageable 分页信息
     * @return 匹配成功的OnlyDoc分页
     */
    @Query(value = "{'author._VALUE': ?0}")
    Page<OnlyDoc> findAllByAuthor__VALUE(String author, Pageable pageable);
}
