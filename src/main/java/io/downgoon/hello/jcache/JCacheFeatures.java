package io.downgoon.hello.jcache;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.FactoryBuilder;
import javax.cache.configuration.MutableCacheEntryListenerConfiguration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.expiry.ExpiryPolicy;
import javax.cache.spi.CachingProvider;

import com.hazelcast.cache.ICache;

public class JCacheFeatures {

	public static void main(String[] args) {

		CachingProvider cacheProvider = Caching.getCachingProvider();

		CacheManager cacheManager = cacheProvider.getCacheManager();

		MutableConfiguration<String, String> configuration = new MutableConfiguration<String, String>();

		
		// public class MutableCacheEntryListenerConfiguration<K, V> implements CacheEntryListenerConfiguration<K, V>
		
		configuration.setTypes(String.class, String.class).addCacheEntryListenerConfiguration(
				new MutableCacheEntryListenerConfiguration<String, String>(
						FactoryBuilder.factoryOf(new MyCacheEntryListener<String, String>()),
						FactoryBuilder.factoryOf(new MyCacheEntryEventFilter<String, String>()),
						true, 
						true)
		);

		configuration.setExpiryPolicyFactory(AccessedExpiryPolicy.factoryOf(Duration.ONE_MINUTE));

		Cache<String, String> myCache = cacheManager.createCache("myCache", configuration);

//		myCache.registerCacheEntryListener(new MutableCacheEntryListenerConfiguration<String, String>(
//				FactoryBuilder.factoryOf(new MyCacheEntryListener<String, String>()),
//				FactoryBuilder.factoryOf(new MyCacheEntryEventFilter<String, String>()),
//				true, 
//				true));

		myCache.put("key", "value");
		myCache.get("key");

		// ICache extends Cache interface, provides more functionality
		ICache<String, String> icache = myCache.unwrap(ICache.class);

		icache.getAsync("key");
		icache.putAsync("key", "value");

		final ExpiryPolicy customExpiryPolicy = AccessedExpiryPolicy.factoryOf(Duration.TEN_MINUTES).create();
		icache.put("key", "newValue", customExpiryPolicy);

		// cache size
		icache.size();
	}
}
