# hello-world ``inotify``

>Java 7 has several new classes in the ``java.nio.file`` package that let you listen to file system events.

>If you want to use the ``inotify`` mechanism directly in Java, look at the following libraries

## 样例代码

``` java
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

public class FileWatcherDemo {

	public static void main(String[] args) {

		// define a folder root (Watchable)
		Path liveDir = Paths.get("/Users/liwei/tmp/rtsp-live");
		// Path liveDir = Paths.get("/var/wd/tmp");

		try {

			WatchService liveWatcher = liveDir.getFileSystem().newWatchService();

			liveDir.register(liveWatcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
					StandardWatchEventKinds.ENTRY_MODIFY);

			System.out.println("waiting file events ...");

			// loop forever to watch directory
			while (true) {

				/*
				 * similar to Selector.select() Retrieves and removes next watch
				 * key, waiting if none are yet present.
				 */
				// this call is blocking until events are present
				WatchKey watckKey = liveWatcher.take();

				liveDir.register(liveWatcher, StandardWatchEventKinds.ENTRY_CREATE,
						StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);

				// poll for file system events on the WatchKey
				List<WatchEvent<?>> events = watckKey.pollEvents();
				for (WatchEvent<?> event : events) {
					if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
						System.out.println("Created: " + event.context().toString());
					}
					if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
						System.out.println("Delete: " + event.context().toString());
					}
					if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
						System.out.println("Modify: " + event.context().toString());
					}
				}

				// if the watched directed gets deleted, get out of run method
				// Once the events have been processed the consumer invokes the
				// key's reset method to reset the key which allows the key to
				// be signalled and re-queued with further events.

				// MUST call watchKey.reset() before next iteration
				if (!watckKey.reset()) {
					System.out.println("No longer valid");
					watckKey.cancel();
					liveWatcher.close();
					break;
				}

			}

		} catch (Exception e) {
			System.out.println("Error: " + e.toString());
		}
	}
}
```

## 官方文档

### reset: 下一次take前，必须reset

>A watch service that watches registered objects for changes and events. For example a file manager may use a watch service to monitor a directory for changes so that it can update its display of the list of files when files are created or deleted.

>A Watchable object is registered with a watch service by invoking its register method, returning a WatchKey to represent the registration. When an event for an object is detected the key is signalled, and if not currently signalled, it is queued to the watch service so that it can be retrieved by consumers that invoke the poll or take methods to retrieve keys and process events. **Once the events have been processed the consumer invokes the key's reset method to reset the key which allows the key to be signalled and re-queued with further events.**

### 平台依赖：Linux下借助inotify很快，但mac下很慢

Platform dependencies

>The implementation that observes events from the file system is intended to map directly on to the **native file event notification facility** (Linux-2.6.4 就特别快) where available, or to **use a primitive mechanism, such as polling, when a native facility is not available*** （比如Mac操作系统没有inotify机制，就只能用JVM线程去监听，功能上虽然实现了，但是性能差很大，事件要延迟5~10s，太慢了）.

>Consequently, many of the details on how events are detected, their timeliness, and whether their ordering is preserved are highly implementation specific. **For example, when a file in a watched directory is modified then it may result in a single ENTRY_MODIFY event in some implementations but several events in other implementations**. Short-lived files (meaning files that are deleted very quickly after they are created) may not be detected by primitive implementations that periodically poll the file system to detect changes.

If a watched file is not located on a local storage device then it is implementation specific if changes to the file can be detected. In particular, it is not required that changes to files carried out on remote systems be detected.

不同的平台，事件发现机制是不一样的。比如Linux2.6.4下，是基于``inotify``机制的，可以很快检测到变更。同时一个文件的修改，每次刷盘的时候都会被触发。比如执行``jstack ${pid} > file.jstack``会触发若干次修改事件。但是如果在Mac下，系统层面没有``inotify``机制，这样JVM会用一个线程去定时扫描，文件是否修改依赖的是操作系统的“文件最后修改时间”来判断，如果短时间内修改N次，JVM线程可能只会看到1次，2次不等的情况。

## 等待文件事件

### centos 线程情况

