package io.downgoon.hello.spring.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public class RedisHello {

	private static final Logger LOG = LoggerFactory.getLogger(RedisHello.class);

	public static void main(String[] args) {
		JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setHostName("localhost");
		factory.setPort(6379);
		factory.afterPropertiesSet();

		RedisTemplate<String, Employee> template = new RedisTemplate<String, Employee>();
		template.setConnectionFactory(factory);

		template.setKeySerializer(new StringRedisSerializer());
		// template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		
		template.setValueSerializer(new Jackson2JsonRedisSerializer<Employee>(Employee.class));

		template.afterPropertiesSet();

		template.opsForValue().set("name", new Employee("downgoon", 18));

		Employee employee = template.opsForValue().get("name");

		LOG.info("employee: {}", employee);
	}

}
