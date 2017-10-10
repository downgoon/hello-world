package io.downgoon.hello;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

public class MultiValues {

	public static void main(String[] args) throws Exception {

		Factory<SecurityManager> securityManagerFactory = new IniSecurityManagerFactory("classpath:multi-values.ini");
		SecurityManager securityManager = securityManagerFactory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);

		Subject subject = SecurityUtils.getSubject();

		subject.login(new UsernamePasswordToken("zhangsan", "zs1234"));

		System.out.println(String.format("%s access %s: %s", subject.getPrincipal(), "printer:print",
				subject.isPermitted("printer:print")));
		System.out.println(String.format("%s access %s: %s", subject.getPrincipal(), "printer:query",
				subject.isPermitted("printer:query")));

	}

}
