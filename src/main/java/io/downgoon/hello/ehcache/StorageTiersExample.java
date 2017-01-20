package io.downgoon.hello.ehcache;

import org.ehcache.Cache;
import org.ehcache.PersistentCacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StorageTiersExample {
	
	private static final Logger LOG = LoggerFactory.getLogger(StorageTiersExample.class);

	public static void main(String[] args) {

		PersistentCacheManager persistentCacheManager = CacheManagerBuilder.newCacheManagerBuilder()
				.with(CacheManagerBuilder.persistence("/tmp/ehcache/hello"))
				.withCache("threeTieredCache",
						CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
								ResourcePoolsBuilder.newResourcePoolsBuilder()
										.heap(10, EntryUnit.ENTRIES)
										.offheap(1, MemoryUnit.MB)
										.disk(20, MemoryUnit.MB)))
				.build(true);

		Cache<Long, String> threeTieredCache = persistentCacheManager.getCache("threeTieredCache", 
				Long.class,
				String.class);

		threeTieredCache.put(123L, "ABC");
		String value = threeTieredCache.get(123L);
		
		LOG.info("value: {}", value);
		
		persistentCacheManager.close();

	}

}
