package io.downgoon.hello;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.util.concurrent.locks.Lock;

public class DistributedLock {
    public static void main(String[] args) {
        Config config = new Config();
        HazelcastInstance h = Hazelcast.newHazelcastInstance(config);
        Lock lock = h.getLock("my-distributed-lock");
        lock.lock();
        try {
            //do something here
        } finally {
            lock.unlock();
        }
    }
}