package cn.jmu.spark_dblp.server.service;


import cn.jmu.spark_dblp.server.dao.OnlyDocDAO;
import cn.jmu.spark_dblp.server.entity.OnlyDoc;
import cn.jmu.spark_dblp.server.entity.sub.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 这个Service是为了实现对关键字的搜索结果进行缓存并将数据过滤工作由数据库上推至服务器层面
 */
@Service
public class OnlyDocService {
    @Autowired
    OnlyDocDAO dao;

    /**
     * 关键的根据关键字获取匹配结果
     *
     * @param title 匹配的title关键字
     * @return 匹配成功的OnlyDoc列表
     * @apiNote 该服务会对数据进行缓存
     */
    @Cacheable(value = "onlyDocTitleCache", key = "#title")
    public List<OnlyDoc> findAllByTitleMatchesTextReturnList(String title) {
        return dao.findAllByText(title);
    }

    @Cacheable(value = "onlyDocAuthorCache", key = "#author")
    public List<OnlyDoc> findAllByAuthor__VALUE(String author) {
        return dao.findAllByAuthor__VALUE(author);
    }

    /**
     * 根据year字段使用流过滤
     *
     * @param stream    输入流
     * @param yearArray 条件集合
     * @return 符合过滤条件的流
     * @deprecated
     */
    public Stream<OnlyDoc> filterByYear(Stream<OnlyDoc> stream, String[] yearArray) {
        return stream.filter(onlyDoc -> {
            Long docYear = onlyDoc.getYearOption().orElse(0L);
            for (String it : yearArray) if (docYear.equals(Long.valueOf(it))) return true;
            return false;
        });
    }

    /**
     * 根据venue字段使用流过滤
     *
     * @param stream     输入流
     * @param venueArray 条件集合
     * @return 符合过滤条件的流
     * @deprecated
     */
    public Stream<OnlyDoc> filterByVenue(Stream<OnlyDoc> stream, String[] venueArray) {
        return stream.filter(onlyDoc -> {
            String docPrefix2 = onlyDoc.getPrefix2Option().orElse("");
            for (String it : venueArray) if (docPrefix2.equals(it)) return true;
            return false;
        });
    }

    /**
     * 根据type字段使用流过滤
     *
     * @param stream    输入流
     * @param typeArray 条件集合
     * @return 符合过滤条件的流
     * @deprecated
     */
    public Stream<OnlyDoc> filterByType(Stream<OnlyDoc> stream, String[] typeArray) {
        return stream.filter(onlyDoc -> {
            String docPrefix2 = onlyDoc.getTypeOption().orElse("");
            for (String it : typeArray) if (docPrefix2.equals(it)) return true;
            return false;
        });
    }

    /**
     * 根据authro字段使用流过滤
     *
     * @param stream      输入流
     * @param authorArray 条件集合
     * @return 符合过滤条件的流
     */
    public Stream<OnlyDoc> filterByAuthor(Stream<OnlyDoc> stream, String[] authorArray) {
        return stream.filter(onlyDoc -> {
            List<String> list = onlyDoc.getAuthorOption()
                    .orElse(new ArrayList<>())
                    .stream()
                    .map(Author::get_VALUE)
                    .collect(Collectors.toList());
            for (String s : authorArray) if (list.contains(s)) return true;
            return false;
        });
    }
}
