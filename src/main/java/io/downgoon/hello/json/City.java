package io.downgoon.hello.json;

public class City {
	
	private String name;
	
	private Integer gdp;

	public City() {
		/* called on JSON de-serialization */
	}
	
	public City(String name, Integer gdp) {
		super();
		this.name = name;
		this.gdp = gdp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getGdp() {
		return gdp;
	}

	public void setGdp(Integer gdp) {
		this.gdp = gdp;
	}

	@Override
	public String toString() {
		return "City [name=" + name + ", gdp=" + gdp + "]";
	}

}
