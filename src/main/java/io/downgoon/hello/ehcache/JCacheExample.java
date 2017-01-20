package io.downgoon.hello.ehcache;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JCacheExample {

	private static final Logger LOG = LoggerFactory.getLogger(JCacheExample.class);

	public static void main(String[] args) {
		CachingProvider provider = Caching.getCachingProvider(); // #1

		CacheManager cacheManager = provider.getCacheManager(); // #2

		MutableConfiguration<Long, String> configuration = new MutableConfiguration<Long, String>() // #3
				.setTypes(Long.class, String.class) // #4
				.setStoreByValue(false) // #5
				.setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.ONE_MINUTE)); // #6

		Cache<Long, String> cache = cacheManager.createCache("jCache", configuration); // #7
		cache.put(1L, "one"); // #8
		String value = cache.get(1L); // #9

		LOG.info("value cached: {}", value);
	}

}
