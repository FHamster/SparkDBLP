package cn.jmu.spark_dblp.server.entity;

import cn.jmu.spark_dblp.server.entity.sub.*;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "book")
@Data
public class Book {
    @Id
    protected String _id;
    /*    @Field
    private String _mdate;*/
    @Field
    private String _key;
    @Field
    private String prefix;
    @Field
    private String _publtype;
    @Field
    private List<Author> authorList;
    @Field
    private String booktitle;
    /*    @Field
        private String cdrom;*/
/*    @Field
    private List<Cite> citeList;*/
    @Field
    private List<String> crossrList;
    @Field
    private List<Editor> editorList;
    @Field
    private List<Ee> eeList;
    @Field
    private List<ISBN> isbnList;
    @Field
    private String month;
    @Field
    private List<String> pageList;
/*    @Field
    private List<Note> note;*/
    @Field
    private List<Publisher> publisherList;
    @Field
    private List<String> schoolList;
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
 |-- author: array (nullable = true)
 |    |-- element: struct (containsNull = true)
 |    |    |-- _VALUE: string (nullable = true)
 |    |    |-- _bibtex: string (nullable = true)
 |    |    |-- _orcid: string (nullable = true)
 |-- booktitle: string (nullable = true)
 |-- cdrom: string (nullable = true)
 |-- cite: array (nullable = true)
 |    |-- element: struct (containsNull = true)
 |    |    |-- _VALUE: string (nullable = true)
 |    |    |-- _label: string (nullable = true)
 |-- crossref: string (nullable = true)
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
 |-- month: string (nullable = true)
 |-- note: struct (nullable = true)
 |    |-- _VALUE: string (nullable = true)
 |    |-- _type: string (nullable = true)
 |-- pages: array (nullable = true)
 |    |-- element: string (containsNull = true)
 |-- publisher: struct (nullable = true)
 |    |-- _VALUE: string (nullable = true)
 |    |-- _href: string (nullable = true)
 |-- school: array (nullable = true)
 |    |-- element: string (containsNull = true)
 |-- series: struct (nullable = true)
 |    |-- _VALUE: string (nullable = true)
 |    |-- _href: string (nullable = true)
 |-- title: string (nullable = true)
 |-- url: string (nullable = true)
 |-- volume: string (nullable = true)
 |-- year: long (nullable = true)
* */