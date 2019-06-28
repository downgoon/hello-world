package io.downgoon.hello;

public class EnvEcho {

	public static void main(String[] args) {
		System.out.println("EnvEcho http_proxy: " + System.getenv("http_proxy"));
	}

}
