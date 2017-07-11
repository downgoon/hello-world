package io.downgoon.hello;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class DistributedQueue {
    public static void main(String[] args) throws InterruptedException {
        Config config = new Config();
        HazelcastInstance h = Hazelcast.newHazelcastInstance(config);
        
        BlockingQueue<String> queue = h.getQueue("my-distributed-queue");
        
        
        queue.offer("item");
        String item = queue.poll();

        // Timed blocking Operations
        queue.offer("anotheritem", 500, TimeUnit.MILLISECONDS);
        String anotherItem = queue.poll(5, TimeUnit.SECONDS);

        //Indefinitely blocking Operations
        queue.put("yetanotheritem");
        String yetanother = queue.take();
    }
}     
