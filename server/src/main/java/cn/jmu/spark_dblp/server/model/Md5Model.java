package cn.jmu.spark_dblp.server.model;

import org.springframework.hateoas.RepresentationModel;

public class Md5Model extends RepresentationModel<Md5Model> {
    public String md5;

    public Md5Model(String md5) {
        this.md5 = md5;
    }
}