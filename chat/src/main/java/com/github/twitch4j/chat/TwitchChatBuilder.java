package com.github.twitch4j.chat;

import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.CredentialManagerBuilder;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.api.service.IEventHandler;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.auth.TwitchAuth;
import com.github.twitch4j.auth.providers.TwitchIdentityProvider;
import com.github.twitch4j.chat.enums.MirroredMessagePolicy;
import com.github.twitch4j.chat.events.channel.ChannelJoinFailureEvent;
import com.github.twitch4j.chat.util.TwitchChatLimitHelper;
import com.github.twitch4j.client.websocket.WebsocketConnection;
import com.github.twitch4j.client.websocket.WebsocketConnectionConfig;
import com.github.twitch4j.common.config.ProxyConfig;
import com.github.twitch4j.common.config.Twitch4JGlobal;
import com.github.twitch4j.common.enums.TwitchLimitType;
import com.github.twitch4j.common.util.BucketUtils;
import com.github.twitch4j.common.util.CryptoUtils;
import com.github.twitch4j.common.util.EventManagerUtils;
import com.github.twitch4j.common.util.ThreadUtils;
import com.github.twitch4j.common.util.TwitchLimitRegistry;
import com.github.twitch4j.util.IBackoffStrategy;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.xanthic.cache.api.Cache;
import io.github.xanthic.cache.api.domain.ExpiryType;
import io.github.xanthic.cache.core.CacheApi;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.ApiStatus;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

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
     * WebsocketConnection
     * <p>
     * can be used to inject a mocked connection into the TwitchChat instance
     */
    @With(AccessLevel.PROTECTED)
    private WebsocketConnection websocketConnection = null;

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
    @Setter
    @Accessors(chain = true)
    protected Set<String> commandPrefixes = new HashSet<>();

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
    protected Bandwidth[] whisperRateLimit = TwitchChatLimitHelper.USER_WHISPER_LIMIT.toArray(new Bandwidth[2]);

    /**
     * Custom RateLimit for JOIN/PART
     */
    @With
    protected Bandwidth joinRateLimit = TwitchChatLimitHelper.USER_JOIN_LIMIT;

    /**
     * Custom RateLimit for AUTH
     */
    @With
    protected Bandwidth authRateLimit = TwitchChatLimitHelper.USER_AUTH_LIMIT;

    /**
     * Custom RateLimit for Messages per Channel
     * <p>
     * For example, this can restrict messages per channel at 100/30 (for a verified bot that has a global 7500/30 message limit).
     */
    @With
    protected Bandwidth perChannelRateLimit = BucketUtils.simple(100, Duration.ofSeconds(30), "per-channel-limit");

    /**
     * Shared bucket for messages
     */
    @With
    protected Bucket ircMessageBucket = null;

    /**
     * Shared bucket for whispers
     *
     * @deprecated Twitch will decommission whispers over IRC on February 18, 2023;
     * please migrate to TwitchHelix#sendWhisper and TwitchLimitRegistry#setLimit
     */
    @With
    @Deprecated
    protected Bucket ircWhisperBucket = null;

    /**
     * Shared bucket for joins
     */
    @With
    protected Bucket ircJoinBucket = null;

    /**
     * Shared bucket for auths
     */
    @With
    protected Bucket ircAuthBucket = null;

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
     * Proxy Configuration
     */
    @With
    private ProxyConfig proxyConfig = null;

    /**
     * Whether one's own channel should automatically be joined
     */
    @With
    private boolean autoJoinOwnChannel = true;

    /**
     * Whether JOIN/PART events should be enabled for the {@link TwitchChat} instance.
     */
    @With
    private boolean enableMembershipEvents = true;

    /**
     * Whether join failures should result in removal from current channels.
     *
     * @see ChannelJoinFailureEvent
     */
    @With
    private boolean removeChannelOnJoinFailure = false;

    /**
     * The maximum number of retries to make for joining each channel, with exponential backoff.
     * Set to zero or a negative value to disable this feature.
     */
    @With
    private int maxJoinRetries = 7;

    /**
     * The amount of milliseconds to wait after queuing a JOIN before classifying the attempt as a failure.
     * <p>
     * If this value is configured too low, the chat instance could mistake a successfully joined channel for a failure.
     * This can be especially problematic when removeChannelOnJoinFailure has been set to true.
     */
    @With
    private long chatJoinTimeout = 2000L;

    /**
     * WebSocket RFC Ping Period in ms (0 = disabled)
     */
    @With
    private int wsPingPeriod = 15_000;

    /**
     * Websocket Close Delay in ms (0 = minimum)

     * @see WebsocketConnectionConfig#closeDelay()
     */
    @With
    private int wsCloseDelay = 1_000;

    /**
     * WebSocket Connection Backoff Strategy
     */
    @With
    private IBackoffStrategy connectionBackoffStrategy = null;

    /**
     * Whether the {@link #getChatAccount()} should be validated on reconnection failures.
     * <p>
     * If enabled and the token has expired, chat will connect in read-only mode instead.
     * <p>
     * If the network connection is too slow, you may want to disable this setting to avoid
     * disconnects while waiting for the result of the validate endpoint.
     */
    @With
    private boolean verifyChatAccountOnReconnect = true;

    /**
     * Filter for outbound messages.
     * The command will not be sent to the IRC server if the predicate yields true.
     * <p>
     * Only intended for internal use by twitch4j.
     */
    @Getter(AccessLevel.PRIVATE)
    @With(onMethod_ = { @Deprecated, @ApiStatus.Internal }) // try to discourage calls from outside the library
    private BiPredicate<TwitchChat, String> outboundCommandFilter = null;

    /**
     * Mirrored Message Policy
     */
    @With
    private MirroredMessagePolicy mirroredMessagePolicy = MirroredMessagePolicy.REJECT_IF_OBSERVED;

    /**
     * Predicate that indicates whether the bot has joined a given room id.
     * Only intended for internal use by twitch4j to power {@link MirroredMessagePolicy}.
     */
    @With(onMethod_ = { @ApiStatus.Internal }) // discourage external usage
    private Predicate<String> joinedToRoomId;

    /**
     * Cache of message IDs that have been observed.
     * Only intended for internal use by twitch4j to power {@link MirroredMessagePolicy}.
     */
    @With(onMethod_ = { @ApiStatus.Internal }) // discourage external usage
    private Cache<String, Boolean> observedMessageIds;

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
            scheduledThreadPoolExecutor = ThreadUtils.getDefaultScheduledThreadPoolExecutor("twitch4j-chat-"+ CryptoUtils.generateNonce(4), TwitchChat.REQUIRED_THREAD_COUNT);

        if (mirroredMessagePolicy == MirroredMessagePolicy.REJECT_IF_OBSERVED && observedMessageIds == null) {
            observedMessageIds = CacheApi.create(spec -> {
                spec.expiryTime(Duration.ofSeconds(10L));
                spec.expiryType(ExpiryType.POST_WRITE);
                spec.maxSize(1024L);
            });
        }

        // Initialize/Check EventManager
        eventManager = EventManagerUtils.validateOrInitializeEventManager(eventManager, defaultEventHandler);

        // Initialize/Check CredentialManager
        if (credentialManager == null) {
            credentialManager = CredentialManagerBuilder.builder().build();
        }
        TwitchAuth.registerIdentityProvider(credentialManager, clientId, clientSecret, null, false);

        // Register rate limits across the user id contained within the chat token
        final String userId;
        if (chatAccount == null) {
            userId = null;
        } else {
            if (StringUtils.isEmpty(chatAccount.getUserId())) {
                credentialManager.getIdentityProviderByName("twitch", TwitchIdentityProvider.class)
                    .flatMap(tip -> tip.getAdditionalCredentialInformation(chatAccount))
                    .ifPresent(chatAccount::updateCredential);
            }
            userId = StringUtils.defaultIfEmpty(chatAccount.getUserId(), null);
        }

        if (ircMessageBucket == null)
            ircMessageBucket = userId == null ? BucketUtils.createBucket(this.chatRateLimit) : TwitchLimitRegistry.getInstance().getOrInitializeBucket(userId, TwitchLimitType.CHAT_MESSAGE_LIMIT, Collections.singletonList(chatRateLimit));

        if (ircWhisperBucket == null)
            ircWhisperBucket = userId == null ? BucketUtils.createBucket(this.whisperRateLimit) : TwitchLimitRegistry.getInstance().getOrInitializeBucket(userId, TwitchLimitType.CHAT_WHISPER_LIMIT, Arrays.asList(whisperRateLimit));

        if (ircJoinBucket == null)
            ircJoinBucket = userId == null ? BucketUtils.createBucket(this.joinRateLimit) : TwitchLimitRegistry.getInstance().getOrInitializeBucket(userId, TwitchLimitType.CHAT_JOIN_LIMIT, Collections.singletonList(joinRateLimit));

        if (ircAuthBucket == null)
            ircAuthBucket = userId == null ? BucketUtils.createBucket(this.authRateLimit) : TwitchLimitRegistry.getInstance().getOrInitializeBucket(userId, TwitchLimitType.CHAT_AUTH_LIMIT, Collections.singletonList(authRateLimit));

        if (perChannelRateLimit == null)
            perChannelRateLimit = chatRateLimit;

        log.debug("TwitchChat: Initializing Module ...");
        return new TwitchChat(this.websocketConnection, this.eventManager, this.credentialManager, this.chatAccount, this.baseUrl, this.sendCredentialToThirdPartyHost, this.commandPrefixes, this.chatQueueSize, this.ircMessageBucket, this.ircWhisperBucket, this.ircJoinBucket, this.ircAuthBucket, this.scheduledThreadPoolExecutor, this.chatQueueTimeout, this.proxyConfig, this.autoJoinOwnChannel, this.enableMembershipEvents, this.botOwnerIds, this.removeChannelOnJoinFailure, this.maxJoinRetries, this.chatJoinTimeout, this.wsPingPeriod, this.connectionBackoffStrategy, this.perChannelRateLimit, this.verifyChatAccountOnReconnect, this.wsCloseDelay, this.outboundCommandFilter, this.mirroredMessagePolicy, this.joinedToRoomId, this.observedMessageIds);
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
