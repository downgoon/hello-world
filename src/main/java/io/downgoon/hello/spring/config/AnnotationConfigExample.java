package io.downgoon.hello.spring.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;

public class AnnotationConfigExample {

	public static void main(String[] args) throws Exception {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        
        Employee employee = ctx.getBean(Employee.class);
        System.out.println(employee);
        
        Environment env = ctx.getBean(Environment.class);
        
        System.out.println(env.getProperty("employee.age", Integer.class));
	}
	
}
