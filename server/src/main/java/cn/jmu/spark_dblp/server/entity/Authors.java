package cn.jmu.spark_dblp.server.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "DistinctAuthor")
@Data
public class Authors {
    @Id
    protected String _id;
    @Field
    private String _VALUE;
    @Field
    private String _orcid;
    @Field
    private String _aux;
}
