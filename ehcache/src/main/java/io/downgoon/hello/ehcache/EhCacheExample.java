package io.downgoon.hello.ehcache;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EhCacheExample {

	private static final Logger LOG = LoggerFactory.getLogger(EhCacheExample.class);
	
	public static void main(String[] args) {
		
		// cacheConfiguration
		CacheConfigurationBuilder<Long, String> cacheConfigurationBuilder = CacheConfigurationBuilder.newCacheConfigurationBuilder(
				Long.class, String.class, 
				ResourcePoolsBuilder.heap(100)
			);
		
		CacheConfiguration<Long, String> cacheConfiguration = cacheConfigurationBuilder.build();
		
		// cacheManager
		CacheManagerBuilder<CacheManager> cacheManagerBuilder = CacheManagerBuilder.newCacheManagerBuilder();
		CacheManager cacheManager = cacheManagerBuilder.build(true);
		

		// cache 
		Cache<Long, String> myCache = cacheManager.createCache("myCache", cacheConfiguration);
		myCache.put(1L, "da one!");
		String value = myCache.get(1L);
		LOG.info("value is: {}", value);
		
		cacheManager.close();
	}

}
