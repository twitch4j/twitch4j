package com.github.twitch4j;

import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.CredentialManagerBuilder;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.twitch4j.auth.TwitchAuth;
import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.chat.TwitchChatBuilder;
import com.github.twitch4j.common.builder.TwitchAPIBuilder;
import com.github.twitch4j.graphql.TwitchGraphQL;
import com.github.twitch4j.graphql.TwitchGraphQLBuilder;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.TwitchHelixBuilder;
import com.github.twitch4j.kraken.TwitchKraken;
import com.github.twitch4j.kraken.TwitchKrakenBuilder;
import com.github.twitch4j.pubsub.TwitchPubSub;
import com.github.twitch4j.pubsub.TwitchPubSubBuilder;
import com.github.twitch4j.tmi.TwitchMessagingInterface;
import com.github.twitch4j.tmi.TwitchMessagingInterfaceBuilder;
import io.github.bucket4j.Bandwidth;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Builder to get a TwitchClient Instance by provided various options, to provide the user with a lot of customizable options.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TwitchClientBuilder extends TwitchAPIBuilder<TwitchClientBuilder> {

    /**
     * Redirect Url
     */
    @With
    private String redirectUrl = "http://localhost";

    /**
     * Default Timeout
     */
    @With
    private Integer timeout = 5000;

    /**
     * Enabled: Helix
     */
    @With
    private Boolean enableHelix = false;

    /**
     * Enabled: Kraken
     */
    @With
    private Boolean enableKraken = false;

    /**
     * Enabled: TMI
     */
    @With
    private Boolean enableTMI = false;

    /**
     * Enabled: Chat
     */
    @With
    private Boolean enableChat = false;

    /**
     * IRC Command Handlers
     */
    protected final List<String> commandPrefixes = new ArrayList<>();

    /**
     * Enabled: PubSub
     */
    @With
    private Boolean enablePubSub = false;

    /**
     * Enabled: GraphQL
     */
    @With
    private Boolean enableGraphQL = false;

    /**
     * Chat Account
     */
    @With
    private OAuth2Credential chatAccount;

    /**
     * Channel Cache
     */
    @With
    private Boolean enableChannelCache = false;

    /**
     * EventManager
     */
    @With
    private EventManager eventManager = null;

    /**
     * How many threads should the eventManager use to process the events?
     */
    @With
    private Integer eventManagerThreads = Runtime.getRuntime().availableProcessors() * 2;

    /**
     * How many events can be queued before the eventManager should start dropping events?
     */
    @With
    private Integer eventManagerBufferSize = 16384;

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
     * User Agent
     */
    @With
    private String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36";

    /**
     * User Agent
     */
    @Wither
    private String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36";;

    /**
     * CredentialManager
     */
    @With
    private CredentialManager credentialManager = CredentialManagerBuilder.builder().build();

    /**
     * Default Auth Token for API Requests
     */
    @With
    private OAuth2Credential defaultAuthToken = null;

    /**
     * Default Auth Token for API Requests
     */
     * Thread Pool Executor
     */
    @With
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);

    /**
     * Millisecond Delay for Client Helper Thread
     */
    @With
    private long helperThreadRate = 10000L;


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
        log.debug("TwitchClient: Initializing ErrorTracking ...");

        // Module: Auth (registers Twitch Identity Providers)
        TwitchAuth authModule = new TwitchAuth(credentialManager, getClientId(), getClientSecret(), redirectUrl);

        // Default EventManager
        if (eventManager == null) {
            eventManager = new EventManager();
            eventManager.autoDiscovery();
        }

        // Module: Helix
        TwitchHelix helix = null;
        if (this.enableHelix) {
            helix = TwitchHelixBuilder.builder()
                .withClientId(getClientId())
                .withClientSecret(getClientSecret())
                .withUserAgent(userAgent)
                .withRequestQueueSize(getRequestQueueSize())
                .withTimeout(timeout)
                .build();
        }

        // Module: Kraken
        TwitchKraken kraken = null;
        if (this.enableKraken) {
            kraken = TwitchKrakenBuilder.builder()
                .withClientId(getClientId())
                .withClientSecret(getClientSecret())
                .withUserAgent(userAgent)
                .withRequestQueueSize(getRequestQueueSize())
                .withTimeout(timeout)
                .build();
        }

        // Module: TMI
        TwitchMessagingInterface tmi = null;
        if (this.enableTMI) {
            tmi = TwitchMessagingInterfaceBuilder.builder()
                .withClientId(getClientId())
                .withClientSecret(getClientSecret())
                .withUserAgent(userAgent)
                .withRequestQueueSize(getRequestQueueSize())
                .withTimeout(timeout)
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
                .withChatQueueSize(chatQueueSize)
                .withChatRateLimit(chatRateLimit)
                .build();
        }

        // Module: PubSub
        TwitchPubSub pubsub = null;
        if (this.enablePubSub) {
            pubsub = TwitchPubSubBuilder.builder()
                .withEventManager(eventManager)
                .build();
        }

        // Module: GraphQL
        TwitchGraphQL graphql = null;
        if (this.enableGraphQL) {
            graphql = TwitchGraphQLBuilder.builder()
                .withEventManager(eventManager)
                .withClientId(getClientId())
                .withClientSecret(getClientSecret())
                .build();
        }

        // Module: Client
        final TwitchClient client = new TwitchClient(eventManager, helix, kraken, tmi, chat, pubsub, graphql, scheduledThreadPoolExecutor, helperThreadRate);
        client.getClientHelper().setDefaultAuthToken(defaultAuthToken);


        // Return new Client Instance
        return client;
    }

}
