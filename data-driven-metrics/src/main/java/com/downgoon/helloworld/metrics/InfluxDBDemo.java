package com.downgoon.helloworld.metrics;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ScheduledReporter;
import com.codahale.metrics.Timer;

import metrics_influxdb.HttpInfluxdbProtocol;
import metrics_influxdb.InfluxdbReporter;
import metrics_influxdb.api.measurements.CategoriesMetricMeasurementTransformer;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author downgoon@qq.com
 * @since 2016-05-17
 */
public class InfluxDBDemo {

	public static Random random = new Random();

	public static void main(String[] args) throws InterruptedException {

		// 创建一个性能计数器
		MetricRegistry metricRegistry = new MetricRegistry();
		String timerName = MetricRegistry.name(InfluxDBDemo.class, "TPSLatency");
		Timer timer = metricRegistry.timer(timerName);

		// 并发线程数量
		int c = 3;
		// 每个并发线程的请求数量
		int n = 1000;

		// 启动N个线程并发工作，N分别等于：10,20,40,80,200和1000
		Thread[] ctN = new Thread[c];
		for (int i = 0; i < ctN.length; i++) {
			ctN[i] = new Thread(new RunnableJob(timer, n), "Job#" + i);
		}
		for (int i = 0; i < ctN.length; i++) {
			ctN[i].start();
		}

		// 查看性能测试结果：TPS & Latency
		ConsoleReporter consoleReporter = ConsoleReporter.forRegistry(metricRegistry).build();
		consoleReporter.start(5, TimeUnit.SECONDS);

		// ConsoleReporter 也是 ScheduledReporter 的子类
		// 每10s 推送到 Influxdb上
		ScheduledReporter influxdbReporter = InfluxdbReporter.forRegistry(metricRegistry).protocol(
				// HttpInfluxdbProtocol(String scheme, String host, int port, String user,
				// String password, String db)
				new HttpInfluxdbProtocol("http", "127.0.0.1", 8086, "admin", "123456", "metrics"))
				.convertRatesTo(TimeUnit.SECONDS).convertDurationsTo(TimeUnit.MILLISECONDS).filter(MetricFilter.ALL)
				.skipIdleMetrics(false) //
				.tag("cluster", "adindex4j") //
				.tag("client", "index-worker") //
				.tag("server", "Tomcat#1") //
				.transformer(new CategoriesMetricMeasurementTransformer("module", "artifact")) //
				.build();

		influxdbReporter.start(5, TimeUnit.SECONDS);

	}

	public static class RunnableJob implements Runnable {

		private Timer timer;

		private int numreq;

		public RunnableJob(Timer timer, int numreq) {
			this.timer = timer;
			this.numreq = numreq;
		}

		@Override
		public void run() {
			int numcnt = 0;
			while (numcnt < numreq) {
				// 对当前请求开始计时
				Timer.Context ctx = timer.time();

				try {
					doHttpGetM1("http://localhost:3004/posts");
				} catch (IOException e) {
					e.printStackTrace();

				} finally {
					// 对当前请求结束计时
					ctx.stop();
					numcnt++;
				}

			}
		}

		private void doHttpGetM1(String urlAddr) throws IOException {
			OkHttpClient httpClient = new OkHttpClient();
			Request request = new Request.Builder().url(urlAddr).build();
			Response response = httpClient.newCall(request).execute();
			System.out.println(new String(response.body().bytes()));
		}

	}

}
