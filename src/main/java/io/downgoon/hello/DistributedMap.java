package io.downgoon.hello;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
 
import java.util.concurrent.ConcurrentMap;
 
public class DistributedMap {
    public static void main(String[] args) {
        Config config = new Config();
        HazelcastInstance h = Hazelcast.newHazelcastInstance(config);
        ConcurrentMap<String, String> map = h.getMap("my-distributed-map");
        map.put("key", "value");
        System.out.println(map.get("key"));
         
        // Concurrent Map methods
        map.putIfAbsent("somekey", "somevalue");
        map.replace("key", "value", "newvalue");
    }
}    