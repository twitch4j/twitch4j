package com.github.twitch4j;

import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.CredentialManagerBuilder;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.graphql.TwitchGraphQL;
import com.github.twitch4j.graphql.TwitchGraphQLBuilder;
import com.github.twitch4j.pubsub.TwitchPubSub;
import com.github.twitch4j.pubsub.TwitchPubSubBuilder;
import com.github.twitch4j.kraken.TwitchKraken;
import com.github.twitch4j.kraken.TwitchKrakenBuilder;
import com.github.twitch4j.tmi.TwitchMessagingInterface;
import com.github.twitch4j.tmi.TwitchMessagingInterfaceBuilder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
import lombok.extern.slf4j.Slf4j;
import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.chat.TwitchChatBuilder;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.TwitchHelixBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder to get a TwitchClient Instance by provided various options, to provide the user with a lot of customizable options.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TwitchClientBuilder {

    /**
     * Client ID
     */
    @Wither
    private String clientId;

    /**
     * Client Secret
     */
    @Wither
    private String clientSecret;

    /**
     * Enabled: Helix
     */
    @Wither
    private Boolean enableHelix = false;

    /**
     * Enabled: Kraken
     */
    @Wither
    private Boolean enableKraken = false;

    /**
     * Enabled: TMI
     */
    @Wither
    private Boolean enableTMI = false;

    /**
     * Enabled: Chat
     */
    @Wither
    private Boolean enableChat = false;

    /**
     * IRC Command Handlers
     */
    protected final List<String> commandPrefixes = new ArrayList<>();

    /**
     * Enabled: GraphQL
     */
    @Wither
    private Boolean enableGraphQL = false;

    /**
     * Chat Account
     */
    @Wither
    private OAuth2Credential chatAccount;

    /**
     * Channel Cache
     */
    @Wither
    private Boolean enableChannelCache = false;

    /**
     * EventManager
     */
    @Wither
    private EventManager eventManager = new EventManager();

    /**
     * EventManager
     */
    @Wither
    private CredentialManager credentialManager = CredentialManagerBuilder.builder().build();

    /**
     * With a CommandTrigger
     *
     * @param commandTrigger Command Trigger (Prefix)
     * @return TwitchClientBuilder
     */
    public TwitchClientBuilder withCommandTrigger(String commandTrigger) {
        this.commandPrefixes.add(commandTrigger);
        return this;
    }

    /**
     * Initialize the builder
     *
     * @return Twitch Client Builder
     */
    public static TwitchClientBuilder builder() {
        return new TwitchClientBuilder();
    }

    /**
     * Initialize
     *
     * @return {@link TwitchClient} initialized class
     */
    public TwitchClient build() {
        // Client Id / Client Secret
        if (this.clientId == null || this.clientSecret == null) {
            log.warn("You have not provided the required Client-Id/Client-Secret to use the rest api, defaulting to the twitch official id. You may not be able to use some features, please check out the docs on how to use Twitch4J!");
            this.clientId = "jzkbprff40iqj646a697cyrvl0zt2m6";
            this.clientSecret = "**secret**";
        }

        // Module: Helix
        TwitchHelix helix = null;
        if (this.enableHelix) {
            helix = TwitchHelixBuilder.builder()
                .withClientId(this.clientId)
                .withClientSecret(this.clientSecret)
                .withEventManager(eventManager)
                .build();
        }

        // Module: Kraken
        TwitchKraken kraken = null;
        if (this.enableKraken) {
            kraken = TwitchKrakenBuilder.builder()
                .withClientId(this.clientId)
                .withClientSecret(this.clientSecret)
                .withEventManager(eventManager)
                .build();
        }

        // Module: TMI
        TwitchMessagingInterface tmi = null;
        if (this.enableTMI) {
            tmi = TwitchMessagingInterfaceBuilder.builder()
                .withClientId(this.clientId)
                .withClientSecret(this.clientSecret)
                .withEventManager(eventManager)
                .build();
        }

        // Module: Chat
        TwitchChat chat = null;
        if (this.enableChat) {
            chat = TwitchChatBuilder.builder()
                .withEventManager(eventManager)
                .withCredentialManager(credentialManager)
                .withChatAccount(chatAccount)
                .withEnableChannelCache(enableChannelCache)
                .withCommandTriggers(commandPrefixes)
                .build();
        }

        // Module: PubSub
        TwitchPubSub pubsub = null;
        if (this.enableGraphQL) {
            pubsub = TwitchPubSubBuilder.builder()
                .withEventManager(eventManager)
                .build();
        }

        // Module: GraphQL
        TwitchGraphQL graphql = null;
        if (this.enableGraphQL) {
            graphql = TwitchGraphQLBuilder.builder()
                .withEventManager(eventManager)
                .withClientId(clientId)
                .withClientSecret(clientSecret)
                .build();
        }

        // Module: Client
        final TwitchClient client = new TwitchClient(eventManager, helix, kraken, tmi, chat, pubsub, graphql);

        // Return new Client Instance
        return client;
    }

}
