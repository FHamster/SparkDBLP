package cn.jmu.spark_dblp.server.model;

import org.springframework.hateoas.RepresentationModel;

public class UuidModel extends RepresentationModel<UuidModel> {
    public String uuid;

    public UuidModel(String uuid) {
        this.uuid = uuid;
    }
}