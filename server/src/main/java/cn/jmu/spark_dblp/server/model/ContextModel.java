package cn.jmu.spark_dblp.server.model;

import cn.jmu.spark_dblp.server.entity.OnlyDoc;
import org.springframework.hateoas.RepresentationModel;

import java.util.Collection;
import java.util.List;

public class ContextModel extends RepresentationModel<ContextModel> {
    public String contextID;
    public List<String> context;
    public Collection<OnlyDoc> onlyDocList;

    public ContextModel(String contextID, List<String> list) {
        this.contextID = contextID;
        this.context = list;
    }

    public ContextModel(String contextID, List<String> list, List<OnlyDoc> onlyDocList) {
        this.contextID = contextID;
        this.context = list;
        this.onlyDocList = onlyDocList;
    }
}