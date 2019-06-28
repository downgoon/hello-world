package io.downgoon.hello.jcache;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;

public class JCacheEntryProcessors {

	public static void main(String[] args) {
		CachingProvider cachingProvider = Caching.getCachingProvider();
		CacheManager cacheManager = cachingProvider.getCacheManager();

		MutableConfiguration<String, Integer> config = 
		   new MutableConfiguration<String, Integer>();
		config.setTypes(String.class, Integer.class);

		Cache<String, Integer> cache = cacheManager.createCache("MyCache", config);

		String key = "k";
		Integer value = 1;

		cache.put(key, value);

		System.out.println("The value is " + cache.get(key) + "\n");

		// INCR
		cache.invoke(key, new MyEntryProcessor());

		System.out.println("The value is now " + cache.get(key) + "\n");
	}

}
