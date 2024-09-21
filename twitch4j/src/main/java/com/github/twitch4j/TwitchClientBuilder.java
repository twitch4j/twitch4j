package com.github.twitch4j;

import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.CredentialManagerBuilder;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.api.service.IEventHandler;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.auth.TwitchAuth;
import com.github.twitch4j.auth.providers.TwitchIdentityProvider;
import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.chat.TwitchChatBuilder;
import com.github.twitch4j.chat.enums.MirroredMessagePolicy;
import com.github.twitch4j.chat.util.TwitchChatLimitHelper;
import com.github.twitch4j.client.websocket.WebsocketConnectionConfig;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.common.config.ProxyConfig;
import com.github.twitch4j.common.config.Twitch4JGlobal;
import com.github.twitch4j.common.util.BucketUtils;
import com.github.twitch4j.common.util.CryptoUtils;
import com.github.twitch4j.common.util.EventManagerUtils;
import com.github.twitch4j.common.util.ThreadUtils;
import com.github.twitch4j.eventsub.socket.IEventSubSocket;
import com.github.twitch4j.eventsub.socket.TwitchEventSocket;
import com.github.twitch4j.eventsub.socket.TwitchEventSocketPool;
import com.github.twitch4j.extensions.TwitchExtensions;
import com.github.twitch4j.extensions.TwitchExtensionsBuilder;
import com.github.twitch4j.graphql.TwitchGraphQL;
import com.github.twitch4j.graphql.TwitchGraphQLBuilder;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.TwitchHelixBuilder;
import com.github.twitch4j.internal.ChatCommandHelixForwarder;
import com.github.twitch4j.kraken.TwitchKraken;
import com.github.twitch4j.kraken.TwitchKrakenBuilder;
import com.github.twitch4j.pubsub.TwitchPubSub;
import com.github.twitch4j.pubsub.TwitchPubSubBuilder;
import com.github.twitch4j.tmi.TwitchMessagingInterface;
import com.github.twitch4j.tmi.TwitchMessagingInterfaceBuilder;
import feign.Logger;
import io.github.bucket4j.Bandwidth;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Builder to get a TwitchClient Instance by provided various options, to provide the user with a lot of customizable options.
 */
