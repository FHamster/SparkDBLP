package cn.jmu.spark_dblp.server.entity;

import cn.jmu.spark_dblp.server.entity.sub.Author;
import cn.jmu.spark_dblp.server.entity.sub.Editor;
import cn.jmu.spark_dblp.server.entity.sub.Ee;
import cn.jmu.spark_dblp.server.entity.sub.Series;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.ArrayList;
import java.util.Optional;

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
    private ArrayList<Author> author;
    @Field
    private ArrayList<Ee> ee;
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
    private ArrayList<Editor> editor;
    @Field
    private String journal;
    @Field
    private String pages;
    @Field
    private String publisher;
    @Field
    private String volume;
    @Field
    private ArrayList<String> isbn;
    @Field
    private ArrayList<String> school;
    @Field
    private ArrayList<Series> series;
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

    public Optional<ArrayList<Author>> getAuthor() {
        return Optional.ofNullable(author);
    }
    public Optional<Long> getYear() {
        return Optional.ofNullable(year);
    }
    public Optional<String> getPrefix2() {
        return Optional.ofNullable(prefix2);
    }
}
