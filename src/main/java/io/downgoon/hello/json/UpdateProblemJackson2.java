package io.downgoon.hello.json;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class UpdateProblemJackson2 {

	public static void main(String[] args) throws Exception {
		// jsonUpdate(args);
		jsonUpdate();
	}

	public static void jsonUpdate(String[] args) throws Exception {

		ObjectMapper mapper = new ObjectMapper();

		// 反序列化一个对象
		City city0 = mapper.readValue("{\"name\": \"heze\", \"gdp\": 789}", City.class);
		System.out.println(city0);

		// 序列化一个对象
		String city0Ser = mapper.writeValueAsString(city0);
		System.out.println(city0Ser);

		// 美化输出到标准输出

		// 反序列化一个对象数组
		String originJsonText = "[{\"name\": \"jinan\", \"gdp\": 123},{\"name\": \"qingdao\", \"gdp\": 456}]";
		List<City> cities1 = mapper.readValue(originJsonText.getBytes(), 
				new TypeReference<List<City>>() {} );
		System.out.println("cities1:" + cities1);

		// 序列化一个对象数组
		String cities1Text = mapper.writeValueAsString(cities1);
		System.out.println(cities1Text);
		

		// 编辑JSON对象  ( ObjectNode )
		JsonNode jsonNode = mapper.readValue("{\"name\": \"heze\", \"gdp\": 789}", JsonNode.class);
		System.out.println("jsonNode: "+ jsonNode);
		
		int gdp = jsonNode.path("gdp").asInt();
		System.out.println("gdp: " + gdp);
		
		// 增加字段
		((ObjectNode) jsonNode).put("year", 2017);
		
		System.out.println("json node (after year added): " + jsonNode);
		
		// 修改字段
		((ObjectNode) jsonNode).put("name", "hangzhou");
		System.out.println("json node (after name value updated): " + jsonNode);
		
		// 删除字段
		((ObjectNode) jsonNode).remove("gdp");
		System.out.println("json node (after gdp field removed): " + jsonNode);
		
	}
	
	public static void jsonUpdate() throws Exception {

		String originJsonText = "[{\"name\": \"jinan\", \"gdp\": 123},{\"name\": \"qingdao\", \"gdp\": 456}]";
		
		ObjectMapper mapper = new ObjectMapper();
		
		// JSONArray citiesJsonArray = JSONArray.fromObject(originJsonText);
		ArrayNode citiesJsonArray = mapper.readValue(originJsonText.getBytes(), ArrayNode.class);

		// JSONObject jinanJson = citiesJsonArray.getJSONObject(0); // get 浅拷贝(共享)
		ObjectNode jinanJson = (ObjectNode) citiesJsonArray.get(0);
		
		System.out.println("jinan origin << " + jinanJson);
		System.out.println("cities origin << " + citiesJsonArray);

		System.out.println();
		// jinanJson.element("gdp", "321");
		jinanJson.put("gdp", 321);
		
		System.out.println("jinan changed >> " + jinanJson);
		System.out.println("cities changed ? >> " + citiesJsonArray); // true

		System.out.println();
		// JSONObject hezeJson = JSONObject.fromObject("{\"name\": \"heze\", \"gdp\": 789}");
		ObjectNode hezeJson = mapper.readValue("{\"name\": \"heze\", \"gdp\": 789}", ObjectNode.class);
		
		citiesJsonArray.add(hezeJson); // add 深拷贝（复制）
		System.out.println("cities after heze added >> " + citiesJsonArray);

		System.out.println();
		// hezeJson.element("gdp", 987);
		hezeJson.put("gdp", 987);
		
		System.out.println("heze updated >> " + hezeJson);
		System.out.println("cities after heze updated ? >> " + citiesJsonArray); // false

		// [{"name":"jinan","gdp":"321"},{"name":"qingdao","gdp":456},{"name":"heze","gdp":789}]
		// heze 数据依然是 789，并没有发生变化。这种特性跟JAVA对象是不一样的。
	}

}
