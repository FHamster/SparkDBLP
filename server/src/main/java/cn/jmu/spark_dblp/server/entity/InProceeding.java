package cn.jmu.spark_dblp.server.entity;

import cn.jmu.spark_dblp.server.entity.sub.Author;
import cn.jmu.spark_dblp.server.entity.sub.Cite;
import cn.jmu.spark_dblp.server.entity.sub.Editor;
import cn.jmu.spark_dblp.server.entity.sub.Ee;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "inproceedings")
@Data
public class InProceeding {
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
    @Field
    private List<Author> authorList;
    @Field
    private String booktitle;
/*    @Field
    private String cdrom;*/
/*    @Field
    private List<Cite> cite;*/
    @Field
    private List<String> crossreList;
    @Field
    private List<Editor> editorList;
    @Field
    private List<Ee> eeList;
    @Field
    private String month;
/*    @Field
    private List<Note> note;*/
/*    @Field
    private String number;*/
    @Field
    private String pages;
    @Field
    private String title;
/*    @Field
    private String url;*/
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
 |    |    |-- _aux: string (nullable = true)
 |    |    |-- _orcid: string (nullable = true)
 |-- booktitle: string (nullable = true)
 |-- cdrom: array (nullable = true)
 |    |-- element: string (containsNull = true)
 |-- cite: array (nullable = true)
 |    |-- element: struct (containsNull = true)
 |    |    |-- _VALUE: string (nullable = true)
 |    |    |-- _label: string (nullable = true)
 |-- crossref: array (nullable = true)
 |    |-- element: string (containsNull = true)
 |-- editor: array (nullable = true)
 |    |-- element: struct (containsNull = true)
 |    |    |-- _VALUE: string (nullable = true)
 |    |    |-- _orcid: string (nullable = true)
 |-- ee: array (nullable = true)
 |    |-- element: struct (containsNull = true)
 |    |    |-- _VALUE: string (nullable = true)
 |    |    |-- _type: string (nullable = true)
 |-- month: string (nullable = true)
 |-- note: struct (nullable = true)
 |    |-- _VALUE: string (nullable = true)
 |    |-- _type: string (nullable = true)
 |-- number: string (nullable = true)
 |-- pages: string (nullable = true)
 |-- title: struct (nullable = true)
 |    |-- _VALUE: string (nullable = true)
 |    |-- _bibtex: string (nullable = true)
 |-- url: string (nullable = true)
 |-- year: long (nullable = true)
 */