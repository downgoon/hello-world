package io.downgoon.hello.spring.config;

public class Employee {

	private String name;
	
	private Integer age;

	public Employee() {
		super();
	}

	public Employee(String name, Integer age) {
		super();
		this.name = name;
		this.age = age;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "Employee [name=" + name + ", age=" + age + "]";
	}
	
	
}
