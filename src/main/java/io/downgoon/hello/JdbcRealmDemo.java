package io.downgoon.hello;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class JdbcRealmDemo {

	public static void main(String[] args) {

		System.out.println("Hello shiro!");

		// JDBC datasource
		MysqlDataSource mysqlDataSource = new MysqlDataSource();
		mysqlDataSource.setUser("root");
		mysqlDataSource.setPassword("123456");
		mysqlDataSource.setServerName("localhost");
		mysqlDataSource.setUrl("jdbc:mysql://localhost:3306/jdbc-realm");
		// datasource.setDriverClassName("com.mysql.jdbc.Driver");
		// datasource.setMaxActive(10);

		// JDBC realm
		org.apache.shiro.realm.jdbc.JdbcRealm jdbcRealm = new JdbcRealm();
		jdbcRealm.setDataSource(mysqlDataSource);
		jdbcRealm.setPermissionsLookupEnabled(true);
		jdbcRealm.setAuthenticationQuery("SELECT PASSWORD FROM account WHERE name = ?");
		jdbcRealm.setUserRolesQuery(
				"SELECT NAME FROM role WHERE id =(SELECT roleId FROM account_role WHERE userId = (SELECT id FROM account WHERE NAME = ?))");
		jdbcRealm.setPermissionsQuery(
				"SELECT NAME FROM permission WHERE id in (SELECT permissionId FROM permission_role WHERE (SELECT id FROM role WHERE NAME = ?))");

		// SecurityManager initiation
		DefaultSecurityManager securityManager = new DefaultSecurityManager(jdbcRealm);
		SecurityUtils.setSecurityManager(securityManager);

		// authentication & authorization subject
		Subject subject = SecurityUtils.getSubject();

		/*--------------------------------------------
		|               authentication               |
		============================================*/

		if (!subject.isAuthenticated()) {

			UsernamePasswordToken token = new UsernamePasswordToken("zhangsan", "zs1234");
			token.setRememberMe(true);
			try {
				subject.login(token);

				System.out.println("login successfully");

			} catch (UnknownAccountException uae) {

				System.out.println("There is no user with username of " + token.getPrincipal());

			} catch (IncorrectCredentialsException ice) {

				System.out.println("Password for account " + token.getPrincipal() + " was incorrect!");

			} catch (LockedAccountException lae) {

				System.out.println("The account for username " + token.getPrincipal() + " is locked.  " +

						"Please contact your administrator to unlock it.");

			}

			// ... catch more exceptions here (maybe custom ones specific to
			// your application?

			catch (AuthenticationException ae) {

				// unexpected condition? error?

			}

		}


		System.out.println("User [" + subject.getPrincipal() + "] logged in successfully.");

		/*--------------------------------------------
		|               authorization               |
		============================================*/

		// test a role:

		if (subject.hasRole("admin")) {
			System.out.println("hasRole: admin");
		} else {
			System.out.println("Hello, mere mortal.");
		}

		// test a typed permission (not instance-level)

		if (subject.isPermitted("write")) {
			System.out.println("You can write!");
		} else {

			System.out.println("Sorry, lightsaber rings are for schwartz masters only.");
		}

		// a (very powerful) Instance Level permission:

		if (subject.isPermitted("videos:download:demo")) {
			System.out.println("PERMITTED:  videos:download:demo");

		} else {
			System.out.println("NOT PERMITTED:  videos:download:demo");
		}

		// all done - log out!
		subject.logout();

	}

}