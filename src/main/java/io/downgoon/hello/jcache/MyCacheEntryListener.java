package io.downgoon.hello.jcache;

import java.io.Serializable;
import javax.cache.event.CacheEntryCreatedListener;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryListenerException;
import javax.cache.event.CacheEntryUpdatedListener;

public class MyCacheEntryListener<K, V> implements CacheEntryCreatedListener<K, V>, CacheEntryUpdatedListener<K, V>, Serializable {
	private static final long serialVersionUID = 1L;

	public void onCreated(Iterable<CacheEntryEvent<? extends K, ? extends V>> events)
			throws CacheEntryListenerException {
		
		for (CacheEntryEvent<? extends K, ? extends V> event : events) {
			System.out.println("Listener Received a " + event);
		}
		
	}

	@Override
	public void onUpdated(Iterable<CacheEntryEvent<? extends K, ? extends V>> events)
			throws CacheEntryListenerException {
		
		for (CacheEntryEvent<? extends K, ? extends V> event : events) {
			System.out.println("Listener onUpdated a " + event);
		}
			
	}
	
}