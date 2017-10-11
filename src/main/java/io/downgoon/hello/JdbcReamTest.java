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

public class JdbcReamTest {

	public static void main(String[] args) {

		System.out.println("Hello shiro!");

		MysqlDataSource datasource = new MysqlDataSource();

		datasource.setUser("root");

		datasource.setPassword("123456");

		datasource.setServerName("localhost");

		// datasource.setDriverClassName("com.mysql.jdbc.Driver");

		datasource.setUrl("jdbc:mysql://localhost:3306/jdbc-realm");

		// datasource.setMaxActive(10);

		org.apache.shiro.realm.jdbc.JdbcRealm jdbcRealm = new JdbcRealm();

		jdbcRealm.setDataSource(datasource);

		jdbcRealm.setPermissionsLookupEnabled(true);

		jdbcRealm
				.setAuthenticationQuery("SELECT PASSWORD FROM account WHERE name = ?");

		jdbcRealm
				.setUserRolesQuery("SELECT NAME FROM role WHERE id =(SELECT roleId FROM account_role WHERE userId = (SELECT id FROM account WHERE NAME = ?))");

		jdbcRealm
				.setPermissionsQuery("SELECT NAME FROM permission WHERE id in (SELECT permissionId FROM permission_role WHERE (SELECT id FROM role WHERE NAME = ?))");

		DefaultSecurityManager security = new DefaultSecurityManager(jdbcRealm);

		SecurityUtils.setSecurityManager(security);
		Subject currentUser = SecurityUtils.getSubject();
		
		
		if (!currentUser.isAuthenticated()) {

			UsernamePasswordToken token = new UsernamePasswordToken("zhangsan",
					"zs1234");

			token.setRememberMe(true);
			try {
				currentUser.login(token);

				System.out.println("login successfully");

			} catch (UnknownAccountException uae) {

				System.out.println("There is no user with username of "
						+ token.getPrincipal());

			} catch (IncorrectCredentialsException ice) {

				System.out.println("Password for account "
						+ token.getPrincipal() + " was incorrect!");

			} catch (LockedAccountException lae) {

				System.out.println("The account for username "
						+ token.getPrincipal() + " is locked.  " +

						"Please contact your administrator to unlock it.");

			}

			// ... catch more exceptions here (maybe custom ones specific to
			// your application?

			catch (AuthenticationException ae) {

				// unexpected condition? error?

			}

		}

		// say who they are:

		// print their identifying principal (in this case, a username):

		System.out.println("User [" + currentUser.getPrincipal()
				+ "] logged in successfully.");

		// test a role:

		if (currentUser.hasRole("admin")) {

			System.out.println("May the admin be with you!");

		} else {

			System.out.println("Hello, mere mortal.");

		}

		// test a typed permission (not instance-level)

		if (currentUser.isPermitted("write")) {
			System.out.println("You can write!.");
		} else {

			System.out.println("Sorry, lightsaber rings are for schwartz masters only.");
		}

		// a (very powerful) Instance Level permission:

		if (currentUser.isPermitted("winnebago:drive:eagle5")) {

			System.out
					.println("You are permitted to 'drive' the winnebago with license plate (id) 'eagle5'.  "
							+

							"Here are the keys - have fun!");

		} else {

			System.out
					.println("Sorry, you aren't allowed to drive the 'eagle5' winnebago!");

		}

		// all done - log out!

		currentUser.logout();

	}

}