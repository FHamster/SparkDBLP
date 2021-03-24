package cn.jmu.spark_dblp.server.rsqlcache;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class ConditionGenerator {

    public static void main(String[] args) {
        ConditionGenerator generator = new ConditionGenerator(
                "year",
                Arrays.asList("<=", "=", "=>"),
                Arrays.asList("2020", "2017")
        );

        for (int i = 2005; i < 2019; i++) {
            System.out.printf("\"%d\",", i);
        }
    }

    private final String property;
    private final List<String> operator;
    private final List<String> value;
    private final Random random = new Random();

    public ConditionGenerator(String property, List<String> operator, List<String> value) {
        this.property = property;
        this.operator = operator;
        this.value = value;
    }

    public String getCondition() {
        String o = operator.get(random.nextInt(operator.size()));
        String v = value.get(random.nextInt(value.size()));

        return property + o + v;
    }
}