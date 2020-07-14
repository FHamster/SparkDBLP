package com.example.demo.entity;

import cn.jmu.spark_dblp.server.entity.sub.*;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "onlyDoc")
@Data
public class OnlyDoc {
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
    //===========================================book specify===========================================
   /*  duplicate
    @Field
    private String booktitle;
    @Field
    private String crossref;
    @Field
    private List<Editor> editor;
    @Field
    private List<String> pages;
    @Field
    private Publisher publisher;
    */
    @Field
    private List<ISBN> isbn;
    @Field
    private List<String> school;
    @Field
    private Series series;
    //===========================================incollection specify===========================================
    /* duplicate
    @Field
    private String booktitle;
    @Field
    private String crossref;
    @Field
    private String pages;
    @Field
    private Publisher publisher;
    */
    @Field
    private String cdrom;
    @Field
    private Long chapter;
    //long -> string
    @Field
    private String number;
    //===========================================inproceedings specify===========================================
    /* duplicate
    @Field
    private String booktitle;
    @Field
    private String crossref;
    @Field
    private List<Editor> editor;
    @Field
    private String pages;
    */
    //===========================================mastersthesis specify===========================================
    /* duplicate
    @Field
    private List<String> school;
    */
    //===========================================phdthesis specify===========================================
    /* duplicate
    @Field
    private List<String> isbn;
    @Field
    private String pages;
    @Field
    private String publisher;
    @Field
    private List<String> school;
    @Field
    private Series series;
    @Field
    private String volume;
    */
    //===========================================proceedings specify===========================================
    /* duplcate
    @Field
    private String booktitle;
    @Field
    private List<Editor> editor;
    @Field
    private List<ISBN> isbn;
    @Field
    private String journal;
    @Field
    private String number;
    @Field
    private String pages;
    @Field
    private List<String> publisher;
    @Field
    private String volume;
    */
//    @Field
//    private List<Series> seriess;

}
