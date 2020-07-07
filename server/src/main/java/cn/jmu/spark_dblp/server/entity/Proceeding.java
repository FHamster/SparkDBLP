package cn.jmu.spark_dblp.server.entity;

import cn.jmu.spark_dblp.server.entity.sub.*;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "proceedings")
@Data
public class Proceeding {
    @Id
    protected String _id;
    @Field
    private String _key;
    @Field
    private String prefix;
    /*    @Field
        private String _mdate;*/
    @Field
    private String _publtype;
/*    @Field
    private String address;*/
    @Field
    private List<Author> authorList;
    @Field
    private String booktitle;
/*    @Field
    private List<Cite> cite;*/
    @Field
    private List<Editor> editorList;
    @Field
    private List<Ee> eeList;
    @Field
    private List<ISBN> isbnList;
    @Field
    private String journal;
    @Field
    private String month;
/*    @Field
    private List<Note> note;*/
    @Field
    private String number;
    @Field
    private String pages;
    @Field
    private List<Publisher> publisherList;
    @Field
    private List<Series> seriesList;
    @Field
    private String title;
    /*    @Field
        private String url;*/
    @Field
    private String volume;
    @Field
    private Long year;


}
/*
root
 |-- _key: string (nullable = true)
 |-- _mdate: string (nullable = true)
 |-- _publtype: string (nullable = true)
 |-- address: string (nullable = true)
 |-- author: string (nullable = true)
 |-- booktitle: string (nullable = true)
 |-- cite: array (nullable = true)
 |    |-- element: struct (containsNull = true)
 |    |    |-- _VALUE: string (nullable = true)
 |    |    |-- _label: string (nullable = true)
 |-- editor: array (nullable = true)
 |    |-- element: struct (containsNull = true)
 |    |    |-- _VALUE: string (nullable = true)
 |    |    |-- _orcid: string (nullable = true)
 |-- ee: array (nullable = true)
 |    |-- element: struct (containsNull = true)
 |    |    |-- _VALUE: string (nullable = true)
 |    |    |-- _type: string (nullable = true)
 |-- isbn: array (nullable = true)
 |    |-- element: struct (containsNull = true)
 |    |    |-- _VALUE: string (nullable = true)
 |    |    |-- _type: string (nullable = true)
 |-- journal: string (nullable = true)
 |-- note: array (nullable = true)
 |    |-- element: struct (containsNull = true)
 |    |    |-- _VALUE: string (nullable = true)
 |    |    |-- _type: string (nullable = true)
 |-- number: string (nullable = true)
 |-- pages: string (nullable = true)
 |-- publisher: array (nullable = true)
 |    |-- element: struct (containsNull = true)
 |    |    |-- _VALUE: string (nullable = true)
 |    |    |-- _href: string (nullable = true)
 |-- series: array (nullable = true)
 |    |-- element: struct (containsNull = true)
 |    |    |-- _VALUE: string (nullable = true)
 |    |    |-- _href: string (nullable = true)
 |-- title: string (nullable = true)
 |-- url: string (nullable = true)
 |-- volume: string (nullable = true)
 |-- year: long (nullable = true)
 */
