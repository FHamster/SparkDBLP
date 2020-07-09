package cn.jmu.spark_dblp.server.entity;

import cn.jmu.spark_dblp.server.entity.sub.*;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "phdthesis")
@Data
public class PhdThesis {
    @Id
    protected String _id;
    @Field
    private String _key;
    @Field
    private String prefix1;
    @Field
    private String prefix2;
    /*    @Field
        private String _mdate;*/
    @Field
    private String _publtype;
    @Field
    private List<Author> author;
    @Field
    private List<Ee> ee;
    @Field
    private List<String> isbn;
    @Field
    private String month;
/*    @Field
    private List<Note> note;*/
/*    @Field
    private String number;*/
    @Field
    private String pages;
    @Field
    private String publisher;
    @Field
    private List<String> school;
    @Field
    private Series series;
    @Field
    private String title;
    @Field
    private String volume;
/*    @Field
    private String url;*/
    @Field
    private Long yea;

}
/*
root
 |-- _key: string (nullable = true)
 |-- _mdate: string (nullable = true)
 |-- _publtype: string (nullable = true)
 |-- author: array (nullable = true)
 |    |-- element: struct (containsNull = true)
 |    |    |-- _VALUE: string (nullable = true)
 |    |    |-- _orcid: string (nullable = true)
 |-- ee: array (nullable = true)
 |    |-- element: struct (containsNull = true)
 |    |    |-- _VALUE: string (nullable = true)
 |    |    |-- _type: string (nullable = true)
 |-- isbn: array (nullable = true)
 |    |-- element: string (containsNull = true)
 |-- month: string (nullable = true)
 |-- note: array (nullable = true)
 |    |-- element: struct (containsNull = true)
 |    |    |-- _VALUE: string (nullable = true)
 |    |    |-- _type: string (nullable = true)
 |-- number: string (nullable = true)
 |-- pages: array (nullable = true)
 |    |-- element: string (containsNull = true)
 |-- publisher: string (nullable = true)
 |-- school: array (nullable = true)
 |    |-- element: string (containsNull = true)
 |-- series: struct (nullable = true)
 |    |-- _VALUE: string (nullable = true)
 |    |-- _href: string (nullable = true)
 |-- title: string (nullable = true)
 |-- volume: string (nullable = true)
 |-- year: array (nullable = true)
 |    |-- element: long (containsNull = true)
 */