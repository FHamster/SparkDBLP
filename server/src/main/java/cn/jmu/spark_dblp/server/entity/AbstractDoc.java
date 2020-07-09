package cn.jmu.spark_dblp.server.entity;

import cn.jmu.spark_dblp.server.entity.sub.Author;
import cn.jmu.spark_dblp.server.entity.sub.Cite;
import cn.jmu.spark_dblp.server.entity.sub.Ee;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
public abstract class AbstractDoc {
    @Id
    protected String _id;
    @Field
    protected String _key;
    @Field
    protected String prefix1;
    @Field
    protected String prefix2;
    @Field
    private List<Author> author;
    @Field
    private List<Ee> ee;
    @Field
    private String title;
    @Field
    private Long year;
    @Field
    private String month;
    @Field
    private List<Cite> cite;


}