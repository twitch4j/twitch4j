package com.github.twitch4j.pubsub;

import com.github.philippheuer.events4j.core.EventManager;
import com.github.twitch4j.common.util.ThreadUtils;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Twitch PubSub Builder
 */
@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TwitchPubSubBuilder {

    /**
     * Event Manager
     */
    @With
    private EventManager eventManager = null;

    /**
     * Scheduler Thread Pool Executor
     */
    @With
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = null;

    /**
     * Initialize the builder
     * @return Twitch Chat Builder
     */
    public static TwitchPubSubBuilder builder() {
        return new TwitchPubSubBuilder();
    }

    /**
     * Twitch API Client (Helix)
     * @return TwitchHelix
     */
    public TwitchPubSub build() {
        log.debug("PubSub: Initializing Module ...");
        if(scheduledThreadPoolExecutor == null)
            scheduledThreadPoolExecutor = ThreadUtils.getDefaultScheduledThreadPoolExecutor();

        if(eventManager == null) {
            eventManager = new EventManager();
            eventManager.autoDiscovery();
        }

        return new TwitchPubSub(this.eventManager, scheduledThreadPoolExecutor);
    }

}
