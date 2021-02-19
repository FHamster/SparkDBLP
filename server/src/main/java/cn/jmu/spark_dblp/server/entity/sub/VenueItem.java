package cn.jmu.spark_dblp.server.entity.sub;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Data
public class VenueItem implements Serializable {
    @Field
    private String _key;
    @Field
    private String title;
    @Field
    private Long year;
    @Field
    private String type;
    @Field
    private String booktitle;
    @Field
    private String type_xml;
    @Field
    private String crossref;
}
