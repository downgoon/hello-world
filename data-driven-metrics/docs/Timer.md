# Timer

<!-- MDTOC maxdepth:6 firsth1:1 numbering:0 flatten:0 bullets:1 updateOnSave:1 -->

- [Timer](#timer)   
   - [TPS&Latency随并发线程的变化](#tpslatency随并发线程的变化)   

<!-- /MDTOC -->

通常性能测试包含两个方面：
- 时延：Latency用之前说的Histogram度量。
- TPS：TPS用之前说的Meter度量。

因为它们两实在太常用了，于是metrics对它们进行了封装，统一到Timer。



## TPS&Latency随并发线程的变化

模拟一个Job，执行一次需要0~500ms不等，观察TPS和Latency随着并发线程数量的变化而变化的情况。



- RunnableJob 的任务

``` java

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
```



- 并发N个线程，并统计结果



``` java

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
```



- 实验结果



| 并发情况 | 平均TPS | 时延TP99 |
| -------- | ------- | -------- |
| C5       | 20      | 500ms    |
| C10      | 40      | 500ms    |
| C20      | 78      | 500ms    |
| C40      | 157     | 500ms    |
| C80      | 315     | 500ms    |
| C200     | 785     | 500ms    |
| C1000    | 3919    | 500ms    |



> 结果居然显示：随着线程数量的增加，TPS居然稳步增加，而时延TP99却几乎没变。这个几乎不太可思议！后面应该有一个实际运算论证下。



- C20结果

``` text
C20
-- Timers ----------------------------------------------------------------------
com.downgoon.helloworld.metrics.TimerDemo.TPS&Latency
             count = 2369
         mean rate = 78.76 calls/second
     1-minute rate = 79.61 calls/second
     5-minute rate = 80.05 calls/second
    15-minute rate = 80.15 calls/second
               min = 0.00 milliseconds
               max = 501.90 milliseconds
              mean = 250.03 milliseconds
            stddev = 145.29 milliseconds
            median = 252.26 milliseconds
              75% <= 375.67 milliseconds
              95% <= 475.47 milliseconds
              98% <= 492.25 milliseconds
              99% <= 498.03 milliseconds
            99.9% <= 500.32 milliseconds
```
