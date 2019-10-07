package com.downgoon.helloworld.metrics;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.ExponentiallyDecayingReservoir;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author downgoon@qq.com
 * @since 2016-05-17
 */
public class HistogramDemo2 {

    public static void main(String[] args) throws InterruptedException {

        // 构建一个Histogram 计算器
        Histogram latencyHistogram = new Histogram(new ExponentiallyDecayingReservoir());


        // 把计算器放入metric容器
        MetricRegistry metricRegistry = new MetricRegistry();
        metricRegistry.register(MetricRegistry.name(HistogramDemo2.class, "adserver", "letencyMS"), latencyHistogram);


        // 线程不断请求百度，并记录响应时延
        new Thread(() -> {

            int i = 0;
            while (i < 100) {
                OkHttpClient httpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://www.baidu.com")
                        .build();

                long tmStart = System.currentTimeMillis();
                try (Response response = httpClient.newCall(request).execute()) {
                	
                    System.out.println(new String(response.body().bytes()).substring(0, 20));

                } catch (IOException e) {
                    e.printStackTrace();
                }
                long tmEnded = System.currentTimeMillis();

                // 存入响应时延
                latencyHistogram.update(tmEnded - tmStart);
                
                try {
					Thread.sleep(1500L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

                i ++;
            }


        }, "http-client").start();


        // 把响应时延的统计数据显示在控制台
        ConsoleReporter consoleReporter = ConsoleReporter.forRegistry(metricRegistry).build();
        consoleReporter.start(1, TimeUnit.SECONDS);

    }
}
