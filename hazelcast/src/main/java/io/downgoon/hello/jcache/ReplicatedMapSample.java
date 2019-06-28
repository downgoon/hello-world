package io.downgoon.hello.jcache;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ReplicatedMap;

public class ReplicatedMapSample {

    public static void main(String[] args) {
        HazelcastInstance h = Hazelcast.newHazelcastInstance();
        ReplicatedMap<String, String> map = h.getReplicatedMap("my-replicated-map");
        map.put("key", "value"); // key/value replicated to all members
        map.get("key"); // the value retrieved from local member
    }
}
