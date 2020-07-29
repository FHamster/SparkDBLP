package cn.jmu.spark_dblp.server.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Optional;

/**
 * @deprecated venue的数据模式不符合功能预期
 * 迁移至VenueGroup @see {@link VenueGroup}
 */
@Document(collection = "venue")
@Data
public class Venue {
    @MongoId
    private String _id;
    @Field
    private String _key;
    @Field
    private String prefix1;
    @Field
    private String prefix2;
    @Field
    private String title;
    @Field
    private Long year;
    @Field
    private String type;
    @Field
    private String booktitle;
    @Field
    private String ref;
    @Field
    private String type_xml;

    public Optional<Long> getYearOption() {
        return Optional.ofNullable(year);
    }

    public Optional<String> getTypeOption() {
        return Optional.ofNullable(type);
    }

    public Optional<String> getPrefix2Option() {
        return Optional.ofNullable(prefix2);
    }
}
