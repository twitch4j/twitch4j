package com.github.twitch4j.pubsub;

import com.github.philippheuer.events4j.core.EventManager;
import com.github.twitch4j.common.config.ProxyConfig;
import com.github.twitch4j.common.util.ThreadUtils;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

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
     * Proxy Configuration
     */
    @With
    private ProxyConfig proxyConfig = null;

    /**
     * Initialize the builder
     *
     * @return Twitch PubSub Builder
     */
    public static TwitchPubSubBuilder builder() {
        return new TwitchPubSubBuilder();
    }

    /**
     * Twitch PubSub Client
     *
     * @return TwitchPubSub
     */
    public TwitchPubSub build() {
        log.debug("PubSub: Initializing Module ...");
        if (scheduledThreadPoolExecutor == null)
            scheduledThreadPoolExecutor = ThreadUtils.getDefaultScheduledThreadPoolExecutor("twitch4j-pubsub-" + RandomStringUtils.random(4, true, true), TwitchPubSub.REQUIRED_THREAD_COUNT);

        if (eventManager == null) {
            eventManager = new EventManager();
            eventManager.autoDiscovery();
        }

        return new TwitchPubSub(this.eventManager, scheduledThreadPoolExecutor, this.proxyConfig);
    }

}
