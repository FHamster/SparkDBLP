package cn.jmu.spark_dblp.server.other;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class CollectionTest {

    @Test
    public void StackTest() {
        Stack<Integer> s = new Stack<>();
        s.push(3);
        s.push(2);
        s.push(1);
        Collections.reverse(s);

        List<Integer> l = new ArrayList<>();
        l.add(0);
        l.addAll(s);

        System.out.println(l);
    }
}
