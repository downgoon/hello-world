package io.downgoon.hello.spring.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

public class RedisHello {

	private static final Logger LOG = LoggerFactory.getLogger(RedisHello.class);
	
	public static void main(String[] args) {
		JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.afterPropertiesSet();
		
		
		StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(factory);
        template.afterPropertiesSet();

        
        template.opsForValue().set("name", "downgoon");

        String name = template.opsForValue().get("name");
        
        LOG.info("name: {}", name);
	}

}
