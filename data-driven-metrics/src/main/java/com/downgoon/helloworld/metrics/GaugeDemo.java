package com.downgoon.helloworld.metrics;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author downgoon@qq.com
 * @since 2016-05-17
 */
public class GaugeDemo {

    private static final int SIZE_1MB = 1024 * 1024;


    public static void main(String[] args) {

        // metrics 容器，俗称 "dashbord"
        MetricRegistry metricRegistry = new MetricRegistry();

        // 监控数据#1： 空闲内存数
        String metricName = MetricRegistry.name(GaugeDemo.class, "memory", "freeSizeMB");
        Gauge<Double> metricGauge = new Gauge<Double>() {
            @Override
            public Double getValue() {
                // JVM 剩余内存（单位：MB）
                return Runtime.getRuntime().freeMemory() / (SIZE_1MB + 0.0);
            }
        };


        // 把剩余内存这个检测项，添加到 "dashbord" 中
        metricRegistry.register(metricName, metricGauge);

        // 监控数据#2： JVM总内存数据
        metricRegistry.register(MetricRegistry.name(GaugeDemo.class, "memory", "totalSizeMB"),
                new Gauge<Double>() {
                    @Override
                    public Double getValue() {
                        return Runtime.getRuntime().totalMemory() / (SIZE_1MB + 0.0);
                    }
                });

        // 监控数据#3：JVM内存使用率
        metricRegistry.register(MetricRegistry.name(GaugeDemo.class, "memory", "usageRate%"),
                new Gauge<Double>() {
                    @Override
                    public Double getValue() {
                        // 单位：XX%
                        Long totalSize = Runtime.getRuntime().totalMemory();
                        Long freeSize = Runtime.getRuntime().freeMemory();
                        return ((totalSize - freeSize) / (totalSize + 0.0)) * 100;
                    }
                });


        // 模拟内存消耗
        Random rand = new Random();
        new Thread(() -> {
            List<byte[]> list = new ArrayList<byte[]>();
            while (true) {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 刻意消耗内存，以便查看剩余内存数量
                int allocSize = SIZE_1MB * rand.nextInt(4);
                byte[] buf = new byte[allocSize];
                list.add(buf);
            }
        }, "memleak").start();


        // 额外线程不断查看 dashbord 的各个metrics
        // 与其说是报表，不如说是数据采集器。下例是每2秒采集一次。
        ConsoleReporter consoleReporter = ConsoleReporter.forRegistry(metricRegistry).build();
        consoleReporter.start(2, TimeUnit.SECONDS);


    }
}
