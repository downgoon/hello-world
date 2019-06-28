package io.downgoon.hello.spring.redis;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class RedisCacheExample {

	public static void main(String[] args) throws Exception {

        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        MusicService musicService = ctx.getBean(MusicService.class);

        System.out.println("message: " + musicService.play("trumpet"));
        System.out.println("message: " + musicService.play("trumpet"));
        System.out.println("message: " + musicService.play("guitar"));
        System.out.println("message: " + musicService.play("guitar"));

        System.out.println("Done.");
    }
	
}
