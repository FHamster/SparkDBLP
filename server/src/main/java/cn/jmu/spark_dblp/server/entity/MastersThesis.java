package cn.jmu.spark_dblp.server.entity;

import cn.jmu.spark_dblp.server.entity.sub.*;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "mastersthesis")
@Data
public class MastersThesis {
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
    private List<Ee> ee;
    /*    @Field
        private List<Note> note;*/
/*    @Field
    private String number;*/
    @Field
    private List<String> schoolList;
    @Field
    private String title;
    @Field
    private List<Long> year;
}
/*
root
 |-- _key: string (nullable = true)
 |-- _mdate: string (nullable = true)
 |-- author: string (nullable = true)
 |-- ee: array (nullable = true)
 |    |-- element: struct (containsNull = true)
 |    |    |-- _VALUE: string (nullable = true)
 |    |    |-- _type: string (nullable = true)
 |-- note: struct (nullable = true)
 |    |-- _VALUE: string (nullable = true)
 |    |-- _type: string (nullable = true)
 |-- school: string (nullable = true)
 |-- title: string (nullable = true)
 |-- year: long (nullable = true)
 */