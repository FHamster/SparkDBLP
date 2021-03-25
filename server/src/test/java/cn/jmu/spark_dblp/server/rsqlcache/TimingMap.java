package cn.jmu.spark_dblp.server.rsqlcache;

import java.util.function.Function;


public class TimingMap<T, R> {
    private long beforeTime;
    private long afterTime;
    private final Function<T, R> f;


    public R accept(T t) {
        before();
        R r = f.apply(t);
        after();
        return r;
    }

    public void before() {
        beforeTime = System.currentTimeMillis();
    }

    public void after() {
        afterTime = System.currentTimeMillis();
    }

    public long get() {
        return afterTime - beforeTime;
    }

    public TimingMap(Function<T, R> f) {
        this.f = f;
    }
}