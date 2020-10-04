package com.github.twitch4j.chat;

import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.CredentialManagerBuilder;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.api.service.IEventHandler;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.common.config.ProxyConfig;
import com.github.twitch4j.common.config.Twitch4JGlobal;
import com.github.twitch4j.common.util.EventManagerUtils;
import com.github.twitch4j.common.util.ThreadUtils;
import io.github.bucket4j.Bandwidth;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Twitch Chat
 * <p>
 * Documentation: https://dev.twitch.tv/docs/irc
 */
@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TwitchChatBuilder {

    /**
     * Client Id
     */
    @With
    private String clientId = Twitch4JGlobal.clientId;

    /**
     * Client Secret
     */
    @With
    private String clientSecret = Twitch4JGlobal.clientSecret;

    /**
     * HTTP Request Queue Size
     */
    @With
    private Integer requestQueueSize = -1;

    /**
     * Event Manager
     */
    @With
    private EventManager eventManager;

    /**
     * EventManager
     */
    @With
    private Class<? extends IEventHandler> defaultEventHandler = SimpleEventHandler.class;

    /**
     * Credential Manager
     */
    @With
    private CredentialManager credentialManager = CredentialManagerBuilder.builder().build();

    /**
     * IRC User Id
     */
    @With
    private OAuth2Credential chatAccount;

    /**
     * A custom websocket url for {@link TwitchChat} to connect to.
     * Must include the scheme (e.g. ws:// or wss://).
     */
    @With
    private String baseUrl = TwitchChat.TWITCH_WEB_SOCKET_SERVER;

    /**
     * Whether the {@link OAuth2Credential} password should be sent when the baseUrl does not
     * match the official twitch websocket server, thus bypassing a security check in the library.
     * <p>
     * Do not depart from the default false value unless you understand the consequences.
     */
    @With
    private boolean sendCredentialToThirdPartyHost = false;

    /**
     * User IDs of Bot Owners for applying {@link com.github.twitch4j.common.enums.CommandPermission#OWNER}
     */
    @Setter
    @Accessors(chain = true)
    protected Collection<String> botOwnerIds = new HashSet<>();

    /**
     * IRC Command Handlers
     */
    protected final List<String> commandPrefixes = new ArrayList<>();

    /**
     * Size of the ChatQueue
     */
    @With
    protected Integer chatQueueSize = 200;

    /**
     * Custom RateLimit for ChatMessages
     */
    @With
    protected Bandwidth chatRateLimit = Bandwidth.simple(20, Duration.ofSeconds(30));

    /**
     * Custom RateLimit for Whispers
     */
    @With
    protected Bandwidth[] whisperRateLimit = { Bandwidth.simple(100, Duration.ofSeconds(60)), Bandwidth.simple(3, Duration.ofSeconds(1)) };

    /**
     * Scheduler Thread Pool Executor
     */
    @With
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = null;

    /**
     * Millisecond wait time for taking items off chat queue. Default recommended
     */
    @With
    private long chatQueueTimeout = 1000L;

    /**
     * Proxy Configuraiton
     */
    @With
    private ProxyConfig proxyConfig = null;

    /**
     * Initialize the builder
     *
     * @return Twitch Chat Builder
     */
    public static TwitchChatBuilder builder() {
        return new TwitchChatBuilder();
    }

    /**
     * Twitch API Client (Helix)
     *
     * @return TwitchHelix
     */
    public TwitchChat build() {
        log.debug("TwitchChat: Initializing ErrorTracking ...");

        if (scheduledThreadPoolExecutor == null)
            scheduledThreadPoolExecutor = ThreadUtils.getDefaultScheduledThreadPoolExecutor("twitch4j-chat-"+ RandomStringUtils.random(4, true, true), TwitchChat.REQUIRED_THREAD_COUNT);

        // Initialize/Check EventManager
        eventManager = EventManagerUtils.validateOrInitializeEventManager(eventManager, defaultEventHandler);

        log.debug("TwitchChat: Initializing Module ...");
        return new TwitchChat(this.eventManager, this.credentialManager, this.chatAccount, this.baseUrl, this.sendCredentialToThirdPartyHost, this.commandPrefixes, this.chatQueueSize, this.chatRateLimit, this.whisperRateLimit, this.scheduledThreadPoolExecutor, this.chatQueueTimeout, this.proxyConfig, this.botOwnerIds);
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

    /**
     * With a Bot Owner's User ID
     *
     * @param userId the user id
     * @return TwitchChatBuilder
     */
    public TwitchChatBuilder withBotOwnerId(String userId) {
        this.botOwnerIds.add(userId);
        return this;
    }

    /**
     * With multiple Bot Owner User IDs
     *
     * @param botOwnerIds the user ids
     * @return TwitchChatBuilder
     */
    public TwitchChatBuilder withBotOwnerIds(Collection<String> botOwnerIds) {
        this.botOwnerIds.addAll(botOwnerIds);
        return this;
    }
}
