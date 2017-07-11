package io.downgoon.hello;

import com.hazelcast.config.Config;
import com.hazelcast.core.*;

public class DistributedTopic implements MessageListener<String> {
    public static void main(String[] args) {
        Config config = new Config();
        HazelcastInstance h = Hazelcast.newHazelcastInstance(config);
        ITopic<String> topic = h.getTopic("my-distributed-topic");
        topic.addMessageListener(new DistributedTopic());
        topic.publish("Hello to distributed world");
    }

    @Override
    public void onMessage(Message<String> message) {
        System.out.println("Got message " + message.getMessageObject());
    }
}