``` java
"Thread-0" #9 daemon prio=5 os_prio=0 tid=0x00007ff18c104800 nid=0x2fb997 runnable [0x00007ff174ed8000]
   java.lang.Thread.State: RUNNABLE
	at sun.nio.fs.LinuxWatchService.poll(Native Method)
	at sun.nio.fs.LinuxWatchService.access$600(LinuxWatchService.java:47)
	at sun.nio.fs.LinuxWatchService$Poller.run(LinuxWatchService.java:314)
	at java.lang.Thread.run(Thread.java:745)

"main" #1 prio=5 os_prio=0 tid=0x00007ff18c007800 nid=0x2fb989 waiting on condition [0x00007ff191b11000]
     java.lang.Thread.State: WAITING (parking)
  	at sun.misc.Unsafe.park(Native Method)
  	- parking to wait for  <0x00000000eb6aee20> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
  	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
  	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
  	at java.util.concurrent.LinkedBlockingDeque.takeFirst(LinkedBlockingDeque.java:492)
  	at java.util.concurrent.LinkedBlockingDeque.take(LinkedBlockingDeque.java:680)
  	at sun.nio.fs.AbstractWatchService.take(AbstractWatchService.java:118)
  	at FileWatcherDemo.main(FileWatcherDemo.java:35)
```

在``centos``的实现，把事件监听与事件处理是分离的。``WatchService.take``的阻塞并不是系统IO阻塞，而是JVM信号量阻塞。它单独用了另外的线程``"Thread-0"``来捕获Linux的文件事件。而且我们做一个``jstack ${pid} > filewatch.jstack``操作，会产生很多事件：

``` bash
waiting file events ...
Created: filewatch.jstack
Created: .attach_pid3127688
Delete: .attach_pid3127688
Modify: filewatch.jstack
...... （此处省略20多个）
Modify: filewatch.jstack
Modify: filewatch.jstack
```

会触发特别多的Modify事件，对于一个``Append``形式的文件追加，每次刷盘都会触发修改事件。

### Mac 线程情况

``` java
"main" #1 prio=5 os_prio=31 tid=0x00007fa81c001800 nid=0x1c03 waiting on condition [0x0000700003e9a000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000007955f7600> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
	at java.util.concurrent.LinkedBlockingDeque.takeFirst(LinkedBlockingDeque.java:492)
	at java.util.concurrent.LinkedBlockingDeque.take(LinkedBlockingDeque.java:680)
	at sun.nio.fs.AbstractWatchService.take(AbstractWatchService.java:118)
	at io.downgoon.hello.FileWatcherDemo.main(FileWatcherDemo.java:36)


"Thread-0" #9 daemon prio=5 os_prio=31 tid=0x00007fa81b034000 nid=0x4d03 waiting on condition [0x0000700004cc4000]
     java.lang.Thread.State: TIMED_WAITING (parking)
  	at sun.misc.Unsafe.park(Native Method)
  	- parking to wait for  <0x00000007955f9560> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
  	at java.util.concurrent.locks.LockSupport.parkNanos(LockSupport.java:215)
  	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.awaitNanos(AbstractQueuedSynchronizer.java:2078)
  	at java.util.concurrent.ScheduledThreadPoolExecutor$DelayedWorkQueue.take(ScheduledThreadPoolExecutor.java:1093)
  	at java.util.concurrent.ScheduledThreadPoolExecutor$DelayedWorkQueue.take(ScheduledThreadPoolExecutor.java:809)
  	at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1067)
  	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1127)
  	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
  	at java.lang.Thread.run(Thread.java:745)
```

``Mac``下监听的显著特点是：特别慢！当向一个目录增加一个文件后，得5s，甚至更长时间会被检测到。

## FAQ

- 不支持``FileClose``事件：支持创建，修改和删除事件的检测。但是一个文件的关闭貌似不支持。
- 如何支持文件的流式同步？也就是文件写了一点，就往远程同步一点。

## 参考资料

- [file-change-notification-example-with-watch-service-api](http://www.codejava.net/java-se/file-io/file-change-notification-example-with-watch-service-api)
- [WatchService.html](https://docs.oracle.com/javase/7/docs/api/java/nio/file/WatchService.html)
- [jnotify](http://jnotify.sourceforge.net/): inotify in java
