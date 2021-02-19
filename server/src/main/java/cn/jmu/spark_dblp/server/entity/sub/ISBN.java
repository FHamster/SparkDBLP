package cn.jmu.spark_dblp.server.entity.sub;

import lombok.Data;

import java.io.Serializable;

@Data
public class ISBN implements Serializable {
    private String _VALUE;
    private String _type;
}
