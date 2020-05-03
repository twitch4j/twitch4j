package com.github.twitch4j.common.util;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class ThreadUtils {
    private static ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    /**
     * A singular thread pool executor for all submodules
     * @return ScheduledThreadPoolExecutor
     */
    public static ScheduledThreadPoolExecutor getDefaultScheduledThreadPoolExecutor() {
        if(scheduledThreadPoolExecutor == null) {
            BasicThreadFactory threadFactory = new BasicThreadFactory.Builder()
                .namingPattern("twitch4j-%d")
                .daemon(false)
                .priority(Thread.NORM_PRIORITY)
                .build();

            scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors());
            scheduledThreadPoolExecutor.setThreadFactory(threadFactory);
            scheduledThreadPoolExecutor.setRemoveOnCancelPolicy(true);
            scheduledThreadPoolExecutor.setMaximumPoolSize(Runtime.getRuntime().availableProcessors() * 8);
        }
        return scheduledThreadPoolExecutor;
    }
}
