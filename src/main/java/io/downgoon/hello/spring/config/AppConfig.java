package io.downgoon.hello.spring.config;

import org.springframework.context.annotation.Bean;

public class AppConfig {

	@Bean(name = "wangyi")
	public Employee employee() {
		Employee e = new Employee();
		e.setName("wangyi");
		e.setAge(28);
		return e;
	}
	
	
	@Bean(name = "zhangsan")
	public Employee employeeZhangsan() {
		Employee e = new Employee();
		e.setName("zhangsan");
		e.setAge(37);
		return e;
	}

}
