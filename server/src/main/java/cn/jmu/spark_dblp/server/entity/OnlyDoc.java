package cn.jmu.spark_dblp.server.entity;

import cn.jmu.spark_dblp.server.entity.sub.*;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;
@Document(collection = "onlyDoc")
@Data
public class OnlyDoc {
    @MongoId
    private String _id;
    @Field
    private String _key;
    @Field
    private String prefix1;
    @Field
    private String prefix2;
    @Field
    private List<Author> author;
    @Field
    private List<Ee> ee;
    @Field
    private String title;
    @Field
    private Long year;
    @Field
    private String _publtype;
    @Field
    private String month;
    @Field
    private String type;
    //===========================================article specify===========================================
    @Field
    private String booktitle;
    @Field
    private String crossref;
    @Field
    private List<Editor> editor;
    @Field
    private String journal;
    @Field
    private String pages;
    @Field
    private String publisher;
    @Field
    private String volume;
    @Field
    private List<String> isbn;
    @Field
    private List<String> school;
    @Field
    private List<Series> series;
    @Field
    private String cdrom;
    @Field
    private Long chapter;
    //long -> string
    @Field
    private String number;
    @Field
    private String type_xml;
//    @Field
//    private String type;
}
