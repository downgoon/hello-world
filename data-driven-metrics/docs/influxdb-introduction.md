# 	influxdb 使用介绍

<!-- toc -->

<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [influxdb 数据模型与查询](#influxdb-数据模型与查询)
	- [数据模型](#数据模型)
		- [时间序列](#时间序列)
		- [通信协议](#通信协议)
		- [读写操作](#读写操作)
	- [查询语言](#查询语言)
		- [独特的GroupBy](#独特的groupby)
		- [tag 基数查询](#tag-基数查询)
		- [scheme 查询](#scheme-查询)
		- [聚合函数](#聚合函数)
		- [无法聚合](#无法聚合)
		- [时间戳筛选](#时间戳筛选)
	- [数据采集](#数据采集)
		- [创建数据库](#创建数据库)
		- [写入数据库](#写入数据库)
			- [单条写入](#单条写入)
			- [批量写入](#批量写入)
			- [导入文件](#导入文件)
	- [参考文档](#参考文档)

<!-- /TOC -->



## 数据模型

Data in InfluxDB is organized by ``time series``, which contain a measured value, like “cpu_load” or “temperature”. Time series have zero to many ``points``, one for each ``discrete sample``（离散的采样） of the metric.

``Points`` consist of ``time`` (a timestamp), a ``measurement`` (“cpu_load”), at least one key-value ``field`` (the measured value itself, e.g. “value=0.64” or “15min=0.78”), and zero to many key-value ``tags`` containing metadata (e.g. “host=server01”, “region=EMEA”, “dc=Frankfurt”).

**Conceptually you can think of a measurement as an SQL table**, with rows where **the primary index is always time**. tags and fields are effectively columns in the table. ``tags`` are indexed, ``fields`` are not.

**The difference** is that with InfluxDB you can have **millions of ``measurements``**, you don’t have to define schemas up front, and null values aren’t stored.




### 时间序列

什么叫时间序列（``time series``）?

- 语法

``` bash
<measurement>[,<tag-key>=<tag-value>...] <field-key>=<field-value>[,<field2-key>=<field2-value>...] [unix-nano-timestamp]
```

一个``ts``（时间序列的一个``point``）至少由3个部分组成：``measurement``，``field`` 和 ``timestamp``。另外有可选的``tag``。

用OLAP的术语做个解释：

- ``tag``: ``tag``是``维度``，是要建``索引``的，查询时用作``筛选条件``（类比SQL的``where``）。
- ``field``: ``field``是``指标``，无需建索引，查询时候用作``查询结果``（类似SQL的投影``SELECT <field>``）。
- ``measurement``: ``measurement``是``表``，是``数据容器``。虽然类比SQL的表，但不同的是表可以有数百万张表，另外主键（聚族索引）固定是``timestamp``。


- 样例

```influxdb
cpu,host=serverA,region=us_west value=0.64

payment,device=mobile,product=Notepad,method=credit billed=33,licenses=3i 1434067467100293230

stock,symbol=AAPL bid=127.46,ask=127.48
temperature,machine=unit42,type=assembly external=25,internal=37 1434067467000000000
```



### 模型总结



``时间序列``顾名思义，它是考察随时间变化而变化的函数关系。



具体讲是，**它是考察一组指标，随时间变化而变化的关系**。同时为了方便查找，支持打tag，或者说支持维度拆解。



这里说的``一组指标``，组是通过<measurement>来体现，指标是通过<field-key>=<field-value>[,<field2-key>=<field2-value>...] 来体现。



同时它支持对时间的聚合运算，SQL中常见的groupby 是``点式GroupBy``，而时间序列的是``段式GroupBy``。是``Range GroupBy``。



我们逻辑上画了一个温度表：

- **一组指标**： 内部温度、外部温度
- **维度拆分**： 也就是打tag。机器编号、机器区域和机器批次。
- **时间维度**： 一个特定的维度，这个维度能做groupby。

| 时间     | 内部温度 | 外部温度 | ``机器编号`` | ``机器区域`` | ``机器批次`` |
| -------- | -------- | -------- | ------------ | ------------ | ------------ |
| 2019/2/1 | 35       | 42       | A0001        | 华北         | CG01         |
| 2019/2/1 | 34       | 44       | A0011        | 华北         | CG02         |
| 2019/2/1 | 31       | 46       | A0021        | 华南         | CG01         |
| 2019/2/2 | 36       | 41.5     | A0001        | 华北         | CG01         |
| 2019/2/3 | 33       | 43       | A0001        | 华北         | CG01         |

这么看来，时间序列数据库是一个``阉割版的OLAP引擎``。阉割体现在它只能对时间做groupby，其他维度只能做筛选条件。所以速度上会快。



### 通信协议

通信协议非常简单，就是``文本行``（``line protocol``）。

``` bash
<measurement>[,<tag-key>=<tag-value>...] <field-key>=<field-value>[,<field2-key>=<field2-value>...] [unix-nano-timestamp]
```

**注意**

``field``是必选的，但``tag``不是必选的。两者有什么区别呢？``tag``是紧挨着<measurement>的键值对，中间没有 **空格** ，而 ``field`` 是或者排``tag``后面，或者也排``<measurement>``后面（当``tag``不存在的时候），但是它与``<measurement>``之前有个 **空格** 。



### 读写操作

- 写入

用``influxdb``的命令行工具：

```
> INSERT cpu,host=serverA,region=us_west value=0.64
>
```

表达的语义是：美国西部，一台服务器``serverA``，的CPU负载是 0.64

再如：

```
> INSERT temperature,machine=unit42,type=assembly external=25,internal=37
>
```

这里``temperature``是数据容器；``machine=unit42,type=assembly``是``tag``，是``维度``，可作为筛选条件；``external=25,internal=37``是``field``，是``度量``，可作为查询结果。表达的语义是：“机器unit42，是台类型为assembly的机器（负责集成装备应用的），内部温度是37，外部温度是25”。时间戳删略了，默认就是服务器的时间。



- 查询

```
> SELECT * FROM cpu
name: cpu
---------
time		    	                     host     	region   value
2015-10-21T19:28:07.580664347Z  	serverA	  us_west	0.64

```



## 查询语言

时间序列数据库，查询用途特别多，所以专门讲述下。



### 独特的GroupBy：Range GroupBy运算

普通SQL的``GroupBy``，只能把相同的数值``GroupBy``到一起，比如我们要查看一个班级的各省份的人数分布：

``` sql
> SELECT province, sum(province) as gcount
> FROM student
> GROUP BY province
```

但是如果我们要按 **年龄段** 做``GroupBy``呢？默认就不支持了，需要写个函数，而且性能还不高。



>普通``GroupBy``只能对 **离散的点** 聚合；独特的``GroupBy`` 需要对 **连续的段** 做聚合。时间序列数据库需要对 ``timestamp`` 做 **连续的段** 做聚合运算（注意并不需要对所有类型做聚合）。



比如``influxdb``支持查询语法``GROUP BY time(1h)``表示将时间序列，按每小时聚合。当然也可以``group by time(5m) where time > now() - 1h``查询，表示最近一小时，每5分钟聚合。



今后，把这种聚合，叫做``按段聚合``，确切说是只能对``timestamp``做``按段聚合``（grouping by given ``time buckets``）。





### tag 基数查询：相当于Where条件

如下``measurement``的``machine``是一个``tag``：

``` sql
> INSERT temperature,machine=unit42,type=assembly external=25,internal=37
```

每个机器都会向控制中心报送温度，我想看下截止目前有多少机器开始上报了。换句话说要看下这个``tag``的基数(``cardinality``)。



```
> show tag values from "temperature"  WITH KEY = "machine";
name: machine
-------------------
unit42
unit01
unit13
```



**注意**：

>表名``temperature``必须用""引号引起来，否则语法有错。





### scheme 查询：schema less 列式存储

``ts-db``不像关系型数据库，它是没有``schema``的。但是可以查看有哪些``tag``。

对于一个 ``measurement``，除了``timestamp``列和``value``列（可以叫别的名字，只不过当只有一个``field``的时候，大家习惯起名为``value``），怎么知道哪些是``field``，哪些是``tag``呢？

``` sql
> show tag keys from "temperature"
name: temperature
------------------
tagKey
machine
type

```



### 聚合函数

当我们做``聚合``操作的时候，比如“每5分钟聚合”，例如``group by time(5m) where time > now() - 1h``，就会形成“每5分钟的”一组数据，我们需要从这一组数据“选一个”具有代表性的作为``points``来描点绘图，比如可以是“均值”。

``` sql
> SELECT mean(internal)
> FROM temperature
> GROUP BY time(5m)
> where time > now() - 1h
```

**注意**

- 语法1：``1h``必须紧凑写，中间不能有空格，比如``1 h``。
- 语法2：``-``号左右必须有空格，不能``now()-1h``或``now() -1h``，这样识别不了。





### 无法聚合

初次使用``influxdb``的时候，做聚合函数操作，常常会出现不能聚合的异常。

>原因往往是：数据不是 **数字型**， 实际上是 **数字字符串**。聚合函数，比如``mean``只能处理 **数字型** 的数据。





### 时间戳筛选

- 保留字：字段``time``是系统保留字，因为``timestamp``是隐含的，无需定义。
- 相对时间：``where time > now() - 1h``
- 格式化时间：时间格式可以是``yyyy-MM-dd HH:mm:ss.SSS``。比如``where time > '2013-08-12 23:32:01.232' and time < '2013-08-13'``
- 时间戳：绝对时间的时间戳``UTC``表示法。比如``where time > 1388534400s``





## 数据采集

``influxdb`` 除了专门针对时间序列的数据库设计，贴合场景的查询语句，被大家广为称道外，还有接入协议非常简洁。



我们很容易把我们的``metrics``接入``influxdb``，因为它就是简单的 文本行协议（``line protocol``），啥叫文本行呢？



我们应用程序写日志的时候就是文本行呀，这样说来，所有的应用日志都可以轻松得导入``influxdb``。

而且设计者还主推了基于``HTTP``协议（并没有自己发明新的协议），写入文本行。HTTP协议不仅可以跨平台，而且熟悉它的人太多太多，这些都极大的降低了接入``influxdb``的门槛。

官方文档 [writing_data](https://docs.influxdata.com/influxdb/v1.2/guides/writing_data/) 写到：





### 创建数据库

``influxdb``并没有给创建数据库设计专门的 HTTP API，而是笼统的设计了一个 ``SQL执行`` 接口 ``/query``，并以``form``表单参数``q``，来传递SQL语句。如下样例创建了一个名叫``mydb``的数据库：

``` bash
$ curl -i -XPOST http://localhost:8086/query --data-urlencode "q=CREATE DATABASE mydb"
```





### 写入数据库

写入 ``influxdb`` 有3种方式，主推 ``HTTP API``：

- HTTP API:  后文重点讲解
- 命令行： [/usr/local/bin/influx](https://docs.influxdata.com/influxdb/v1.2/tools/shell/)
- 客户端：[Java/Node/Python](https://docs.influxdata.com/influxdb/v1.2/tools/api_client_libraries/)
  - Java客户端：[influxdb-java](https://github.com/influxdb/influxdb-java)，顺便提一下，它的HTTP协议部分，并没有用Apache的，而是用了``okhttp3``
  - Node客户端：[node-influx](https://github.com/node-influx/node-influx)



#### 单条写入

``` bash
$ curl -i -XPOST 'http://localhost:8086/write?db=mydb' --data-binary 'cpu_load_short,host=server01,region=us-west value=0.64 1434055562000000000'
```

| 项目 |  数值 |
|----|----|
| 写入接口 | ``/write`` |
| 指定DB | 依靠参数``db=mydb`` |
| Body | 文本行 |

包含2个``tag``的文本行：

```
cpu_load_short,host=server01,region=us-west value=0.64 1434055562000000000
```



#### 批量写入

为了减少往返时延，不用一条条的写入，可以批量写入：

```
curl -i -XPOST 'http://localhost:8086/write?db=mydb' --data-binary 'cpu_load_short,host=server02 value=0.67
cpu_load_short,host=server02,region=us-west value=0.55 1422568543702900257
cpu_load_short,direction=in,host=server01,region=us-west value=2.0 1422568543702900257'
```

在 HTTP Body 里面，直接换行就可以。



#### 导入文件

```
$ curl -i -XPOST 'http://localhost:8086/write?db=mydb' --data-binary @cpu_data.txt
```

**注意**:  ``curl``语法导入文件，有个``@``符号，在文件名前面。

>Note: If your data file has more than **5,000** points, it may be necessary to split that file into several files in order to write your data in batches to InfluxDB. By default, the HTTP request times out after five seconds. InfluxDB will still attempt to write the points after that time out but there will be no confirmation that they were successfully written.



文件内容：

```
$ cat cpu_data.txt
cpu_load_short,host=server02 value=0.67
cpu_load_short,host=server02,region=us-west value=0.55 1422568543702900257
cpu_load_short,direction=in,host=server01,region=us-west value=2.0 1422568543702900257
```





## 服务器日志

``influxdb``默认日志就输出到控制台，错误日志``stderr``，并没有专门管理。

如果想把日志写入文件，可以重定向：

``` bash
$ influxd 2>$HOME/my_log_file
```

但是这样写入，运行时间长了，就不担心日志文件变成几个GB么？



>事实上``influxdb``为了减少依赖，没用第三方日志系统，但也没去处理日志文件。



既然应用层没处理日志文件，那就只能依靠系统层来处理日志了。系统层处理日志常规工具是``logrotate``。





## 参考文档

- [getting_started](https://influxdb.com/docs/v0.9/introduction/getting_started.html )
- [query_syntax](https://docs.influxdata.com/influxdb/v0.9/query_language/query_syntax/)
- [aggregate_functions](https://influxdb.com/docs/v0.7/api/aggregate_functions.html)
- [writing_data](https://docs.influxdata.com/influxdb/v1.2/guides/writing_data/)
- [error log](https://docs.influxdata.com/influxdb/v1.2/administration/logs/)
