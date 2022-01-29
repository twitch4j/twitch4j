package com.github.twitch4j.graphql;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.api.service.IEventHandler;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.common.config.ProxyConfig;
import com.github.twitch4j.common.util.EventManagerUtils;
import com.netflix.config.ConfigurationManager;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Twitch GraphQL Builder
 * <p>
 * This is an unofficial API that is not intended for third-party use. Use at your own risk. Methods could change or stop working at any time.
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
    private EventManager eventManager = null;

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
     * Default First-Party OAuth Token
     */
    @With
    private OAuth2Credential defaultFirstPartyToken = null;

    /**
     * Whether GraphQL Queries should be batched
     */
    @With
    private boolean enableBatching = false;

    /**
     * Default Timeout
     */
    @With
    private Integer timeout = 5000;

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
        log.warn("GraphQL: GraphQL is a experimental module not intended for third-party use, please take care as some features might break unannounced.");

        // Hystrix
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds", timeout);

        // GQL
        TwitchGraphQL client = new TwitchGraphQL(baseUrl, userAgent, eventManager, clientId, defaultFirstPartyToken, proxyConfig, enableBatching, timeout);

        // Initialize/Check EventManager
        eventManager = EventManagerUtils.validateOrInitializeEventManager(eventManager, defaultEventHandler);

        // register with serviceMediator
        this.eventManager.getServiceMediator().addService("twitch4j-graphql", client);

        return client;
    }
}
