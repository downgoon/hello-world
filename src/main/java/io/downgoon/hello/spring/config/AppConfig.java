package io.downgoon.hello.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

@PropertySource("classpath:/application.properties")
public class AppConfig {
	
	@Value("${employee.name}")
	private String employeeName;

	@Value("${employee.age}")
	private Integer employeeAge;

	@Bean(name = "chenliu")
	public Employee employeeChenliu() {

		Employee e = new Employee();
		e.setName(employeeName);
		e.setAge(employeeAge);
		return e;
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeHolder() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	
	@Autowired
	private Environment env;
	
	@Bean(name = "env")
	public Employee employeeFromEnv() {
		Employee e = new Employee();
		e.setName(env.getProperty("employee.name"));
		e.setAge(env.getProperty("employee.age", Integer.class));
		return e;
	}
	

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
