package cn.jmu.spark_dblp.server.entity;

import cn.jmu.spark_dblp.server.entity.sub.*;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "article")
@Data
public class Article extends AbstractDoc{
/*    @Id
    protected String _id;
*//*    @Field
    private String _cdate;*//*
    @Field
    private String _key;
    @Field
    private String prefix1;
    @Field
    private String prefix2;*/
/*    @Field
    private String _mdate;*/
    @Field
    private String _publtype;
/*    @Field
    private List<Author> author;*/
    @Field
    private String booktitle;
/*    @Field
    private String cdrom;*/
/*    @Field
    private List<Cite> cite;*/
    @Field
    private String crossref;
    @Field
    private List<Editor> editor;
/*    @Field
    private List<Ee> ee;*/
    @Field
    private String journal;
/*    @Field
    private String month;*/
/*    @Field
    private List<Note> note;*/
    @Field
    private String pages;
    @Field
    private String publisher;
/*    @Field
    private String title;*/
/*    @Field
    private String url;*/
    @Field
    private String volume;
/*    @Field
    private Long year;*/

}
    /*

    root
 |-- _cdate: string (nullable = true)
 |-- _key: string (nullable = true)
 |-- _mdate: string (nullable = true)
 |-- _publtype: string (nullable = true)
 |-- author: array (nullable = true)
 |    |-- element: struct (containsNull = true)
 |    |    |-- _VALUE: string (nullable = true)
 |    |    |-- _aux: string (nullable = true)
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
 |-- journal: string (nullable = true)
 |-- month: string (nullable = true)
 |-- note: array (nullable = true)
 |    |-- element: struct (containsNull = true)
 |    |    |-- _VALUE: string (nullable = true)
 |    |    |-- _type: string (nullable = true)
 |-- number: string (nullable = true)
 |-- pages: string (nullable = true)
 |-- publisher: string (nullable = true)
 |-- title: struct (nullable = true)
 |    |-- _VALUE: string (nullable = true)
 |    |-- _bibtex: string (nullable = true)
 |-- url: string (nullable = true)
 |-- volume: string (nullable = true)
 |-- year: long (nullable = true)
    * */