package io.downgoon.hello;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;

public class ByteBuddyHello {

	public Class<?> helloClass() {

		Class<?> dynamicType = 
				new ByteBuddy()
					.subclass(Object.class)
					.name("io.downgoon.dynamic.HelloWorld")
					.method(ElementMatchers.named("toString"))
					.intercept(FixedValue.value("Hello World!"))
					.make()
					.load(getClass().getClassLoader())
					.getLoaded();

		return dynamicType;

	}

	public static void main(String[] args) throws Exception {

		ByteBuddyHello buddyHello = new ByteBuddyHello();
		Class<?> dClass = buddyHello.helloClass();
		
		System.out.println(dClass.getName());
		System.out.println(dClass.newInstance().toString());

	}

}
