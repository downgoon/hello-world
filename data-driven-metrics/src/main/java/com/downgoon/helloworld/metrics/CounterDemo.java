package com.downgoon.helloworld.metrics;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Counter;
import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;

/**
 * @author downgoon@qq.com
 * @since 2016-05-17
 */
public class CounterDemo {


    private static final String[] videoNames = {"中国有嘻哈", "延禧攻略", "天天向上", "我的前半生"};


    public static void main(String[] args) throws InterruptedException {
        MetricRegistry metricRegistry = new MetricRegistry();
        String totalKey = MetricRegistry.name(CounterDemo.class, "vv", "total");
        Counter totalView = metricRegistry.counter(totalKey);


        // 控制台报表
        ConsoleReporter consoleReporter = ConsoleReporter.forRegistry(metricRegistry).build();
        consoleReporter.start(1, TimeUnit.SECONDS);

        // JMX报表
        JmxReporter jmxReporter = JmxReporter.forRegistry(metricRegistry).build();
        jmxReporter.start();


        // 模拟用户随机播放视频
        Random rand = new Random();
        while (true) {
            Thread.sleep(300);
            // 随机模拟一个用户，播放了某个视频
            String videoName = videoNames[rand.nextInt(4)];

            // 找到视频对应的并发计数器
            Counter videoCounter = metricRegistry.counter(MetricRegistry.name(CounterDemo.class, "vv", videoName));
            // 具体某个视频的VV
            videoCounter.inc();

            // 全站总VV
            totalView.inc();
        }
    }
}
