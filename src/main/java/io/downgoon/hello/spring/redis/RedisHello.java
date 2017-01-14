package io.downgoon.hello.spring.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheElement;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

public class RedisHello {

	private static final Logger LOG = LoggerFactory.getLogger(RedisHello.class);

	public static void main(String[] args) {
		JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setHostName("localhost");
		factory.setPort(6379);
		factory.setPoolConfig(new JedisPoolConfig()); // 
		factory.afterPropertiesSet();

		RedisTemplate<String, Employee> redisTemplate = new RedisTemplate<String, Employee>();
		redisTemplate.setConnectionFactory(factory);

		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		
		redisTemplate.afterPropertiesSet();

		RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
		redisCacheManager.setUsePrefix(true); // dafault 'false'
		redisCacheManager.afterPropertiesSet();
		
		// RedisCache is sub-class of Cache
		RedisCache redisCache = (RedisCache) redisCacheManager.getCache("session"); 
		
		redisCache.put("wangyi", "logined");
		redisCache.put("employee", new Employee("liusan", 23));
		
		// RedisCacheElement is sub-class of ValueWrapper
		RedisCacheElement valueWrapper = (RedisCacheElement) redisCache.get("wangyi");
	
		LOG.info("value: {}, ttl: {}", valueWrapper.get(), valueWrapper.getTimeToLive());
	}

}
