package com.github.twitch4j.common.util;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class IncrementalReusableIdProvider {
    private final AtomicInteger counter = new AtomicInteger(0);
    private final Queue<String> released = new ConcurrentLinkedQueue<>();

    public String get() {
        String reused = released.poll();
        if (reused != null) {
            return reused;
        }

        return String.valueOf(counter.getAndIncrement());
    }

    public void release(String id) {
        released.offer(id);
    }
}
