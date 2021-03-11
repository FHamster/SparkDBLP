package cn.jmu.spark_dblp.server.util;

import cn.jmu.spark_dblp.server.entity.OnlyDoc;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.rutledgepaulv.qbuilders.builders.GeneralQueryBuilder;
import com.github.rutledgepaulv.qbuilders.conditions.Condition;
import com.github.rutledgepaulv.qbuilders.visitors.MongoVisitor;
import com.github.rutledgepaulv.qbuilders.visitors.RSQLVisitor;
import com.github.rutledgepaulv.rqe.pipes.QueryConversionPipeline;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 处理Condition的工具
 * @deprecated 处理RSQL语句的抽象由String改为更安全的RSQLFilter对象，该工具的功能由RSQLFilter实现
 */
@Deprecated
public class ConditionUtil {
    public static boolean isEqual(Condition<GeneralQueryBuilder> c1, Condition<GeneralQueryBuilder> c2) {
        if (c1 == null && c2 == null) return true;
        else if (c1 == null || c2 == null) return false;
        String q1 = c1.query(new MongoVisitor()).getCriteriaObject().toJson();
        String q2 = c2.query(new MongoVisitor()).getCriteriaObject().toJson();

        Set<String> p1 = JSONObject.parseObject(q1).getJSONArray("$and").stream()
                .map(JSON::toJSONString)
                .collect(Collectors.toSet());
        Set<String> p2 = JSONObject.parseObject(q2).getJSONArray("$and").stream()
                .map(JSON::toJSONString)
                .collect(Collectors.toSet());

        //        System.out.println(p1);
//        System.out.println(p2);
//        System.out.println(b);
        boolean equals = p1.equals(p2);
        return equals;
    }

    @Deprecated
    public static String Condition2LexOrderString(Condition<GeneralQueryBuilder> c1) {
        QueryConversionPipeline pipeline = QueryConversionPipeline.defaultPipeline();
        String q1 = c1.query(new RSQLVisitor());
        String p = pipeline.apply(q1, OnlyDoc.class)
                .query(new MongoVisitor()).getCriteriaObject().toJson();

//        Optional<String> o = Optional.ofNullable(JSONObject.parseObject(q1)
//                .getJSONArray("$and"));
//
//        Set<String> p1 = .stream()
//                .map(JSON::toJSONString)
//                .collect(Collectors.toSet());

        return null;
//        return Arrays.toString(p1.toArray());
    }

    public static String Condition2LexOrderString(List<String> c1) {
        Set<String> p1;
        if (c1.size() > 1) {
            String rsql = c1.stream().collect(Collectors.joining(";"));
            QueryConversionPipeline pipeline = QueryConversionPipeline.defaultPipeline();
//        String q1 = c1.query(new RSQLVisitor());
            String p = pipeline.apply(rsql, OnlyDoc.class)
                    .query(new MongoVisitor())
                    .getCriteriaObject()
                    .toJson();
            p1 = JSONObject.parseObject(p)
                    .getJSONArray("$and")
                    .stream()
                    .map(JSON::toJSONString)
                    .collect(Collectors.toSet());
        } else {
            p1 = new HashSet<>(c1);
        }


//        return null;
        return Arrays.toString(p1.toArray());
    }

    public static String Condition2TimeLineString(Condition<GeneralQueryBuilder> c1) {
        String q1 = c1.query(new MongoVisitor()).getCriteriaObject().toJson();

        return Arrays.toString(JSONObject.parseObject(q1).getJSONArray("$and").stream()
                .map(JSON::toJSONString).distinct().toArray());
    }

    public static List<String> Condition2TimeLineList(Condition<GeneralQueryBuilder> c1) {
        String q1 = c1.query(new MongoVisitor()).getCriteriaObject().toJson();

        return JSONObject.parseObject(q1).getJSONArray("$and").stream()
                .map(JSON::toJSONString)
                .distinct()
                .collect(Collectors.toList());
    }

    public static String RSQConcat(String l, String r) {
        QueryConversionPipeline pipeline = QueryConversionPipeline.defaultPipeline();
        try {
            pipeline.apply(l, OnlyDoc.class);
        } catch (Exception e) {
            return r;
        }
        try {
            pipeline.apply(r, OnlyDoc.class);
        } catch (Exception e) {
            return l;
        }
        return l + ";" + r;
    }
}