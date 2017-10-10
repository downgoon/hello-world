package io.downgoon.hello;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;

public class PwdDemo {

	public static void main(String[] args) throws Exception {
		PasswordService pwdService = new DefaultPasswordService();
		String pwdCipher = pwdService.encryptPassword("abc");
		System.out.println("pwd cipher: " + pwdCipher);
	}

}
