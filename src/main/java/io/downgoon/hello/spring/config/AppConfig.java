package io.downgoon.hello.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@PropertySource({ "classpath:/application.properties", "classpath:/another.properties" })
public class AppConfig {

	@Autowired
	private Environment env;

	@Bean
	public Employee employeeFromEnv() {
		Employee e = new Employee();
		e.setName(env.getProperty("employee.name"));
		e.setAge(env.getProperty("employee.age", Integer.class));
		return e;
	}

}