@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TwitchClientBuilder {

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
     *
     * @see <a href="https://discuss.dev.twitch.tv/t/how-extensions-are-affected-by-the-legacy-twitch-api-v5-shutdown/32708">Twitch Shutdown Announcement</a>
     * @deprecated the Extensions API traditionally uses the decommissioned Kraken API. While the module now forwards calls to Helix, please migrate to using Helix directly as this module will be removed in the future.
     */
    @With
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    private Boolean enableExtensions = false;

    /**
     * Enabled: Helix
     */
    @With
    private Boolean enableHelix = false;

    /**
     * Enabled: Kraken
     * @deprecated Kraken is deprecated and has been shut down on <b>Febuary 28, 2022</b>.
     *             More details about the deprecation are available <a href="https://blog.twitch.tv/en/2021/07/15/legacy-twitch-api-v5-shutdown-details-and-timeline">here</a>.
     *             Use {@link #withEnableHelix(Boolean)} instead.
     */
    @With
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    private Boolean enableKraken = false;

    /**
     * Enabled: TMI
     *
     * @deprecated All of the {@link TwitchMessagingInterfaceBuilder} endpoints have been (or will be) decommissioned by Twitch.
     */
    @With
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    private Boolean enableTMI = false;

    /**
     * Enabled: Chat
     */
    @With
    private Boolean enableChat = false;

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
     * Enabled: EventSub over WebSocket
     */
    @With
    private Boolean enableEventSocket = false;

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
     *
     * @deprecated Twitch will decommission whispers over IRC on February 18, 2023;
     * please migrate to TwitchHelix#sendWhisper and TwitchLimitRegistry#setLimit
     */
    @With
    @Deprecated
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
     * Custom RateLimit for Messages per Channel
     * <p>
     * For example, this can restrict messages per channel at 100/30 (for a verified bot that has a global 7500/30 message limit).
     */
    @With
    protected Bandwidth chatChannelMessageLimit = BucketUtils.simple(100, Duration.ofSeconds(30), "per-channel-limit");

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
    @NotNull
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
     * Default Auth Token for Helix API Requests
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
     * Websocket Close Delay in ms (0 = minimum)
     *
     * @see WebsocketConnectionConfig#closeDelay()
     */
    @With
    private int wsCloseDelay = 1_000;

    /**
     * Whether chat commands should be executed via the Helix API, if possible.
     * <p>
     * Must have {@link #withEnableHelix(Boolean)} and {@link #withEnableChat(Boolean)} set to true.
     * <p>
     * Must have {@link #withChatAccount(OAuth2Credential)} or {@link #withDefaultAuthToken(OAuth2Credential)} specified.
     */
    @With
    private boolean chatCommandsViaHelix = Instant.now().isAfter(ChatCommandHelixForwarder.ENABLE_AFTER);

    /**
     * The per-channel rate limit at which chat commands forwarded to helix should be executed.
     * <p>
     * This prevents commands to a single channel from consuming the entire helix rate limit bucket.
     * As such, this can be restricted further or loosened, depending on how many channels the bot serves.
     * <p>
     * This has no effect unless {@link #isChatCommandsViaHelix()} is true.
     * <p>
     * Users should migrate to manual helix calls (with whatever throttling they desire) instead.
     */
    @With(onMethod_ = { @Deprecated })
    private Bandwidth forwardedChatCommandHelixLimitPerChannel = TwitchChatLimitHelper.MOD_MESSAGE_LIMIT;

    /**
     * Mirrored Message Policy for the chat module (currently does not apply to eventsub websocket).
     */
    @With
    private MirroredMessagePolicy ircMirroredMessagePolicy = MirroredMessagePolicy.REJECT_IF_OBSERVED;

    /**
     * With a Bot Owner's User ID
     *
     * @param userId the user id
     * @return TwitchClientBuilder
     */
    public TwitchClientBuilder withBotOwnerId(String userId) {
        this.botOwnerIds.add(userId);
        return this;
    }

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
     * With a base thread delay for API calls by {@link TwitchClientHelper}
     * <p>
     * Note: the method name has been a misnomer as it has always set the <i>delay</i> rather than a rate.
     * One can change the <i>rate</i> at any time via {@link TwitchClientHelper#setThreadRate(long)}.
     *
     * @param helperThreadDelay TwitchClientHelper Base Thread Delay
     * @return TwitchClientBuilder
     * @deprecated in favor of withHelperThreadDelay
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    public TwitchClientBuilder withHelperThreadRate(long helperThreadDelay) {
        return this.withHelperThreadDelay(helperThreadDelay);
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
        TwitchAuth.registerIdentityProvider(credentialManager, getClientId(), getClientSecret(), redirectUrl, TwitchHelixBuilder.MOCK_BASE_URL.equals(helixBaseUrl));

        // Initialize/Check EventManager
        eventManager = EventManagerUtils.validateOrInitializeEventManager(eventManager, defaultEventHandler);

        // Determinate required threadPool size
        int poolSize = TwitchClientHelper.REQUIRED_THREAD_COUNT;
        if (enableChat)
            poolSize = poolSize + TwitchChat.REQUIRED_THREAD_COUNT;
        if (enablePubSub)
            poolSize = poolSize + TwitchPubSub.REQUIRED_THREAD_COUNT;
        if (enableEventSocket)
            poolSize = poolSize + TwitchEventSocket.REQUIRED_THREAD_COUNT;

        // check a provided threadPool or initialize a default one
        if (scheduledThreadPoolExecutor != null && scheduledThreadPoolExecutor.getCorePoolSize() < poolSize) {
            log.warn("Twitch4J requires a scheduledThreadPoolExecutor with at least {} threads to be fully functional! Some features may not work as expected.", poolSize);
        }
        if (scheduledThreadPoolExecutor == null) {
            scheduledThreadPoolExecutor = ThreadUtils.getDefaultScheduledThreadPoolExecutor("twitch4j-" + CryptoUtils.generateNonce(4), poolSize);
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
                .withRedirectUrl(redirectUrl)
                .withCredentialManager(credentialManager)
                .withUserAgent(userAgent)
                .withDefaultAuthToken(defaultAuthToken)
                .withRequestQueueSize(requestQueueSize)
                .withScheduledThreadPoolExecutor(scheduledThreadPoolExecutor)
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
        TwitchChat chat = null;
        if (this.enableChat) {
            //noinspection deprecation (withOutboundCommandFilter *can* be used internally)
            chat = TwitchChatBuilder.builder()
                .withEventManager(eventManager)
                .withCredentialManager(credentialManager)
                .withChatAccount(chatAccount)
                .withChatQueueSize(chatQueueSize)
                .withChatRateLimit(chatRateLimit)
                .withWhisperRateLimit(chatWhisperLimit)
                .withJoinRateLimit(chatJoinLimit)
                .withAuthRateLimit(chatAuthLimit)
                .withPerChannelRateLimit(chatChannelMessageLimit)
                .withScheduledThreadPoolExecutor(scheduledThreadPoolExecutor)
                .withBaseUrl(chatServer)
                .withChatQueueTimeout(chatQueueTimeout)
                .withProxyConfig(proxyConfig)
                .withMaxJoinRetries(chatMaxJoinRetries)
                .withMirroredMessagePolicy(ircMirroredMessagePolicy)
                .withOutboundCommandFilter(
                    chatCommandsViaHelix && enableHelix && (chatAccount != null || defaultAuthToken != null) ? new ChatCommandHelixForwarder(
                        helix,
                        chatAccount != null ? chatAccount : defaultAuthToken,
                        credentialManager.getIdentityProviderByName(TwitchIdentityProvider.PROVIDER_NAME, TwitchIdentityProvider.class).orElse(null),
                        scheduledThreadPoolExecutor,
                        forwardedChatCommandHelixLimitPerChannel
                    ) : null
                )
                .setBotOwnerIds(botOwnerIds)
                .setCommandPrefixes(commandPrefixes)
                .withWsPingPeriod(wsPingPeriod)
                .withWsCloseDelay(wsCloseDelay)
                .build();
        }

        // Module: EventSub over WebSocket
        IEventSubSocket eventSocket = null;
        if (this.enableEventSocket) {
            eventSocket = TwitchEventSocketPool.builder()
                .eventManager(eventManager)
                .executor(scheduledThreadPoolExecutor)
                .fallbackToken(defaultAuthToken)
                .helix(helix)
                .identityProvider(
                    credentialManager.getIdentityProviderByName(TwitchIdentityProvider.PROVIDER_NAME, TwitchIdentityProvider.class)
                        .orElseGet(() -> new TwitchIdentityProvider(clientId, clientSecret, redirectUrl))
                )
                .advancedConfiguration(builder ->
                    builder.proxyConfig(() -> proxyConfig)
                )
                .build();
        }

        // Module: PubSub
        TwitchPubSub pubSub = null;
        if (this.enablePubSub) {
            pubSub = TwitchPubSubBuilder.builder()
                .withEventManager(eventManager)
                .withScheduledThreadPoolExecutor(scheduledThreadPoolExecutor)
                .withProxyConfig(proxyConfig)
                .setBotOwnerIds(botOwnerIds)
                .withWsPingPeriod(wsPingPeriod)
                .withWsCloseDelay(wsCloseDelay)
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

        // Module: TwitchClient & ClientHelper
        final TwitchClient client = new TwitchClient(eventManager, extensions, helix, kraken, tmi, chat, pubSub, graphql, eventSocket, scheduledThreadPoolExecutor, credentialManager, defaultAuthToken);
        client.getClientHelper().setThreadDelay(helperThreadDelay);

        // Return new Client Instance
        return client;
    }

}
