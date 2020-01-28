package com.github.twitch4j.graphql;

import com.github.philippheuer.events4j.core.EventManager;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
import lombok.extern.slf4j.Slf4j;

/**
 * Twitch GraphQL Builder
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TwitchGraphQLBuilder {

    /**
     * Event Manager
     */
    @Wither
    private EventManager eventManager = new EventManager();

    /**
     * Client Id
     */
    @Wither
    private String clientId = "kimne78kx3ncx6brgo4mv6wki5h1ko";

    /**
     * Client Secret
     */
    @Wither
    private String clientSecret = "**SECRET**";

    /**
     * User Agent
     */
    private String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36";

    /**
     * BaseUrl
     */
    private String baseUrl = "https://api.twitch.tv/gql";

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
        TwitchGraphQL client = new TwitchGraphQL(eventManager, clientId, clientSecret);

        // register with serviceMediator
        this.eventManager.getServiceMediator().addService("twitch4j-graphql", client);

        return client;
    }
}
