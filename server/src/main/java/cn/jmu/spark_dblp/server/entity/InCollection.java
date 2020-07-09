package cn.jmu.spark_dblp.server.entity;

import cn.jmu.spark_dblp.server.entity.sub.*;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "incollection")
@Data
public class InCollection extends AbstractDoc{
/*    @Id
    protected String _id;
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
    @Field
    private String crossref;
    @Field
    private String cdrom;
    @Field
    private Long chapter;
/*    @Field
    private List<Cite> cite;*/
/*    @Field
    private List<Ee> ee;*/
/*    @Field
    private List<Note> note;*/
    @Field
    private Long number;
    @Field
    private String pages;
    @Field
    private Publisher publisher;
/*    @Field
    private String title;*/
    /*    @Field
        private String url;*/
/*    @Field
    private Long year;*/
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
 |-- booktitle: string (nullable = true)
 |-- cdrom: string (nullable = true)
 |-- chapter: long (nullable = true)
 |-- cite: array (nullable = true)
 |    |-- element: struct (containsNull = true)
 |    |    |-- _VALUE: string (nullable = true)
 |    |    |-- _label: string (nullable = true)
 |-- crossref: string (nullable = true)
 |-- ee: array (nullable = true)
 |    |-- element: struct (containsNull = true)
 |    |    |-- _VALUE: string (nullable = true)
 |    |    |-- _type: string (nullable = true)
 |-- note: string (nullable = true)
 |-- number: string (nullable = true)
 |-- pages: string (nullable = true)
 |-- publisher: struct (nullable = true)
 |    |-- _VALUE: string (nullable = true)
 |    |-- _href: string (nullable = true)
 |-- title: string (nullable = true)
 |-- url: string (nullable = true)
 |-- year: long (nullable = true)
 */
