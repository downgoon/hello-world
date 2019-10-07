package com.downgoon.helloworld.metrics;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

/**
 * @author downgoon@qq.com
 * @since 2016-05-17
 */
public class TimerDemo {

	public static Random random = new Random();

	public static void main(String[] args) throws InterruptedException {
		
		// 创建一个性能计数器
		MetricRegistry metricRegistry = new MetricRegistry();
		String timerName = MetricRegistry.name(TimerDemo.class, "TPS&Latency");
		Timer timer = metricRegistry.timer(timerName);
		
		
		// 启动N个线程并发工作，N分别等于：10,20,40,80,200和1000
		Thread[] ctN = new Thread[10];
		for (int i = 0; i < ctN.length; i++) {
			ctN[i] = new Thread(new RunnableJob(timer), "Job#" + i);
		}
		for (int i = 0; i < ctN.length; i++) {
			ctN[i].start();
		}
		
		// 查看性能测试结果：TPS & Latency
		ConsoleReporter consoleReporter = ConsoleReporter.forRegistry(metricRegistry).build();
		consoleReporter.start(2, TimeUnit.SECONDS);
		
	}

	public static class RunnableJob implements Runnable {

		private Timer timer;

		public RunnableJob(Timer timer) {
			this.timer = timer;
		}

		@Override
		public void run() {
			while (true) {
				// 对当前请求开始计时
				Timer.Context ctx = timer.time();
				try {
					// Job Time Cost
					Thread.sleep(random.nextInt(500));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// 对当前请求结束计时
				ctx.stop();
			}
		}

	}

}
