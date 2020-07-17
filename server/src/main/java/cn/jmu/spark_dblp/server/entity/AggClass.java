package cn.jmu.spark_dblp.server.entity;

import lombok.Data;

@Data
public class AggClass {
    String group;
    long count;

    public AggClass(String group, long count) {
        this.group = group;
        this.count = count;
    }
}
