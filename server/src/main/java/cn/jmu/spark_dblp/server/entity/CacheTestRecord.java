package cn.jmu.spark_dblp.server.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;


@Document(collection = "cacheTestRecord")
@Data
public class CacheTestRecord {
    @MongoId
    protected String _id;
    @Field
    private String keyword;
    @Field
    private String clist;
    @Field
    private int depth;
    @Field
    private int repeat;
    @Field
    private boolean isHit;
    @Field
    private Long resTime;

    public CacheTestRecord(String keyword, String clist, int depth, int repeat, boolean isHit, Long resTime) {
        this.keyword = keyword;
        this.clist = clist;
        this.depth = depth;
        this.repeat = repeat;
        this.isHit = isHit;
        this.resTime = resTime;
    }
}
