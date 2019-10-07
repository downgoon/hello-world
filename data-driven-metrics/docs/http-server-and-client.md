# HTTP服务端和客户端快速实验





## json-server 安装和使用



``` bash

$ brew install node

$ node -v
v12.4.0

$ npm install -g json-server  # 安装 json-server 服务器

$ cat db.json   # 创建并查看 数据文件
{
  "posts": [
    { "id": 1, "title": "json-server", "author": "typicode" }
  ],
  "comments": [
    { "id": 1, "body": "some comment", "postId": 1 }
  ],
  "profile": { "name": "typicode" }
}

$ json-server --watch db.json --port 3004  # 启动json-server服务器

  \{^_^}/ hi!

  Loading db.json
  Done

  Resources
  http://localhost:3004/posts
  http://localhost:3004/comments
  http://localhost:3004/profile

  Home
  http://localhost:3004

  Type s + enter at any time to create a snapshot of the database
  Watching...
  

$ curl -i -X GET http://localhost:3004/posts # 客户端访问
HTTP/1.1 200 OK
X-Powered-By: Express
Vary: Origin, Accept-Encoding
Access-Control-Allow-Credentials: true
Cache-Control: no-cache
Pragma: no-cache
Expires: -1
X-Content-Type-Options: nosniff
Content-Type: application/json; charset=utf-8
Content-Length: 77
ETag: W/"4d-49G7XbVRP2NKipc5uj9Z4hcUq3Y"
Date: Mon, 17 Jun 2019 12:16:14 GMT
Connection: keep-alive   # 服务端默认支持保持长链接

[
  {
    "id": 1,
    "title": "json-server",
    "author": "typicode"
  }
]%
```





### curl 命令



``` bash

$ curl -i -X GET http://localhost:3004/posts -v
Note: Unnecessary use of -X or --request, GET is already inferred.
*   Trying ::1...
* TCP_NODELAY set
* Connection failed
* connect to ::1 port 3004 failed: Connection refused
*   Trying 127.0.0.1...
* TCP_NODELAY set
* Connected to localhost (127.0.0.1) port 3004 (#0)
> GET /posts HTTP/1.1
> Host: localhost:3004
> User-Agent: curl/7.54.0
> Accept: */*
>
< HTTP/1.1 200 OK
HTTP/1.1 200 OK
< X-Powered-By: Express
X-Powered-By: Express
< Vary: Origin, Accept-Encoding
Vary: Origin, Accept-Encoding
< Access-Control-Allow-Credentials: true
Access-Control-Allow-Credentials: true
< Cache-Control: no-cache
Cache-Control: no-cache
< Pragma: no-cache
Pragma: no-cache
< Expires: -1
Expires: -1
< X-Content-Type-Options: nosniff
X-Content-Type-Options: nosniff
< Content-Type: application/json; charset=utf-8
Content-Type: application/json; charset=utf-8
< Content-Length: 77
Content-Length: 77
< ETag: W/"4d-49G7XbVRP2NKipc5uj9Z4hcUq3Y"
ETag: W/"4d-49G7XbVRP2NKipc5uj9Z4hcUq3Y"
< Date: Mon, 17 Jun 2019 12:27:28 GMT
Date: Mon, 17 Jun 2019 12:27:28 GMT
< Connection: keep-alive
Connection: keep-alive

<
[
  {
    "id": 1,
    "title": "json-server",
    "author": "typicode"
  }
* Connection #0 to host localhost left intact
]%                                                                                                  ➜  ~

```



### echo + telnet 

每隔1秒，给发送一个HTTP请求，而且是持久链接：

``` bash

$ while :;do echo -e "GET /posts HTTP/1.1\nlocalhost: 3004\n\n";sleep 1;done|telnet localhost 3004
```



###  wrk 



``` bash

$ brew install wrk
$ wrk -t10 -c10 -d120s http://localhost:3004/posts

Running 2m test @ http://localhost:3004/posts
  10 threads and 10 connections

  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     6.98ms   21.89ms 476.97ms   98.39%
    Req/Sec   209.15     24.61   252.00     91.52%
  248196 requests in 2.00m, 106.28MB read
Requests/sec:   2066.60
Transfer/sec:      0.88MB
```



## OkHttp

we’ll go into more advanced use cases of configuring a client with ``custom headers``, ``timeouts``, ``response caching``, etc.



OkHttp is an efficient HTTP & HTTP/2 ``client for Android`` and Java applications.



It comes with advanced features such as ``connection pooling`` (if HTTP/2 isn’t available), transparent ``GZIP compression``, and ``response caching`` to avoid the network completely for repeated requests.



It’s also able to recover from common connection problems and, **on a connection failure, if a service has ``multiple IP`` addresses, it can retry the request to alternate addresses**.



At a high level, the client is designed for both ``blocking synchronous`` and ``nonblocking asynchronous`` calls.



OkHttp supports Android 2.3 and above. For Java, the minimum requirement is 1.7.

After this brief overview, let’s see some usage examples.



``` xml

<dependency>
    <groupId>com.squareup.okhttp3</groupId>
    <artifactId>okhttp</artifactId>
    <version>3.4.2</version>
</dependency>

```



### **Synchronous GET with OkHttp**



