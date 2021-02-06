package cn.jmu.spark_dblp.server.service;


import cn.jmu.spark_dblp.server.dao.OnlyDocDAO;
import cn.jmu.spark_dblp.server.entity.OnlyDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 这个Service是为了实现对关键字的搜索结果进行缓存并将数据过滤工作由数据库上推至服务器层面
 */
@Service
public class CacheService {
    @Autowired
    OnlyDocDAO dao;

    /**
     * 关键的根据关键字获取匹配结果
     *
     * @param title 匹配的title关键字
     * @return 匹配成功的OnlyDoc列表
     * @apiNote 该服务会对数据进行缓存
     */
    @CachePut(value = "ReSTQueryCache", key = "#uuid")
    public List<OnlyDoc> putOnlyDocListCache(String uuid,String title) {
        return dao.findAllByTextReturnListJPA(title);
    }

    @Cacheable(value = "ReSTQueryCache", key = "#uuid")
    public List<OnlyDoc> getOnlyDocListCache(String uuid) {
        return null;
    }
}