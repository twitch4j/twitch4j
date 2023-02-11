package com.github.twitch4j.common.util;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntSupplier;

public class IncrementalReusableIdProvider implements IntSupplier {
    private final AtomicInteger counter = new AtomicInteger(0);
    private final Queue<Integer> released = new ConcurrentLinkedQueue<>();

    @Override
    public int getAsInt() {
        Integer reused = released.poll();
        if (reused != null)
            return reused;

        return counter.incrementAndGet();
    }

    public void release(Integer i) {
        released.offer(i);
    }
}
