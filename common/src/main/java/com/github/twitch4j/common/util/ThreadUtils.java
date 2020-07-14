package com.github.twitch4j.common.util;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class ThreadUtils {

    /**
     * The default thread pool executor used in twitch4j
     * <p>
     * PubSub: 2 Thread(s)
     * Chat: 1 Thread(s)
     * ClientHelper: 2 Thread(s)
     *
     * @return ScheduledThreadPoolExecutor
     */
    public static ScheduledThreadPoolExecutor getDefaultScheduledThreadPoolExecutor(String namePrefix, Integer poolSize) {
        BasicThreadFactory threadFactory = new BasicThreadFactory.Builder()
            .namingPattern(namePrefix+"-%d")
            .daemon(false)
            .priority(Thread.NORM_PRIORITY)
            .build();

        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(poolSize);
        scheduledThreadPoolExecutor.setThreadFactory(threadFactory);
        scheduledThreadPoolExecutor.setRemoveOnCancelPolicy(true);

        return scheduledThreadPoolExecutor;
    }

}
