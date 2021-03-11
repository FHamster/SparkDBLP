package cn.jmu.spark_dblp.server.other;

import cn.jmu.spark_dblp.server.entity.OnlyDoc;
import cn.jmu.spark_dblp.server.util.ConditionUtil;
import com.github.rutledgepaulv.qbuilders.builders.GeneralQueryBuilder;
import com.github.rutledgepaulv.qbuilders.conditions.Condition;
import com.github.rutledgepaulv.rqe.pipes.QueryConversionPipeline;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @deprecated ConditionUtil已经被废弃
 */
class ConditionUtilTest {
    QueryConversionPipeline pipeline = QueryConversionPipeline.defaultPipeline();

    @Test
    void PredicateEquatable() {
        Condition<GeneralQueryBuilder> condition1 = pipeline.apply("title=re=Hadoop;year==2019", OnlyDoc.class);
        Condition<GeneralQueryBuilder> condition2 = pipeline.apply("title=re=Hadoop;year==2019", OnlyDoc.class);
        Assertions.assertTrue(ConditionUtil.isEqual(condition1, condition2));
    }

    @Test
    void Commutativity() {
        Condition<GeneralQueryBuilder> condition1 = pipeline.apply("year==2019;title=re=Hadoop*", OnlyDoc.class);
        Condition<GeneralQueryBuilder> condition2 = pipeline.apply("title=re=Hadoop*;year==2019", OnlyDoc.class);
        Assertions.assertTrue(ConditionUtil.isEqual(condition1, condition2));
    }

    @Test
    void Condition2StringTest() {
        Condition<GeneralQueryBuilder> condition1 = pipeline.apply("cdrom==abc;year==2019;title=re=Hadoop*", OnlyDoc.class);
        Condition<GeneralQueryBuilder> condition2 = pipeline.apply("cdrom==abc;title=re=Hadoop*;year==2019", OnlyDoc.class);


        System.out.println(ConditionUtil.Condition2LexOrderString(condition1));
        Assertions.assertEquals(
                ConditionUtil.Condition2LexOrderString(condition1),
                ConditionUtil.Condition2LexOrderString(condition2)
        );
    }

    @Test
    void Condition2TimeLineStringTest() {

        Condition<GeneralQueryBuilder> condition1 = pipeline.apply("cdrom==abc;year==2019;title=re=Hadoop*", OnlyDoc.class);
        Condition<GeneralQueryBuilder> condition2 = pipeline.apply("cdrom==abc;title=re=Hadoop*;year==2019", OnlyDoc.class);
//        List<Condition<GeneralQueryBuilder>> l = new ArrayList<>();
//        nil.and().and(condition1, condition2).and(condition1, condition2);
        List<String> a = ConditionUtil.Condition2TimeLineList(condition1);
        a.addAll(ConditionUtil.Condition2TimeLineList(condition2));
        System.out.println(a);

//        System.out.println(ConditionUtil.Condition2String(condition2));
        Assertions.assertEquals(
                ConditionUtil.Condition2TimeLineString(condition1),
                ConditionUtil.Condition2TimeLineString(condition2)
        );
    }

    @Test
    void Condition() {

        String rsql1 = "cdrom==abc;title=re=Hadoop*";
        String rsql2 = "cdrom==abc;title=re=Hadoop*;year==2019";
        String rsql3 = "title=re=Hadoop*;year==2019";
        String rsql4 = "cdrom==ab";

//        System.out.println(ConditionUtil.RSQConcat("cdrom==abc;year==2019;title=re=Hadoop*", "cdrom==abc;title=re=Hadoop*;year==2019"));

        List<String> l = new ArrayList<>();
        l.add(rsql1);
        l.add(rsql2);
        l.add(rsql3);
        l.add(rsql4);

        System.out.println(l.stream().collect(Collectors.joining(";")));

        Condition<GeneralQueryBuilder> condition1 = pipeline.apply(l.stream().collect(Collectors.joining(";")), OnlyDoc.class);

        System.out.println(ConditionUtil.Condition2LexOrderString(condition1));
//        Assertions.assertNotEquals(
//                ConditionUtil.Condition2TimeLineString(condition1),
//                ConditionUtil.Condition2TimeLineString(condition2)
//        );
    }
}