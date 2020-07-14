package cn.jmu.spark_dblp.server.entity;

import cn.jmu.spark_dblp.server.entity.sub.Author;
import cn.jmu.spark_dblp.server.entity.sub.Ee;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
public abstract class BaseDoc {
    @Id
    protected String _id;
    @Field
    protected String _key;
    @Field
    protected String prefix1;
    @Field
    protected String prefix2;
    @Field
    protected List<Author> author;
    @Field
    protected List<Ee> ee;
    @Field
    protected String title;
    @Field
    protected Long year;
    @Field
    private String _publtype;
    @Field
    protected String month;
    @Field
    protected String type;
/*    @Field
    protected List<Cite> cite;*/
}