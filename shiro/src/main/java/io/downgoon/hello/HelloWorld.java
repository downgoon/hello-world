package io.downgoon.hello;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

public class HelloWorld {

	public static void main(String[] args) throws Exception {

		// load configuration and create SecurityManager
		Factory<SecurityManager> securityManagerFactory = new IniSecurityManagerFactory("classpath:hello-world.ini");
		SecurityManager securityManager = securityManagerFactory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);

		Subject subject = SecurityUtils.getSubject();

		// login as vip Role

		subject.login(new UsernamePasswordToken("zhangsan", "zs1234"));

		// Role checking

		if (subject.hasRole("vip")) {
			System.out.println("has role vip");
		} else {
			System.out.println("no role vip");
		}

		// Permission checking

		System.out.println(String.format("%s access %s: %s", subject.getPrincipal(), "videos:upload",
				subject.isPermitted("videos:upload")));

		System.out.println(String.format("%s access %s: %s", subject.getPrincipal(), "videos:download",
				subject.isPermitted("videos:download")));

		System.out.println(
				String.format("%s access %s: %s", subject.getPrincipal(), "videos:*", subject.isPermitted("videos:*")));

		System.out.println(String.format("%s access %s: %s", subject.getPrincipal(), "printer:print",
				subject.isPermitted("printer:print")));
		System.out.println(String.format("%s access %s: %s", subject.getPrincipal(), "printer:query",
				subject.isPermitted("printer:query")));

		// BUG on http://shiro.apache.org/permissions.html#multiple-values
		System.out.println(
				String.format("%s access %s: %s", subject.getPrincipal(), "query", subject.isPermitted("query")));

	}

}
