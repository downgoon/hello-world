# Gauge 应用举例

<!-- MDTOC maxdepth:6 firsth1:1 numbering:0 flatten:0 bullets:1 updateOnSave:1 -->

- [Gauge 应用举例](#gauge-应用举例)   
   - [需求描述](#需求描述)   
   - [依赖工具](#依赖工具)   
   - [Gauge定义](#gauge定义)   
   - [模拟内存消耗](#模拟内存消耗)   
   - [单独线程踩点输出](#单独线程踩点输出)   
   - [线程分析](#线程分析)   

<!-- /MDTOC -->

## 需求描述

一个应用程序，需要监控JVM的内存使用情况。把这个数据展示在``Dashbord``上：

| freeSizeMB         | totalSizeMB | usageRate%         |
| ------------------ | ----------- | ------------------ |
| 182.37422943115234 | 245.5       | 25.713144834561163 |



为了简单起见，我们不做页面，而是用一个独立的线程，每2秒，主动把上述信息打印出来。形如：

``` log
-- Gauges ----------------------------------------------------------------------
com.downgoon.helloworld.metrics.GaugeDemo.memory.freeSizeMB
             value = 182.37422943115234
com.downgoon.helloworld.metrics.GaugeDemo.memory.totalSizeMB
             value = 245.5
com.downgoon.helloworld.metrics.GaugeDemo.memory.usageRate%
             value = 25.713144834561163
```





## 依赖工具



``` xml

<dependency>
		<groupId>io.dropwizard.metrics</groupId>
		<artifactId>metrics-core</artifactId>
		<version>3.1.2</version>
</dependency>

```



## Gauge定义



以监控项``memory.freeSizeMB``为例：



``` java

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
```



其他监控项：



``` java

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
```



## 模拟内存消耗



``` java

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
```



## 单独线程踩点输出



``` java

// 额外线程不断查看 dashbord 的各个metrics
// 与其说是报表，不如说是数据采集器。下例是每2秒采集一次。
ConsoleReporter consoleReporter = ConsoleReporter.forRegistry(metricRegistry).build();
consoleReporter.start(2, TimeUnit.SECONDS);

```



## 线程分析

先用``jps -m``查看Java进程，然后用``jstack <pid>``查看堆栈信息。观察线程：



``` jstack

// 报表线程：每2秒执行一次。不是用Sleep方式。
"metrics-console-reporter-1-thread-1" #12 daemon prio=5 os_prio=31 tid=0x00007fa0cf0c2000 nid=0xa803 waiting on condition [0x000070000bb8d000]
   java.lang.Thread.State: TIMED_WAITING (parking)
        at sun.misc.Unsafe.park(Native Method)
        - parking to wait for  <0x000000076eb00730> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
        at java.util.concurrent.locks.LockSupport.parkNanos(LockSupport.java:215)
        at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.awaitNanos(AbstractQueuedSynchronizer.java:2078)
        at java.util.concurrent.ScheduledThreadPoolExecutor$DelayedWorkQueue.take(ScheduledThreadPoolExecutor.java:1093)
        at java.util.concurrent.ScheduledThreadPoolExecutor$DelayedWorkQueue.take(ScheduledThreadPoolExecutor.java:809)
        at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1074)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1134)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
        at java.lang.Thread.run(Thread.java:748)



// 内存泄露线程：不断分配内存，并且不释放
"memleak" #11 prio=5 os_prio=31 tid=0x00007fa0cc9fb000 nid=0x5603 waiting on condition [0x000070000ba8a000]
   java.lang.Thread.State: TIMED_WAITING (sleeping)
        at java.lang.Thread.sleep(Native Method)
        at com.downgoon.helloworld.metrics.GaugeDemo.lambda$main$0(GaugeDemo.java:41)
        at com.downgoon.helloworld.metrics.GaugeDemo$$Lambda$1/824909230.run(Unknown Source)
        at java.lang.Thread.run(Thread.java:748)
```
