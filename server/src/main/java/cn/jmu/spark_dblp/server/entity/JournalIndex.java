package cn.jmu.spark_dblp.server.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.ArrayList;

@Document(collection = "journalIndex")
@Data
public class JournalIndex {
    @MongoId
    private String _id;
    @Field
    private String prefix1;
    @Field
    private String prefix2;
    @Field
    private ArrayList<String> volume;
    @Field
    private ArrayList<String> journal;

}
