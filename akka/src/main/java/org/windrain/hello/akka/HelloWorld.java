package org.windrain.hello.akka;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

/**
 * Hello world!
 *
 */
public class HelloWorld extends UntypedActor {
	// REFER-1: AKKA 计算模型 http://www.blogbus.com/dreamhead-logs/235916459.html
	// REFER-2: 并发编程网介绍AKKA  http://ifeve.com/akka-doc-java/
	// REFER-3: http://doc.akka.io/docs/akka/2.0.2/intro/getting-started-first-java.html
	
	@Override
	public void preStart() {
		final ActorRef greeter = getContext().actorOf(Props.create(Greeter.class), "greeter");
		greeter.tell(Greeter.Msg.GREET, getSelf());
	}

	@Override
	public void onReceive(Object msg) throws Exception {
		if (msg == Greeter.Msg.DONE) {
			getContext().stop(getSelf());
		} else {
			unhandled(msg);
		}
	}
}
