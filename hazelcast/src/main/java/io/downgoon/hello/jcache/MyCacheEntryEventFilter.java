package io.downgoon.hello.jcache;

import java.io.Serializable;

import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryEventFilter;
import javax.cache.event.CacheEntryListenerException;

public class MyCacheEntryEventFilter<K, V> implements CacheEntryEventFilter<K, V>, Serializable {
	private static final long serialVersionUID = 1L;

	public boolean evaluate(CacheEntryEvent<? extends K, ? extends V> event) throws CacheEntryListenerException {
		boolean result = false;

//		if (event.getEventType() == EventType.CREATED) {
//			System.out.println("filter event=" + event + " filter result=" + result);
//		}

		System.out.println("filter event=" + event + " filter result=" + result);
		
		return true;
	}
}