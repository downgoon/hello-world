package io.downgoon.hello;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class EnvSub {

	public static void main(String[] args) throws Exception {
		System.out.println("main http_proxy: " + System.getenv("http_proxy"));
		
		Process sub = Runtime.getRuntime().exec("java EnvEcho");
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(sub.getInputStream()));
		
		String line = "";
		while ((line = reader.readLine()) != null) {
			System.out.println("\tsub: "+ line);
		}
		
	}

}
