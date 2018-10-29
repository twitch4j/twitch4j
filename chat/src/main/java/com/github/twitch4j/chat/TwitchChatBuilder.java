package com.github.twitch4j.chat;

import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.CredentialManagerBuilder;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.EventManager;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
import lombok.extern.slf4j.Slf4j;

/**
 * Twitch Chat
 *
 * Documentation: https://dev.twitch.tv/docs/irc
 */
@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TwitchChatBuilder {

    /**
     * Event Manager
     */
    @Wither
    private EventManager eventManager = new EventManager();

    /**
     * Credential Manager
     */
    @Wither
    private CredentialManager credentialManager = CredentialManagerBuilder.builder().build();

    /**
     * IRC User Id
     */
    @Wither
    private OAuth2Credential chatAccount;

    /**
     * Enable Channel Cache to keep track of mods/timeouts/bans/
     */
    @Wither
    private Boolean enableChannelCache = false;

    /**
     * BaseUrl
     */
    private String baseUrl = "https://api.twitch.tv/helix";

    /**
     * Initialize the builder
     * @return Twitch Chat Builder
     */
    public static TwitchChatBuilder builder() {
        return new TwitchChatBuilder();
    }

    /**
     * Twitch API Client (Helix)
     * @return TwitchHelix
     */
    public TwitchChat build() {
        log.debug("TwitchChat: Initializing Module ...");
        TwitchChat twitchChat = new TwitchChat(this.eventManager, this.credentialManager, this.chatAccount, this.enableChannelCache);
        return twitchChat;
    }
}