``` java

@Test
public void whenGetRequest_thenCorrect() throws IOException {
    Request request = new Request.Builder()
      .url(BASE_URL + "/date")
      .build();
 
    Call call = client.newCall(request);
    Response response = call.execute();
 
    assertThat(response.code(), equalTo(200));
}


```



###  **Asynchronous GET with OkHttp**

``` java

@Test
public void whenAsynchronousGetRequest_thenCorrect() {
    Request request = new Request.Builder()
      .url(BASE_URL + "/date")
      .build();
 
    Call call = client.newCall(request);
    call.enqueue(new Callback() {
        public void onResponse(Call call, Response response) 
          throws IOException {
            // ...
        }
         
        public void onFailure(Call call, IOException e) {
            fail();
        }
    });
}

```



## 附录1：OkHttp编程风格



### 基于Builder的fluent风格



``` java

OkHttpClient client = new OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)       //设置连接超时
        .readTimeout(60, TimeUnit.SECONDS)          //设置读超时
        .writeTimeout(60,TimeUnit.SECONDS)          //设置写超时
        .retryOnConnectionFailure(true)             //是否自动重连
        .build();          

```



我们之前见的最多的是以下风格：

``` java

HttpConf conf = new HttpConf().setReadTimeout(60).setConnectTimeout(120);
OkHttpClient client = new OkHttpClient(conf);
```



Builder风格内部实现就是上述的HttpConf：



``` java

public class OkHttpClient {
  
  // 构造方法是private，跟 OkHttpClient client = new OkHttpClient(conf); 一样
  private OkHttpClient(Builder builder) { 
  	// 从builder中获取配置信息
  }
  
  
  public static final class Builder {
    
    public Builder() {
      // 默认配置
    }
    
		 public OkHttpClient build() {
	      return new OkHttpClient(this);
     }  
  }
  
}


```



理解了``OkHttp``的编码风格后，就很容易读懂其他代码，比如构建一个HTTP请求：



``` java

Request request = new Request.Builder().url(urlAddr).build();

// 刻画一个HTTP请求
RequestConf conf = new RequestConf().url(urlAddr);
Request request = new Request(conf);

// 用Client去构建一个调用
Call call = httpClient.newCall(request)
  
// 这个call可以被执行，也可以被取消
call.execute(); // 同步执行
call.cancel(); // 取消
call.enqueue(); // 异步执行，放入队列的意思
```



再比如更复杂一点的：



``` java

Request request = new Request.Builder()
        .url("https://api.github.com/repos/square/okhttp/issues")                          //设置访问url
        .get()                                                                             //类似的有post、delete、patch、head、put等方法，对应不同的网络请求方法
        .header("User-Agent", "OkHttp Headers.java")                                       //设置header
        .addHeader("Accept", "application/json; q=0.5")                                    //添加header
        .removeHeader("User-Agent")                                                        //移除header
        .headers(new Headers.Builder().add("User-Agent", "OkHttp Headers.java").build())   //移除原有所有header，并设置新header
        .addHeader("Accept", "application/vnd.github.v3+json")
        .build();
```







### OkHttpClient好比一个浏览器



>最好只使用一个共享的OkHttpClient 实例，将所有的网络请求都通过这个实例处理。因为每个OkHttpClient 实例都有自己的连接池和线程池，重用这个实例能降低延时，减少内存消耗，而重复创建新实例则会浪费资源。



OkHttpClient的线程池和连接池在空闲的时候会自动释放，所以一般情况下不需要手动关闭，但是如果出现极端内存不足的情况，可以使用以下代码释放内存:



``` java

client.dispatcher().executorService().shutdown();   //清除并关闭线程池
client.connectionPool().evictAll();                 //清除并关闭连接池
client.cache().close();                             //清除cache

```



如果对一些请求需要特殊定制，可以使用：



> 有些配置是全局共享的，有些配置是因请求而异的，比如readTimeout，查询任务和提交任务，对时延的容忍度就不一样。因此需要``请求级别``的差异化参数。



``` java

OkHttpClient eagerClient = client.newBuilder() 
       .readTimeout(500, TimeUnit.MILLISECONDS)
       .build();

```



这样创建的实例与原实例共享线程池、连接池和其他设置项，只需进行少量配置就可以实现特殊需求。



## 参考资料

- 快速HTTP REST接口服务
  - [/typicode/json-server](https://github.com/typicode/json-server)
  - [jsonplaceholder](http://jsonplaceholder.typicode.com/): json-server 的在线版
  - [/indexzero/http-server](https://github.com/indexzero/http-server)
- 快速MySQL REST接口服务
  - [/o1lab/xmysql](https://github.com/o1lab/xmysql)
  - [/downgoon/autorest4db](https://github.com/downgoon/autorest4db)
- 客户端
  - [curl](https://curl.haxx.se/)：HTTP 功能测试
  - [wrk](https://github.com/wg/wrk)：HTTP 性能测试。支持写Lua脚本 [Intelligent benchmark with wrk](https://medium.com/@felipedutratine/intelligent-benchmark-with-wrk-163986c1587f)
  - [A Guide to OkHttp](https://www.baeldung.com/guide-to-okhttp) + [OkHttp使用小记——8分钟进阶](https://www.jianshu.com/p/a71a42f4634b)
  
  





