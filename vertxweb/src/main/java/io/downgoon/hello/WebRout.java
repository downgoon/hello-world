package io.downgoon.hello;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 * Hello world!
 */
public class WebRout {
	public static void main(String[] args) throws Exception {

		Vertx vertx = Vertx.vertx();

		HttpServer server = vertx.createHttpServer();

		// why NOT vertx.createRouter()
		Router router = Router.router(vertx);

		/*
		 * like nginx 'location' 'location {
		 * 
		 * }'
		 */
		router.route("/hello.json").handler(new Handler<RoutingContext>() {

			@Override
			public void handle(RoutingContext event) {

				event.response().putHeader("Content-Type", "application/json;charset=UTF-8").putHeader("Server",
						"autorest4db");

				JsonObject json = new JsonObject();
				json.put("name", "陈六子");
				json.put("age", 42);
				json.put("id", 2);
				
				event.response().end(json.toString());
			}
		});

		// HttpServer & Router binding
		server.requestHandler(router::accept);

		int port = 8080;
		server.listen(port);
		System.out.println("listening on " + port);
	}
}
