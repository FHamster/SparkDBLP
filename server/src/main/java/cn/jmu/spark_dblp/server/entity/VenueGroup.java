package cn.jmu.spark_dblp.server.entity;

import cn.jmu.spark_dblp.server.entity.sub.VenueItem;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.ArrayList;

@Document(collection = "venue")
@Data
public class VenueGroup {
    @MongoId
    private String _id;
    @Field
    private String prefix2;
    @Field
    private ArrayList<VenueItem> venue;

}
