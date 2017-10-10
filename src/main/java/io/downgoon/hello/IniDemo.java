package io.downgoon.hello;

import java.util.Set;

import org.apache.shiro.config.Ini;

public class IniDemo {

	public static void main(String[] args) throws Exception {

		Ini ini = Ini.fromResourcePath("classpath:shiro.ini");
		Set<String> sections = ini.getSectionNames();
		for(String section : sections) {
			System.out.println("section: " + section);
		}
		
		String pwdRoles = ini.getSection("users").get("root");
		System.out.println("password and role of root: " + pwdRoles);
		
	}

}
