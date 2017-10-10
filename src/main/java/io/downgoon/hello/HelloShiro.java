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

		Factory<SecurityManager> securityManagerFactory = new IniSecurityManagerFactory("classpath:shiro.ini");
		SecurityManager securityManager = securityManagerFactory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);

		Subject subject = SecurityUtils.getSubject();
		

		UsernamePasswordToken token = new UsernamePasswordToken("lonestarr", "vespa");

		subject.login(token);
		

		LOG.info("User [" + subject.getPrincipal() + "] logged in successfully.");
		

	}

}
