package cn.jmu.spark_dblp.server.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "onlyDoc")
@Data
public class OnlyDoc extends BaseDoc {

}
