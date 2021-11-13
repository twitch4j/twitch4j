package com.github.twitch4j.graphql;

import com.github.philippheuer.events4j.api.service.IEventHandler;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.common.config.ProxyConfig;
import com.github.twitch4j.common.util.EventManagerUtils;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Twitch GraphQL Builder
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Unofficial
public class TwitchGraphQLBuilder {

    /**
     * Event Manager
     */
    @With
    private EventManager eventManager = new EventManager();

    /**
     * EventManager
     */
    @With
    private Class<? extends IEventHandler> defaultEventHandler = SimpleEventHandler.class;

    /**
     * Proxy Configuration
     */
    @With
    private ProxyConfig proxyConfig = null;

    /**
     * Client Id
     */
    @With
    private String clientId = "kimne78kx3ncx6brgo4mv6wki5h1ko";

    /**
     * Client Secret
     */
    @With(onMethod_ = { @Deprecated })
    private String clientSecret = "**SECRET**";

    /**
     * Whether GraphQL Queries should be batched
     */
    @With
    private boolean enableBatching = false;

    /**
     * User Agent
     */
    private String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36";

    /**
     * BaseUrl
     */
    private String baseUrl = "https://gql.twitch.tv/gql";

    /**
     * Initialize the builder
     *
     * @return Twitch GraphQL Builder
     */
    public static TwitchGraphQLBuilder builder() {
        return new TwitchGraphQLBuilder();
    }

    /**
     * Twitch API Client (GraphQL)
     *
     * @return TwitchGraphQL
     */
    public TwitchGraphQL build() {
        log.debug("GraphQL: Initializing Module ...");
        log.warn("GraphQL: GraphQL is a experimental module, please take care as some features might break unannounced.");
        TwitchGraphQL client = new TwitchGraphQL(baseUrl, userAgent, eventManager, clientId, proxyConfig, enableBatching);

        // Initialize/Check EventManager
        eventManager = EventManagerUtils.validateOrInitializeEventManager(eventManager, defaultEventHandler);

        // register with serviceMediator
        this.eventManager.getServiceMediator().addService("twitch4j-graphql", client);

        return client;
    }
}
