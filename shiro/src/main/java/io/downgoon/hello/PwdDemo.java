package io.downgoon.hello;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.util.SimpleByteSource;

public class PwdDemo {

	public static void main(String[] args) throws Exception {
		PasswordService pwdService = new DefaultPasswordService();
		String pwdCipher = pwdService.encryptPassword("abc");
		System.out.println("pwd cipher: " + pwdCipher);
		
		
		Sha256Hash sha256Hash = new Sha256Hash();

	}

}
