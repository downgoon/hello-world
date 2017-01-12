package io.downgoon.hello.spring.redis;

import java.io.Serializable;

public class Employee implements Serializable {

    private static final long serialVersionUID = -1L;

    private String name;
    private Integer age;
    
    
    public Employee() {
    	/* called on JSON de-serialization */
    }

    public Employee(String name, Integer age) {
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
