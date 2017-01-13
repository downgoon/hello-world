package io.downgoon.hello.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class UpdateProblemFastjson {

	public static void main(String[] args) throws Exception {

		String originJsonText = "[{\"name\": \"jinan\", \"gdp\": 123},{\"name\": \"qingdao\", \"gdp\": 456}]";
		JSONArray citiesJsonArray =  JSON.parseArray(originJsonText);
		

		JSONObject jinanJson = citiesJsonArray.getJSONObject(0); // get 浅拷贝(共享)
		System.out.println("jinan origin << " + jinanJson);
		System.out.println("cities origin << " + citiesJsonArray);

		System.out.println();
		jinanJson.put("gdp", 321);
		System.out.println("jinan changed >> " + jinanJson);
		System.out.println("cities changed ? >> " + citiesJsonArray); // true

		System.out.println();
		JSONObject hezeJson = JSON.parseObject("{\"name\": \"heze\", \"gdp\": 789}");
		citiesJsonArray.add(hezeJson); // add 深拷贝（复制）
		System.out.println("cities after heze added >> " + citiesJsonArray);

		System.out.println();
		hezeJson.put("gdp", 987);
		
		System.out.println("heze updated >> " + hezeJson);
		System.out.println("cities after heze updated ? >> " + citiesJsonArray); // false

	}

}
