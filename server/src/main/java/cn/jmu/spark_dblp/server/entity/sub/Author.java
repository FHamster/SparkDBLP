package cn.jmu.spark_dblp.server.entity.sub;

import lombok.Data;

@Data
public class Author {
    private String _VALUE;
    private String _orcid;
    private String _aux;

    public Author(String _VALUE) {
        this._VALUE = _VALUE;
    }
}
