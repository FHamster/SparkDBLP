package cn.jmu.spark_dblp.server.entity.sub;

import lombok.Data;

import java.io.Serializable;

@Data
public class Title implements Serializable {
    private String _VALUE;
    private String _bibtex;
}
