package com.github.twitch4j.pubsub;

import com.github.philippheuer.events4j.api.service.IEventHandler;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.client.websocket.WebsocketConnection;
import com.github.twitch4j.common.config.ProxyConfig;
import com.github.twitch4j.common.util.EventManagerUtils;
import com.github.twitch4j.common.util.ThreadUtils;
import com.github.twitch4j.util.IBackoffStrategy;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Collection;
import java.util.HashSet;
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
     * WebsocketConnection
     * <p>
     * can be used to inject a mocked connection into the TwitchChat instance
     */
    @With(AccessLevel.PROTECTED)
    private WebsocketConnection websocketConnection = null;

    /**
     * Event Manager
     */
    @With
    private EventManager eventManager = null;

    /**
     * EventManager
     */
    @With
    private Class<? extends IEventHandler> defaultEventHandler = SimpleEventHandler.class;

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
     * User IDs of Bot Owners for applying {@link com.github.twitch4j.common.enums.CommandPermission#OWNER}
     */
    @Setter
    @Accessors(chain = true)
    private Collection<String> botOwnerIds = new HashSet<>();

    /**
     * WebSocket RFC Ping Period in ms (0 = disabled)
     */
    @With
    private int wsPingPeriod = 15_000;

    /**
     * WebSocket Connection Backoff Strategy
     */
    @With
    private IBackoffStrategy connectionBackoffStrategy = null;

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

        // Initialize/Check EventManager
        eventManager = EventManagerUtils.validateOrInitializeEventManager(eventManager, defaultEventHandler);

        return new TwitchPubSub(this.websocketConnection, this.eventManager, scheduledThreadPoolExecutor, this.proxyConfig, this.botOwnerIds, this.wsPingPeriod, this.connectionBackoffStrategy);
    }

    /**
     * With a Bot Owner's User ID
     *
     * @param userId the user id
     * @return TwitchPubSubBuilder
     */
    public TwitchPubSubBuilder withBotOwnerId(String userId) {
        this.botOwnerIds.add(userId);
        return this;
    }

    /**
     * With multiple Bot Owner User IDs
     *
     * @param botOwnerIds the user ids
     * @return TwitchPubSubBuilder
     */
    public TwitchPubSubBuilder withBotOwnerIds(Collection<String> botOwnerIds) {
        this.botOwnerIds.addAll(botOwnerIds);
        return this;
    }
}
