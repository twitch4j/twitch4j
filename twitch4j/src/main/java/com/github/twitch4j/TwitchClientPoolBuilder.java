package com.github.twitch4j;

import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.CredentialManagerBuilder;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.api.service.IEventHandler;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.auth.TwitchAuth;
import com.github.twitch4j.chat.ITwitchChat;
import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.chat.TwitchChatBuilder;
import com.github.twitch4j.chat.TwitchChatConnectionPool;
import com.github.twitch4j.chat.util.TwitchChatLimitHelper;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.common.config.ProxyConfig;
import com.github.twitch4j.common.config.Twitch4JGlobal;
import com.github.twitch4j.common.util.EventManagerUtils;
import com.github.twitch4j.common.util.ThreadUtils;
import com.github.twitch4j.extensions.TwitchExtensions;
import com.github.twitch4j.extensions.TwitchExtensionsBuilder;
import com.github.twitch4j.graphql.TwitchGraphQL;
import com.github.twitch4j.graphql.TwitchGraphQLBuilder;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.TwitchHelixBuilder;
import com.github.twitch4j.kraken.TwitchKraken;
import com.github.twitch4j.kraken.TwitchKrakenBuilder;
import com.github.twitch4j.pubsub.ITwitchPubSub;
import com.github.twitch4j.pubsub.TwitchPubSub;
import com.github.twitch4j.pubsub.TwitchPubSubBuilder;
import com.github.twitch4j.pubsub.TwitchPubSubConnectionPool;
import com.github.twitch4j.tmi.TwitchMessagingInterface;
import com.github.twitch4j.tmi.TwitchMessagingInterfaceBuilder;
import feign.Logger;
import io.github.bucket4j.Bandwidth;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Builder to get a TwitchClientPool Instance by provided various options, to provide the user with a lot of customizable options.
 */
