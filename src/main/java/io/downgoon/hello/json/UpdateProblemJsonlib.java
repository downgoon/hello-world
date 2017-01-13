package io.downgoon.hello.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class UpdateProblemJsonlib {

	// json 在 add 的时候，会被拷贝。
	public static void main(String[] args) throws Exception {
		jsonUpdate(args);
		System.out.println();
		
		// System.out.println();
		// javaUpdate(args);
	}

	public static void jsonUpdate(String[] args) throws Exception {

		String originJsonText = "[{\"name\": \"jinan\", \"gdp\": 123},{\"name\": \"qingdao\", \"gdp\": 456}]";
		JSONArray citiesJsonArray = JSONArray.fromObject(originJsonText);

		JSONObject jinanJson = citiesJsonArray.getJSONObject(0); // get 浅拷贝(共享)
		System.out.println("jinan origin << " + jinanJson);
		System.out.println("cities origin << " + citiesJsonArray);

		System.out.println();
		jinanJson.element("gdp", "321");
		System.out.println("jinan changed >> " + jinanJson);
		System.out.println("cities changed ? >> " + citiesJsonArray); // true

		System.out.println();
		JSONObject hezeJson = JSONObject.fromObject("{\"name\": \"heze\", \"gdp\": 789}");
		citiesJsonArray.add(hezeJson); // add 深拷贝（复制）
		System.out.println("cities after heze added >> " + citiesJsonArray);

		System.out.println();
		hezeJson.element("gdp", 987);
		System.out.println("heze updated >> " + hezeJson);
		System.out.println("cities after heze updated ? >> " + citiesJsonArray); // false

		// [{"name":"jinan","gdp":"321"},{"name":"qingdao","gdp":456},{"name":"heze","gdp":789}]
		// heze 数据依然是 789，并没有发生变化。这种特性跟JAVA对象是不一样的。
	}

	public static void javaUpdate(String[] args) throws Exception {

		List<City> cities = new ArrayList<City>(Arrays.asList(new City("jinan", 123), (new City("qingdao", 456))));
		City jinan = cities.get(0); // get 浅拷贝 （共享）
		System.out.println("jinan origin << " + jinan);
		System.out.println("cities origin << " + cities);

		System.out.println();
		jinan.setGdp(321);
		System.out.println("jinan changed >> " + jinan);
		System.out.println("cities changed ? >> " + cities); // true

		System.out.println();
		City heze = new City("heze", 789);
		cities.add(heze); // add 浅拷贝（共享）
		System.out.println("cities after heze added >> " + cities);

		System.out.println();
		heze.setGdp(987);
		System.out.println("heze updated >> " + heze);
		System.out.println("cities after heze updated ? >> " + cities); // true
		// [City [name=jinan, gdp=321], City [name=qingdao, gdp=456], City
		// [name=heze, gdp=987]]
		// heze 数据被一同修改成 987
	}

}
