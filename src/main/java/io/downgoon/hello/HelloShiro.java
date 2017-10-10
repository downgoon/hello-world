package io.downgoon.hello;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloShiro {

	private static final transient Logger LOG = LoggerFactory.getLogger(HelloShiro.class);

	public static void main(String[] args) throws Exception {

		Factory<SecurityManager> securityManagerFactory = new IniSecurityManagerFactory("classpath:hello-world.ini");
		SecurityManager securityManager = securityManagerFactory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);

		Subject subject = SecurityUtils.getSubject();

		// if login fail, AuthenticationException thrown
		UsernamePasswordToken token = new UsernamePasswordToken("root", "yYMWOIx3");
		subject.login(token);

		LOG.info("User [" + subject.getPrincipal() + "] logged in successfully.");

		String action = "videos:download";
		if (subject.isPermitted(action)) {
			System.out.println(String.format("%s access %s: %s", subject.getPrincipal(), action, true));
		} else {
			System.out.println(String.format("%s access %s: %s", subject.getPrincipal(), action, false));
		}

		subject.logout();

		// login as guest
		subject.login(new UsernamePasswordToken("guest", "123456"));

		System.out.println(String.format("%s access %s: %s", subject.getPrincipal(), "videos:download",
				subject.isPermitted("videos:download")));
		
		System.out.println(String.format("%s access %s: %s", subject.getPrincipal(), "videos:download:demo",
				subject.isPermitted("videos:download:demo")));

		subject.logout();
		
		// login as vip
		
		subject.login(new UsernamePasswordToken("zhangsan", "zs1234") );
		
		if (subject.hasRole("vip")) {
			System.out.println("has role vip");
		} else {
			System.out.println("no role vip");
		}
		
		System.out.println(String.format("%s access %s: %s", subject.getPrincipal(), "videos:*",
				subject.isPermitted("videos:*")));
		
		System.out.println(String.format("%s access %s: %s", subject.getPrincipal(), "videos:upload",
				subject.isPermitted("videos:upload")));
		
		System.out.println(String.format("%s access %s: %s", subject.getPrincipal(), "videos:download",
				subject.isPermitted("videos:download")));
		
		System.out.println(String.format("%s access %s: %s", subject.getPrincipal(), "printer:print",
				subject.isPermitted("printer:print")));
		System.out.println(String.format("%s access %s: %s", subject.getPrincipal(), "printer:query",
				subject.isPermitted("printer:query")));
		
	}

}
