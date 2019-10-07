# 数据驱动方法论之metrics



## metrics 入门demo

- [Gauge](docs/Gauge.md)： 演示在应用程序内，监控JVM内存使用率。
- [Counter](docs/Counter.md)：演示用``计数器``来统计视频播放量。
- [Histogram](docs/Histogram.md)：演示``时延``分布统计，并重点介绍TP99的概念。
- [Meter](docs/Meter.md)： 演示``QPS/TPS``吞吐量的计算，并输出到CSV，然后用Excel图形展示变化过程。
- [Timer](Timer.md)： 观察TPS和时延随着线程数量的变化而变化。



## metrics 生态：metrics+influxdb+grafana

- [metrics-influxdb-grafana](docs/metrics-influxdb-grafana.md)：基于开源metrics，influxdb和grafana构建实时、多维度、自定义监控平台。
- [influxdb-introduction](docs/influxdb-introduction.md)：时间序列数据库influxdb使用简介。
- [metrics-influxdb](https://github.com/davidB/metrics-influxdb)：metrics推送到influxdb的reporter。


## HTTP 服务端和客户端测试工具

- [http-server-and-client](docs/http-server-and-client.md)
