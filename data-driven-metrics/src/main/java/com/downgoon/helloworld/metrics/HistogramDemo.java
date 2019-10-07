package com.downgoon.helloworld.metrics;

import com.codahale.metrics.ExponentiallyDecayingReservoir;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Snapshot;

/**
 * @author downgoon@qq.com
 * @since 2016-05-17
 */
public class HistogramDemo {


    public static void main(String[] args) throws InterruptedException {

        // 一个HTTP服务的响应时延数据
        int[] responseLatencyMS = new int[]{
                459, 118, 384, 564, 655, 509, 601, 418, 452, 513, 556, 658, 698, 414, 280, 549, 348, 629, 686, 666
        };

        responseLatencyMS = new int[] {
                1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,
                1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,
        };

        // histogram 计算器
        Histogram histogram = new Histogram(new ExponentiallyDecayingReservoir());


        // 数据录入
        int i = 0;
        while (i < responseLatencyMS.length) {
            histogram.update(responseLatencyMS[i]);
            i++;
        }

        // histogram 结果输出
        Snapshot r = histogram.getSnapshot();

        System.out.println("Max（最大值）: " + r.getMax());
        System.out.println("min（最小值）: " + r.getMin());
        System.out.println("Mean（平均值）: " + r.getMean());
        System.out.println("Median（中位数）: " + r.getMedian());
        System.out.println("StdDev（标准差）: " + r.getStdDev());

        // 概念：TP100 就是 Max; TP50 就是 中位数。
        // TP99 不是指99%的情况，时延是多少；也不是指99%的平均值；而是99%的最大值（先从小到大排列）
        System.out.println("TP99（第99%百分线的取值）: " + r.get99thPercentile());
        System.out.println("TP95（第95%百分线的取值）: " + r.get95thPercentile());
        System.out.println("TP75（第75%百分线的取值）: " + r.get75thPercentile());


    }
}
