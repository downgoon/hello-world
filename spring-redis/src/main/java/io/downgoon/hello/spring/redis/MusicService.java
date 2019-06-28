package io.downgoon.hello.spring.redis;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class MusicService {

	@Cacheable(value = "messageCache", condition = "'guitar'.equals(#instrument)")
	public String play(final String instrument) {
		System.out.println("Executing: " + this.getClass().getSimpleName() + ".play(" + instrument + ")");
		return "paying " + instrument + "!";
	}

}
