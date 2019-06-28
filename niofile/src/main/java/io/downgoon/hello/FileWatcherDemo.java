package io.downgoon.hello;

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