@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TwitchClientPoolBuilder {

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
     * User Agent
     */
    @With
    private String userAgent = Twitch4JGlobal.userAgent;

    /**
     * HTTP Request Queue Size
     */
    @With
    private Integer requestQueueSize = -1;

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
     * Enabled: Extensions
     */
    @With
    private Boolean enableExtensions = false;

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

    @With
    private Boolean enableChatPool = false;

    @With
    private int maxChannelsPerChatInstance = 100;

    /**
     * User IDs of Bot Owners for applying {@link com.github.twitch4j.common.enums.CommandPermission#OWNER}
     */
    @Setter
    @Accessors(chain = true)
    protected Collection<String> botOwnerIds = new HashSet<>();

    /**
     * IRC Command Handlers
     */
    protected Set<String> commandPrefixes = new HashSet<>();

    /**
     * Enabled: PubSub
     */
    @With
    private Boolean enablePubSub = false;

    @With
    private Boolean enablePubSubPool = false;

    @With
    private int maxTopicsPerPubSubInstance = 50;

    /**
     * Enabled: GraphQL
     * <p>
     * This is an unofficial API that is not intended for third-party use. Use at your own risk. Methods could change or stop working at any time.
     */
    @With
    @Unofficial
    private Boolean enableGraphQL = false;

    /**
     * Chat Account
     */
    @With
    private OAuth2Credential chatAccount;

    /**
     * EventManager
     */
    @With
    private EventManager eventManager = null;

    /**
     * EventManager
     */
    @With
    private Class<? extends IEventHandler> defaultEventHandler = SimpleEventHandler.class;

    /**
     * Size of the ChatQueue
     */
    @With
    protected Integer chatQueueSize = 200;

    /**
     * Custom RateLimit for ChatMessages
     */
    @With
    protected Bandwidth chatRateLimit = TwitchChatLimitHelper.USER_MESSAGE_LIMIT;

    /**
     * Custom RateLimit for Whispers
     */
    @With
    protected Bandwidth[] chatWhisperLimit = TwitchChatLimitHelper.USER_WHISPER_LIMIT.toArray(new Bandwidth[2]);

    /**
     * Custom RateLimit for JOIN/PART
     */
    @With
    protected Bandwidth chatJoinLimit = TwitchChatLimitHelper.USER_JOIN_LIMIT;

    /**
     * Custom RateLimit for AUTH
     */
    @With
    protected Bandwidth chatAuthLimit = TwitchChatLimitHelper.USER_AUTH_LIMIT;

    /**
     * Wait time for taking items off chat queue in milliseconds. Default recommended
     */
    @With
    private long chatQueueTimeout = 1000L;

    /**
     * The maximum number of retries to make for joining each channel, with exponential backoff.
     * Set to zero or a negative value to disable this feature.
     */
    @With
    private int chatMaxJoinRetries = 7;

    /**
     * Sets the default server used for chat
     * <p>
     * Defaults to TwitchChat.TWITCH_WEB_SOCKET_SERVER, you can use TwitchChat.FDGT_TEST_SOCKET_SERVER for testing
     */
    @With
    private String chatServer = TwitchChat.TWITCH_WEB_SOCKET_SERVER;

    /**
     * The base URL to use for Helix API calls.
     * <p>
     * Can be adjusted to point to the <a href="https://dev.twitch.tv/docs/cli/mock-api-command">Twitch CLI Mock API</a>, for example.
     *
     * @see TwitchHelixBuilder#OFFICIAL_BASE_URL
     * @see TwitchHelixBuilder#MOCK_BASE_URL
     */
    @With
    private String helixBaseUrl = TwitchHelixBuilder.OFFICIAL_BASE_URL;

    /**
     * CredentialManager
     */
    @With
    private CredentialManager credentialManager = CredentialManagerBuilder.builder().build();

    /**
     * Scheduler Thread Pool Executor
     */
    @With
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = null;

    /**
     * Millisecond Delay for Client Helper Thread
     */
    @With
    private long helperThreadDelay = 10000L;

    /**
     * Default Auth Token for API Requests
     */
    @With
    private OAuth2Credential defaultAuthToken = null;

    /**
     * Default First-Party OAuth Token for GraphQL calls
     */
    @With
    private OAuth2Credential defaultFirstPartyToken = null;

    /**
     * Proxy Configuration
     */
    @With
    private ProxyConfig proxyConfig = null;

    /**
     * you can overwrite the feign loglevel to print the full requests + responses if needed
     */
    @With
    private Logger.Level feignLogLevel = Logger.Level.NONE;

    /**
     * WebSocket RFC Ping Period in ms (0 = disabled)
     */
    @With
    private int wsPingPeriod = 15_000;

    /**
     * With a Bot Owner's User ID
     *
     * @param userId the user id
     * @return TwitchClientPoolBuilder
     */
    public TwitchClientPoolBuilder withBotOwnerId(String userId) {
        this.botOwnerIds.add(userId);
        return this;
    }

    /**
     * With a CommandTrigger
     *
     * @param commandTrigger Command Trigger (Prefix)
     * @return TwitchClientPoolBuilder
     */
    public TwitchClientPoolBuilder withCommandTrigger(String commandTrigger) {
        this.commandPrefixes.add(commandTrigger);
        return this;
    }

    /**
     * With a base thread delay for API calls by {@link TwitchClientHelper}
     * <p>
     * Note: the method name has been a misnomer as it has always set the <i>delay</i> rather than a rate.
     * One can change the <i>rate</i> at any time via {@link TwitchClientHelper#setThreadRate(long)}.
     *
     * @param helperThreadDelay TwitchClientHelper Base Thread Delay
     * @return TwitchClientPoolBuilder
     * @deprecated in favor of withHelperThreadDelay
     */
    @Deprecated
    public TwitchClientPoolBuilder withHelperThreadRate(long helperThreadDelay) {
        return this.withHelperThreadDelay(helperThreadDelay);
    }

    /**
     * Initialize the builder
     *
     * @return Twitch Client Pool Builder
     */
    public static TwitchClientPoolBuilder builder() {
        return new TwitchClientPoolBuilder();
    }

    /**
     * Initialize
     *
     * @return {@link TwitchClientPool} initialized class
     */
    public TwitchClientPool build() {
        log.debug("TwitchClientPool: Initializing ErrorTracking ...");

        // Module: Auth (registers Twitch Identity Providers)
        TwitchAuth.registerIdentityProvider(credentialManager, getClientId(), getClientSecret(), redirectUrl);

        // Initialize/Check EventManager
        eventManager = EventManagerUtils.validateOrInitializeEventManager(eventManager, defaultEventHandler);

        // Determinate required threadPool size
        int poolSize = TwitchClientHelper.REQUIRED_THREAD_COUNT;
        if (enableChat || enableChatPool)
            poolSize = poolSize + TwitchChat.REQUIRED_THREAD_COUNT;
        if (enablePubSub || enablePubSubPool)
            poolSize = poolSize + TwitchPubSub.REQUIRED_THREAD_COUNT;

        // check a provided threadPool or initialize a default one
        if (scheduledThreadPoolExecutor != null && scheduledThreadPoolExecutor.getCorePoolSize() < poolSize) {
            log.warn("Twitch4J requires a scheduledThreadPoolExecutor with at least {} threads to be fully functional! Some features may not work as expected.", poolSize);
        }
        if (scheduledThreadPoolExecutor == null) {
            if (enableChatPool || enablePubSubPool)
                poolSize = Math.max(poolSize, Runtime.getRuntime().availableProcessors());
            scheduledThreadPoolExecutor = ThreadUtils.getDefaultScheduledThreadPoolExecutor("twitch4j-" + RandomStringUtils.random(4, true, true), poolSize);
        }

        // Module: Extensions
        TwitchExtensions extensions = null;
        if (this.enableExtensions) {
            extensions = TwitchExtensionsBuilder.builder()
                .withClientId(clientId)
                .withClientSecret(clientSecret)
                .withUserAgent(userAgent)
                .withRequestQueueSize(requestQueueSize)
                .withTimeout(timeout)
                .withProxyConfig(proxyConfig)
                .withLogLevel(feignLogLevel)
                .build();
        }

        // Module: Helix
        TwitchHelix helix = null;
        if (this.enableHelix) {
            helix = TwitchHelixBuilder.builder()
                .withBaseUrl(helixBaseUrl)
                .withClientId(clientId)
                .withClientSecret(clientSecret)
                .withUserAgent(userAgent)
                .withDefaultAuthToken(defaultAuthToken)
                .withRequestQueueSize(requestQueueSize)
                .withTimeout(timeout)
                .withProxyConfig(proxyConfig)
                .withLogLevel(feignLogLevel)
                .build();
        }

        // Module: Kraken
        TwitchKraken kraken = null;
        if (this.enableKraken) {
            kraken = TwitchKrakenBuilder.builder()
                .withClientId(clientId)
                .withClientSecret(clientSecret)
                .withUserAgent(userAgent)
                .withRequestQueueSize(requestQueueSize)
                .withTimeout(timeout)
                .withProxyConfig(proxyConfig)
                .withLogLevel(feignLogLevel)
                .build();
        }

        // Module: TMI
        TwitchMessagingInterface tmi = null;
        if (this.enableTMI) {
            tmi = TwitchMessagingInterfaceBuilder.builder()
                .withClientId(clientId)
                .withClientSecret(clientSecret)
                .withUserAgent(userAgent)
                .withRequestQueueSize(requestQueueSize)
                .withTimeout(timeout)
                .withProxyConfig(proxyConfig)
                .withLogLevel(feignLogLevel)
                .build();
        }

        // Module: Chat
        ITwitchChat chat = null;
        if (this.enableChatPool) {
            chat = TwitchChatConnectionPool.builder()
                .eventManager(eventManager)
                .chatAccount(() -> chatAccount)
                .chatRateLimit(chatRateLimit)
                .whisperRateLimit(chatWhisperLimit)
                .joinRateLimit(chatJoinLimit)
                .authRateLimit(chatAuthLimit)
                .executor(() -> scheduledThreadPoolExecutor)
                .proxyConfig(() -> proxyConfig)
                .maxSubscriptionsPerConnection(maxChannelsPerChatInstance)
                .advancedConfiguration(builder ->
                    builder.withCredentialManager(credentialManager)
                        .withChatQueueSize(chatQueueSize)
                        .withBaseUrl(chatServer)
                        .withChatQueueTimeout(chatQueueTimeout)
                        .withMaxJoinRetries(chatMaxJoinRetries)
                        .withWsPingPeriod(wsPingPeriod)
                        .setCommandPrefixes(commandPrefixes)
                        .setBotOwnerIds(botOwnerIds)
                )
                .build();
        } else if (this.enableChat) {
            chat = TwitchChatBuilder.builder()
                .withEventManager(eventManager)
                .withCredentialManager(credentialManager)
                .withChatAccount(chatAccount)
                .withChatQueueSize(chatQueueSize)
                .withChatRateLimit(chatRateLimit)
                .withWhisperRateLimit(chatWhisperLimit)
                .withJoinRateLimit(chatJoinLimit)
                .withAuthRateLimit(chatAuthLimit)
                .withScheduledThreadPoolExecutor(scheduledThreadPoolExecutor)
                .withBaseUrl(chatServer)
                .withChatQueueTimeout(chatQueueTimeout)
                .withProxyConfig(proxyConfig)
                .withMaxJoinRetries(chatMaxJoinRetries)
                .setBotOwnerIds(botOwnerIds)
                .setCommandPrefixes(commandPrefixes)
                .withWsPingPeriod(wsPingPeriod)
                .build();
        }

        // Module: PubSub
        ITwitchPubSub pubSub = null;
        if (this.enablePubSubPool) {
            pubSub = TwitchPubSubConnectionPool.builder()
                .eventManager(eventManager)
                .executor(() -> scheduledThreadPoolExecutor)
                .proxyConfig(() -> proxyConfig)
                .advancedConfiguration(builder -> builder.withWsPingPeriod(wsPingPeriod).setBotOwnerIds(botOwnerIds))
                .build();
        } else if (this.enablePubSub) {
            pubSub = TwitchPubSubBuilder.builder()
                .withEventManager(eventManager)
                .withScheduledThreadPoolExecutor(scheduledThreadPoolExecutor)
                .withProxyConfig(proxyConfig)
                .withWsPingPeriod(wsPingPeriod)
                .setBotOwnerIds(botOwnerIds)
                .build();
        }

        // Module: GraphQL
        TwitchGraphQL graphql = null;
        if (this.enableGraphQL) {
            graphql = TwitchGraphQLBuilder.builder()
                .withEventManager(eventManager)
                .withDefaultFirstPartyToken(defaultFirstPartyToken)
                .withProxyConfig(proxyConfig)
                .withTimeout(timeout)
                .build();
        }

        // Module: TwitchClientPool & ClientHelper
        final TwitchClientPool client = new TwitchClientPool(eventManager, extensions, helix, kraken, tmi, chat, pubSub, graphql, scheduledThreadPoolExecutor);
        client.getClientHelper().setThreadDelay(helperThreadDelay);

        // Return new Client Instance
        return client;
    }

}
