package com.github.twitch4j.chat;

import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.CredentialManagerBuilder;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.twitch4j.common.builder.TwitchEventAwareAPIBuilder;
import io.github.bucket4j.Bandwidth;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Twitch Chat
 *
 * Documentation: https://dev.twitch.tv/docs/irc
 */
@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TwitchChatBuilder extends TwitchEventAwareAPIBuilder<TwitchChatBuilder> {

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
     * IRC Command Handlers
     */
    protected final List<String> commandPrefixes = new ArrayList<>();

    /**
     * Size of the ChatQueue
     */
    @Wither
    protected Integer chatQueueSize = 200;

    /**
     * Custom RateLimit for ChatMessages
     */
    @Wither
    protected Bandwidth chatRateLimit = Bandwidth.simple(20, Duration.ofSeconds(30));

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
        log.debug("TwitchChat: Initializing ErrorTracking ...");

        log.debug("TwitchChat: Initializing Module ...");
        TwitchChat twitchChat = new TwitchChat(this.eventManager, this.credentialManager, this.chatAccount, this.enableChannelCache, this.commandPrefixes, this.chatQueueSize, this.chatRateLimit);

        return twitchChat;
    }

    /**
     * With a CommandTrigger
     *
     * @param commandTrigger Command Trigger (Prefix)
     * @return TwitchChatBuilder
     */
    public TwitchChatBuilder withCommandTrigger(String commandTrigger) {
        this.commandPrefixes.add(commandTrigger);
        return this;
    }

    /**
     * With multiple CommandTriggers
     *
     * @param commandTrigger Command Trigger (Prefix)
     * @return TwitchChatBuilder
     */
    public TwitchChatBuilder withCommandTriggers(Collection<String> commandTrigger) {
        this.commandPrefixes.addAll(commandTrigger);
        return this;
    }
}
