package io.downgoon.hello.spring.config;

import org.springframework.context.annotation.Bean;

public class AppConfig {

	@Bean
	public Employee employee() {
		Employee e = new Employee();
		e.setName("wangyi");
		e.setAge(28);
		return e;
	}
	

}
