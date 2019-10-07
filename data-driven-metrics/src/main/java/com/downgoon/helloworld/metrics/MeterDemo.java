package com.downgoon.helloworld.metrics;

import java.io.File;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.CsvReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;

/**
 * @author downgoon@qq.com
 * @since 2016-05-17
 */
public class MeterDemo {

	public static void main(String[] args) throws InterruptedException {
		MetricRegistry metricRegistry = new MetricRegistry();

		String meterName = MetricRegistry.name(MeterDemo.class, "adserver", "tps");

		// get or add meter instance
		Meter tpsMeter = metricRegistry.meter(meterName);

		new Thread(() -> {
			Random rand = new Random();

			while (true) {

				// 模拟每个请求的处理时间，按随机时间算
				try {
					Thread.sleep(rand.nextInt(100));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				// 标记处理完了一个请求
				tpsMeter.mark();

			}

		}, "request-handler").start();

		// 显示到控制台
		ConsoleReporter consoleReporter = ConsoleReporter.forRegistry(metricRegistry).build();
		consoleReporter.start(2, TimeUnit.SECONDS);
		
		// 把TPS过程保存到CSV文件夹中（不是文件）
		File csvFile = new File("data");
		CsvReporter csvReporter = CsvReporter.forRegistry(metricRegistry).build(csvFile);
		csvReporter.start(2, TimeUnit.SECONDS);

	}
}
