package io.downgoon.hello;

import org.apache.shiro.codec.Hex;

public class CodecDemo {

	public static void main(String[] args) throws Exception {
		// String encodeMe = "Hello, I'm a text.";
		//
		// byte[] bytes = CodecSupport.toBytes(encodeMe);
		//
		// String decoded = CodecSupport.toString(bytes);
		// System.out.println(decoded);

		byte[] encodeMe = { 2, 4, 6, 8, 10, 12, 14, 16, 18, 20 };

		String hexadecimal = Hex.encodeToString(encodeMe);

		System.out.println(hexadecimal);
	}

}
