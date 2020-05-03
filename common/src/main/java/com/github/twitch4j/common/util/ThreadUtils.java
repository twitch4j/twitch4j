package com.github.twitch4j.common.util;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class ThreadUtils {
    private static ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    /**
     * A singular thread pool executor for all submodules
     * @return ScheduledThreadPoolExecutor
     */
    public static ScheduledThreadPoolExecutor getDefaultScheduledThreadPoolExecutor() {
        if(scheduledThreadPoolExecutor == null) {
            scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors());
            scheduledThreadPoolExecutor.setRemoveOnCancelPolicy(true);
            scheduledThreadPoolExecutor.setMaximumPoolSize(Runtime.getRuntime().availableProcessors() * 8);
        }
        return scheduledThreadPoolExecutor;
    }
}
