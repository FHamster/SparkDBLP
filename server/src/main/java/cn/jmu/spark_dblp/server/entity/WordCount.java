package cn.jmu.spark_dblp.server.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;


@Document(collection = "wordCount")
@Data
public class WordCount implements Serializable {
    @MongoId
    protected String _id;
    @Field
    private int count;
    @Field
    private String word;
    @Field
    private String wordlength;
}
