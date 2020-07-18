package cn.jmu.spark_dblp.server.entity;

import lombok.Data;

@Data
public class AggClass {
    String group;
    long count;
    String property;
    public AggClass(String group, long count,String property) {
        this.group = group;
        this.count = count;
        this.property = property;
    }
    public AggClass(String group, long count) {
        this.group = group;
        this.count = count;
    }
}
