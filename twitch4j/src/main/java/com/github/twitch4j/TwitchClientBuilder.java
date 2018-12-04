package com.github.twitch4j;

import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.CredentialManagerBuilder;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.EventManager;
import kraken.TwitchKraken;
import kraken.TwitchKrakenBuilder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
import lombok.extern.slf4j.Slf4j;
import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.chat.TwitchChatBuilder;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.TwitchHelixBuilder;

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
     * Enabled: Chat
     */
    @Wither
    private Boolean enableChat = false;

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

        // Module: Chat
        TwitchChat chat = null;
        if (this.enableChat) {
            chat = TwitchChatBuilder.builder()
                .withEventManager(eventManager)
                .withCredentialManager(credentialManager)
                .withChatAccount(chatAccount)
                .withEnableChannelCache(enableChannelCache)
                .build();
        }

        // Module: Client
        final TwitchClient client = new TwitchClient(eventManager, helix, kraken, chat);

        // Return new Client Instance
        return client;
    }

}
