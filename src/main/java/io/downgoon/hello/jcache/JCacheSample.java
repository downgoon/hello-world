package io.downgoon.hello.jcache;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.expiry.ExpiryPolicy;

import com.hazelcast.cache.ICache;

public class JCacheSample {

    public static void main(String[] args) {

        CacheManager manager = Caching.getCachingProvider().getCacheManager();
        MutableConfiguration<String, String> configuration = new MutableConfiguration<String, String>();

        configuration.setExpiryPolicyFactory(AccessedExpiryPolicy.factoryOf(Duration.ONE_MINUTE));

        Cache<String, String> myCache = manager.createCache("myCache", configuration);

        myCache.put("key", "value");
        myCache.get("key");
        

        // ICache extends Cache interface, provides more functionality
        ICache<String, String> icache = myCache.unwrap(ICache.class);

        icache.getAsync("key");
        icache.putAsync("key", "value");

        final ExpiryPolicy customExpiryPolicy = AccessedExpiryPolicy.factoryOf(Duration.TEN_MINUTES).create();
        icache.put("key", "newValue", customExpiryPolicy);

        //cache size
        icache.size();
    }
}
