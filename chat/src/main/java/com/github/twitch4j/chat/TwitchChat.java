package com.github.twitch4j.chat;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.Scheduler;
import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.twitch4j.auth.providers.TwitchIdentityProvider;
import com.github.twitch4j.chat.enums.CommandSource;
import com.github.twitch4j.chat.enums.NoticeTag;
import com.github.twitch4j.chat.enums.TMIConnectionState;
import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.chat.events.CommandEvent;
import com.github.twitch4j.chat.events.IRCEventHandler;
import com.github.twitch4j.chat.events.channel.ChannelJoinFailureEvent;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.chat.events.channel.ChannelNoticeEvent;
import com.github.twitch4j.chat.events.channel.ChannelStateEvent;
import com.github.twitch4j.chat.events.channel.IRCMessageEvent;
import com.github.twitch4j.chat.events.channel.UserStateEvent;
import com.github.twitch4j.client.websocket.WebsocketConnection;
import com.github.twitch4j.client.websocket.domain.WebsocketConnectionState;
import com.github.twitch4j.common.config.ProxyConfig;
import com.github.twitch4j.common.util.BucketUtils;
import com.github.twitch4j.common.util.CryptoUtils;
import com.github.twitch4j.common.util.EscapeUtils;
import com.github.twitch4j.util.IBackoffStrategy;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

@Slf4j
public class TwitchChat implements ITwitchChat {

    public static final int REQUIRED_THREAD_COUNT = 2;

    /**
     * EventManager
     */
    @Getter
    private final EventManager eventManager;

    /**
     * CredentialManager
     */
    @Getter
    private final CredentialManager credentialManager;

    /**
     * WebSocket Connection
     */
    private final WebsocketConnection connection;

    /**
     * OAuth2Credential, used to sign in to twitch chat
     */
    private OAuth2Credential chatCredential;

    /**
     * Twitch's official WebSocket Server
     */
    public static final String TWITCH_WEB_SOCKET_SERVER = "wss://irc-ws.chat.twitch.tv:443";

    /**
     * ThirdParty WebSocket Server for Testing
     */
    public static final String FDGT_TEST_SOCKET_SERVER = "wss://irc.fdgt.dev";

    /**
     * Whether the {@link OAuth2Credential} password should be sent when the baseUrl does not
     * match the official twitch websocket server, thus bypassing a security check in the library.
     */
    protected final boolean sendCredentialToThirdPartyHost;

    /**
     * Channel Cache Lock
     */
    private final ReentrantLock channelCacheLock = new ReentrantLock();

    /**
     * Current Channels
     */
    protected final Set<String> currentChannels = ConcurrentHashMap.newKeySet();

    /**
     * Cache: ChannelId to ChannelName
     */
    protected final Map<String, String> channelIdToChannelName = new ConcurrentHashMap<>();

    /**
     * Cache: ChannelName to ChannelId
     */
    protected final Map<String, String> channelNameToChannelId = new ConcurrentHashMap<>();

    /**
     * IRC Message Bucket
     */
    protected final Bucket ircMessageBucket;

    /**
     * IRC Whisper Bucket
     */
    protected final Bucket ircWhisperBucket;

    /**
     * IRC Join Bucket
     */
    protected final Bucket ircJoinBucket;

    /**
     * IRC Auth Bucket
     */
    protected final Bucket ircAuthBucket;

    /**
     * IRC Per-Channel Message Limit
     */
    protected final Bandwidth perChannelRateLimit;

    /**
     * IRC Command Queue
     */
    protected final BlockingQueue<String> ircCommandQueue;

    /**
     * IRC Command Queue Thread
     */
    protected final ScheduledFuture<?> queueThread;

    /**
     * Whether {@link #flushCommand} is currently executing
     */
    private final AtomicBoolean flushing = new AtomicBoolean();

    /**
     * Whether an expedited flush has already been submitted
     */
    private final AtomicBoolean flushRequested = new AtomicBoolean();

    /**
     * The {@link Runnable} for flushing the {@link #ircCommandQueue}
     */
    private final Runnable flushCommand;

    /**
     * Command Queue Thread stop flag
     */
    protected volatile boolean stopQueueThread = false;

    /**
     * Bot Owner IDs
     */
    protected final Collection<String> botOwnerIds;

    /**
     * IRC Command Handlers
     */
    protected final List<String> commandPrefixes;

    /**
     * Thread Pool Executor
     */
    protected final ScheduledExecutorService taskExecutor;

    /**
     * Time to wait for an item on the chat queue before continuing to next iteration
     * If set too high your thread will be late check to shutdown
     */
    protected final long chatQueueTimeout;

    /**
     * Whether one's own channel should automatically be joined
     */
    protected final boolean autoJoinOwnChannel;

    /**
     * Whether join failures should result in removal from current channels
     */
    protected final boolean removeChannelOnJoinFailure;

    /**
     * Whether JOIN/PART events should be enabled
     */
    protected final boolean enableMembershipEvents;

    /**
     * The maximum number of attempts to make for joining each channel
     */
    protected final int maxJoinRetries;

    /**
     * Minimum milliseconds to wait after a join attempt
     */
    protected final long chatJoinTimeout;

    /**
     * Cache of recent number of join attempts for each channel
     */
    protected final Cache<String, Integer> joinAttemptsByChannelName;

    /**
     * Cache of per-channel message buckets
     */
    protected final Cache<String, Bucket> bucketByChannelName;

    /**
     * Constructor
     *
     * @param websocketConnection            WebsocketConnection
     * @param eventManager                   EventManager
     * @param credentialManager              CredentialManager
     * @param chatCredential                 Chat Credential
     * @param baseUrl                        The websocket url for the chat client to connect to
     * @param sendCredentialToThirdPartyHost Whether the password should be sent when the baseUrl is not official
     * @param commandPrefixes                Command Prefixes
     * @param chatQueueSize                  Chat Queue Size
     * @param ircMessageBucket               Bucket for chat
     * @param ircWhisperBucket               Bucket for whispers
     * @param ircJoinBucket                  Bucket for joins
     * @param ircAuthBucket                  Bucket for auths
     * @param taskExecutor                   ScheduledThreadPoolExecutor
     * @param chatQueueTimeout               Timeout to wait for events in Chat Queue
     * @param proxyConfig                    Proxy Configuration
     * @param autoJoinOwnChannel             Whether one's own channel should automatically be joined
     * @param enableMembershipEvents         Whether JOIN/PART events should be enabled
     * @param botOwnerIds                    Bot Owner IDs
     * @param removeChannelOnJoinFailure     Whether channels should be removed after a join failure
     * @param maxJoinRetries                 Maximum join retries per channel
     * @param chatJoinTimeout                Minimum milliseconds to wait after a join attempt
     * @param wsPingPeriod                   WebSocket Ping Period
     * @param connectionBackoffStrategy      WebSocket Connection Backoff Strategy
     * @param perChannelRateLimit            Per channel message limit
     */
    public TwitchChat(WebsocketConnection websocketConnection, EventManager eventManager, CredentialManager credentialManager, OAuth2Credential chatCredential, String baseUrl, boolean sendCredentialToThirdPartyHost, Collection<String> commandPrefixes, Integer chatQueueSize, Bucket ircMessageBucket, Bucket ircWhisperBucket, Bucket ircJoinBucket, Bucket ircAuthBucket, ScheduledThreadPoolExecutor taskExecutor, long chatQueueTimeout, ProxyConfig proxyConfig, boolean autoJoinOwnChannel, boolean enableMembershipEvents, Collection<String> botOwnerIds, boolean removeChannelOnJoinFailure, int maxJoinRetries, long chatJoinTimeout, int wsPingPeriod, IBackoffStrategy connectionBackoffStrategy, Bandwidth perChannelRateLimit) {
        this.eventManager = eventManager;
        this.credentialManager = credentialManager;
        this.chatCredential = chatCredential;
        this.sendCredentialToThirdPartyHost = sendCredentialToThirdPartyHost;
        this.commandPrefixes = new ArrayList<>(commandPrefixes);
        this.botOwnerIds = botOwnerIds;
        this.ircCommandQueue = new ArrayBlockingQueue<>(chatQueueSize, true);
        this.ircMessageBucket = ircMessageBucket;
        this.ircWhisperBucket = ircWhisperBucket;
        this.ircJoinBucket = ircJoinBucket;
        this.ircAuthBucket = ircAuthBucket;
        this.taskExecutor = taskExecutor;
        this.chatQueueTimeout = chatQueueTimeout;
        this.autoJoinOwnChannel = autoJoinOwnChannel;
        this.enableMembershipEvents = enableMembershipEvents;
        this.removeChannelOnJoinFailure = removeChannelOnJoinFailure;
        this.maxJoinRetries = maxJoinRetries;
        this.chatJoinTimeout = chatJoinTimeout;
        this.perChannelRateLimit = perChannelRateLimit;

        // init per channel message buckets by channel name
        this.bucketByChannelName = Caffeine.newBuilder()
            .expireAfterAccess(Math.max(perChannelRateLimit.getRefillPeriodNanos(), Duration.ofSeconds(30L).toNanos()), TimeUnit.NANOSECONDS)
            .build();

        // init connection
        if (websocketConnection == null) {
            this.connection = new WebsocketConnection(spec -> {
                spec.baseUrl(baseUrl);
                spec.wsPingPeriod(wsPingPeriod);
                spec.onConnected(this::onConnected);
                spec.onTextMessage(this::onTextMessage);
                spec.onDisconnecting(this::onDisconnecting);
                spec.taskExecutor(taskExecutor);
                spec.proxyConfig(proxyConfig);
                if (connectionBackoffStrategy != null)
                    spec.backoffStrategy(connectionBackoffStrategy);
            });
        } else {
            this.connection = websocketConnection;
        }

        // credential validation
        if (this.chatCredential == null) {
            log.info("TwitchChat: No ChatAccount provided, Chat will be joined anonymously! Please look at the docs Twitch4J -> Chat if this is unintentional");
        } else if (this.chatCredential.getUserName() == null) {
            log.debug("TwitchChat: AccessToken does not contain any user information, fetching using the CredentialManager ...");

            // credential manager
            Optional<OAuth2Credential> credential = credentialManager.getOAuth2IdentityProviderByName("twitch")
                .orElse(new TwitchIdentityProvider(null, null, null))
                .getAdditionalCredentialInformation(this.chatCredential);
            if (credential.isPresent()) {
                this.chatCredential = credential.get();
            } else {
                log.error("TwitchChat: Failed to get AccessToken Information, the token is probably not valid. Please check the docs Twitch4J -> Chat on how to obtain a valid token.");
            }
        }

        // register with serviceMediator
        this.eventManager.getServiceMediator().addService("twitch4j-chat", this);

        // register event listeners
        IRCEventHandler ircEventHandler = new IRCEventHandler(this);

        // connect to irc
        connection.connect();

        // queue command worker
        this.flushCommand = () -> {
            if (flushing.getAndSet(true)) return;

            while (!stopQueueThread && connection.getConnectionState() == WebsocketConnectionState.CONNECTED) {
                String command = null;
                try {
                    // Send the command
                    command = ircCommandQueue.poll();
                    if (command == null) break;
                    sendTextToWebSocket(command, false);

                    // Logging
                    log.debug("Processed command from queue: [{}].", command.startsWith("PASS") ? "***OAUTH TOKEN HIDDEN***" : command);
                } catch (Exception ex) {
                    log.error("Chat: Unexpected error in worker thread", ex);

                    // Reschedule command for processing
                    if (command != null) {
                        try {
                            ircCommandQueue.offer(command, this.chatQueueTimeout, TimeUnit.MILLISECONDS);
                        } catch (Exception e) {
                            log.error("Failed to reschedule command", e);
                        }
                    }

                    break;
                }
            }

            flushRequested.set(false);
            flushing.set(false);
        };

        // Thread will start right now
        this.queueThread = taskExecutor.scheduleAtFixedRate(flushCommand, 0, this.chatQueueTimeout, TimeUnit.MILLISECONDS);
        log.debug("Started IRC Queue Worker");

        // Event Handlers
        log.debug("Registering the following command triggers: {}", commandPrefixes);

        // register event handler
        eventManager.onEvent("twitch4j-chat-command-trigger", ChannelMessageEvent.class, this::onChannelMessage);
        eventManager.onEvent(IRCMessageEvent.class, event -> {
            // we get at least one room state event with channel name + id when we join a channel, so we cache that to provide channel id + name for all events
            if ("ROOMSTATE".equalsIgnoreCase(event.getCommandType())) {
                // check that channel id / name are present and that we didn't leave the channel yet
                if (event.getChannelId() != null) {
                    channelCacheLock.lock();
                    try {
                        // store mapping info into channelIdToChannelName / channelNameToChannelId
                        event.getChannelName().map(String::toLowerCase).filter(currentChannels::contains).ifPresent(name -> {
                            String oldName = channelIdToChannelName.put(event.getChannelId(), name);
                            if (!name.equals(oldName)) {
                                if (oldName != null) channelNameToChannelId.remove(oldName, event.getChannelId());
                                channelNameToChannelId.put(name, event.getChannelId());
                            }
                        });
                    } finally {
                        channelCacheLock.unlock();
                    }
                }
            }
        });

        // Initialize joinAttemptsByChannelName (on an attempt expiring without explicit removal, we retry with exponential backoff)
        if (maxJoinRetries > 0) {
            final long initialWait = Math.max(chatJoinTimeout, 0);
            this.joinAttemptsByChannelName = Caffeine.newBuilder()
                .expireAfterWrite(initialWait, TimeUnit.MILLISECONDS)
                .scheduler(Scheduler.forScheduledExecutorService(taskExecutor)) // required for prompt removals on java 8
                .<String, Integer>evictionListener((name, attempts, cause) -> {
                    if (cause == RemovalCause.EXPIRED && name != null && attempts != null) {
                        if (attempts < maxJoinRetries) {
                            taskExecutor.schedule(() -> {
                                if (currentChannels.contains(name)) {
                                    issueJoin(name, attempts + 1);
                                }
                            }, initialWait * (1L << (Math.min(attempts, 16) + 1)), TimeUnit.MILLISECONDS); // exponential backoff (pow2 optimization)
                        } else if (removeChannelOnJoinFailure && removeCurrentChannel(name)) {
                            eventManager.publish(new ChannelJoinFailureEvent(name, ChannelJoinFailureEvent.Reason.RETRIES_EXHAUSTED));
                        } else {
                            log.warn("Chat connection exhausted retries when attempting to join channel: {}", name);
                        }
                    }
                })
                .build();
        } else {
            this.joinAttemptsByChannelName = Caffeine.newBuilder().maximumSize(0).build(); // optimization
        }

        // Remove successfully joined channels from joinAttemptsByChannelName (as further retries are not needed)
        Consumer<AbstractChannelEvent> joinListener = e -> joinAttemptsByChannelName.invalidate(e.getChannel().getName().toLowerCase());
        eventManager.onEvent(ChannelStateEvent.class, joinListener::accept);
        eventManager.onEvent(ChannelNoticeEvent.class, joinListener::accept);
        eventManager.onEvent(UserStateEvent.class, joinListener::accept);

        // Ban Listener
        final Set<NoticeTag> banNotices = EnumSet.of(NoticeTag.MSG_BANNED, NoticeTag.MSG_CHANNEL_SUSPENDED, NoticeTag.TOS_BAN); // bit vector
        eventManager.onEvent(ChannelNoticeEvent.class, e -> {
            String name = e.getChannel().getName();
            NoticeTag type = e.getType();
            if (removeChannelOnJoinFailure && banNotices.contains(type) && removeCurrentChannel(name)) {
                ChannelJoinFailureEvent.Reason reason = type == NoticeTag.MSG_BANNED ? ChannelJoinFailureEvent.Reason.USER_BANNED : ChannelJoinFailureEvent.Reason.CHANNEL_SUSPENDED;
                eventManager.publish(new ChannelJoinFailureEvent(name, reason));
            }
        });
    }

    protected void onConnected() {
        String baseUrl = connection.getConfig().baseUrl();
        log.info("Connecting to Twitch IRC {}", baseUrl);

        // acquire capabilities
        sendTextToWebSocket("CAP REQ :twitch.tv/tags twitch.tv/commands" + (enableMembershipEvents ? " twitch.tv/membership" : ""), true);
        sendTextToWebSocket("CAP END", true);

        // sign in
        String userName;
        if (chatCredential != null) {
            boolean sendRealPass = sendCredentialToThirdPartyHost // check whether this security feature has been overridden
                || baseUrl.equalsIgnoreCase(TWITCH_WEB_SOCKET_SERVER) // check whether the url is exactly the official one
                || baseUrl.equalsIgnoreCase(TWITCH_WEB_SOCKET_SERVER.substring(0, TWITCH_WEB_SOCKET_SERVER.length() - 4)); // check whether the url matches without the port
            sendTextToWebSocket(String.format("pass oauth:%s", sendRealPass ? chatCredential.getAccessToken() : CryptoUtils.generateNonce(30)), true);
            userName = String.valueOf(chatCredential.getUserName()).toLowerCase();
        } else {
            userName = "justinfan" + ThreadLocalRandom.current().nextInt(100000);
        }
        sendTextToWebSocket(String.format("nick %s", userName), true);

        // Join defined channels, in case we reconnect or weren't connected yet when we called joinChannel
        for (String channel : currentChannels) {
            issueJoin(channel);
        }

        // then join to own channel - required for sending or receiving whispers
        if (chatCredential != null && chatCredential.getUserName() != null) {
            if (autoJoinOwnChannel && !currentChannels.contains(userName))
                joinChannel(userName);
        } else {
            log.warn("Chat: The whispers feature is currently not available because the provided credential does not hold information about the user. Please check the documentation on how to pass the token to the credentialManager where it will be enriched with the required information.");
        }
    }

    protected void onTextMessage(String text) {
        Arrays.asList(text.replace("\n\r", "\n")
                .replace("\r", "\n").split("\n"))
            .forEach(message -> {
                if (!message.equals("")) {
                    // Handle messages
                    log.trace("Received WebSocketMessage: " + message);
                    // - CAP
                    if (message.startsWith(":tmi.twitch.tv 410") || message.startsWith(":tmi.twitch.tv CAP * NAK")) {
                        log.error("Failed to acquire requested IRC capabilities!");
                    }
                    // - CAP ACK
                    else if (message.startsWith(":tmi.twitch.tv CAP * ACK :")) {
                        List<String> capabilities = Arrays.asList(message.substring(":tmi.twitch.tv CAP * ACK :".length()).split(" "));
                        capabilities.forEach(cap -> log.debug("Acquired chat capability: " + cap));
                    }
                    // - Ping
                    else if (message.equalsIgnoreCase("PING :tmi.twitch.tv")) {
                        sendTextToWebSocket("PONG :tmi.twitch.tv", true);
                        log.debug("Responding to PING request!");
                    }
                    // - Login failed.
                    else if (message.equalsIgnoreCase(":tmi.twitch.tv NOTICE * :Login authentication failed")) {
                        log.error("Invalid IRC Credentials. Login failed!");
                    }
                    // - Parse IRC Message
                    else {
                        try {
                            IRCMessageEvent event = new IRCMessageEvent(message, channelIdToChannelName, channelNameToChannelId, botOwnerIds);

                            if (event.isValid()) {
                                eventManager.publish(event);
                            } else {
                                log.trace("Can't parse {}", event.getRawMessage());
                            }
                        } catch (Exception ex) {
                            log.error(ex.getMessage(), ex);
                        }
                    }
                }
            });
    }

    protected void onDisconnecting() {
        sendTextToWebSocket("QUIT", true); // safe disconnect
    }

    /**
     * Connecting to IRC-WS
     */
    public void connect() {
        if (WebsocketConnectionState.DISCONNECTED.equals(connection.getConnectionState()) || WebsocketConnectionState.RECONNECTING.equals(connection.getConnectionState())) {
            if (chatCredential != null) {
                // Wait for AUTH limit before opening the connection
                ircAuthBucket.asBlocking().consumeUninterruptibly(1L);
            }
        }

        connection.connect();
    }

    /**
     * Disconnecting from IRC-WS
     */
    public void disconnect() {
        connection.disconnect();
    }

    /**
     * Reconnecting to IRC-WS
     */
    public void reconnect() {
        connection.reconnect();
    }

    /**
     * Send IRC Command
     *
     * @param command IRC Command
     * @param args    command arguments
     */
    protected void sendCommand(String command, String... args) {
        sendRaw(String.format("%s %s", command.toUpperCase(), String.join(" ", args)));
    }

    /**
     * Send raw irc command
     * <p>
     * Note: perChannelRateLimit does not apply when directly using this method
     *
     * @param command raw irc command
     */
    @SuppressWarnings("ConstantConditions")
    public boolean sendRaw(String command) {
        return BucketUtils.scheduleAgainstBucket(ircMessageBucket, taskExecutor, () -> queueCommand(command)) != null;
    }

    /**
     * Adds a raw irc command to the queue without checking bucket headroom.
     *
     * @param command Raw IRC command to be queued.
     */
    private void queueCommand(String command) {
        // Add command to the queue, waiting for a period of time if necessary
        if (!ircCommandQueue.offer(command)) {
            try {
                ircCommandQueue.offer(command, chatQueueTimeout, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                log.warn("Chat: unable to add command to full queue", e);
                return;
            }
        }

        // Expedite command execution if we aren't already flushing the queue and another expedition hasn't already been requested
        if (!flushing.get() && !flushRequested.getAndSet(true))
            taskExecutor.schedule(flushCommand, chatQueueTimeout / 20, TimeUnit.MILLISECONDS); // allow for some accumulation of requests before flushing
    }

    /**
     * Send IRC Command (for Login/...)
     * <p>
     * Sends important irc commands for login / capabilities and similar.
     * Will consume tokens to respect the ratelimit, but will bypass the limit if the bucket is empty.
     *
     * @param command      IRC Command
     * @param consumeToken should a token be consumed when sending this text?
     */
    private boolean sendTextToWebSocket(String command, Boolean consumeToken) {
        // will send text only if CONNECTED or CONNECTING
        if (!connection.getConnectionState().equals(WebsocketConnectionState.CONNECTED) && !connection.getConnectionState().equals(WebsocketConnectionState.CONNECTING)) {
            return false;
        }

        // consume tokens if available, but ignore if not as those are important system commands (CAP, Login, ...)
        if (consumeToken)
            ircMessageBucket.tryConsume(1L);

        // send message
        this.connection.sendText(command);

        return true;
    }

    /**
     * Joining the channel
     *
     * @param channelName channel name
     */
    @Override
    public void joinChannel(String channelName) {
        String lowerChannelName = channelName.toLowerCase();

        channelCacheLock.lock();
        try {
            if (currentChannels.add(lowerChannelName)) {
                issueJoin(lowerChannelName);
                log.debug("Joining Channel [{}].", lowerChannelName);
            } else {
                log.warn("Already joined channel {}", channelName);
            }
        } finally {
            channelCacheLock.unlock();
        }
    }

    private void issueJoin(String channelName) {
        this.issueJoin(channelName, 0);
    }

    protected void issueJoin(String channelName, int attempts) {
        BucketUtils.scheduleAgainstBucket(ircJoinBucket, taskExecutor, () -> {
            String name = channelName.toLowerCase();
            queueCommand("JOIN #" + name);
            joinAttemptsByChannelName.asMap().merge(name, attempts, Math::max); // mark that a join has been initiated to track later success or failure state
        });
    }

    /**
     * leaving the channel
     *
     * @param channelName channel name
     */
    @Override
    public boolean leaveChannel(String channelName) {
        String lowerChannelName = channelName.toLowerCase();

        channelCacheLock.lock();
        try {
            if (currentChannels.remove(lowerChannelName)) {
                issuePart(lowerChannelName);
                log.debug("Leaving Channel [{}].", lowerChannelName);

                // clear cache
                String cachedId = channelNameToChannelId.remove(lowerChannelName);
                if (cachedId != null) channelIdToChannelName.remove(cachedId);

                return true;
            } else {
                log.warn("Already left channel {}", channelName);
                return false;
            }
        } finally {
            channelCacheLock.unlock();
        }
    }

    private void issuePart(String channelName) {
        BucketUtils.scheduleAgainstBucket(
            ircJoinBucket,
            taskExecutor,
            () -> queueCommand("PART #" + channelName.toLowerCase())
        );
    }

    private boolean removeCurrentChannel(String channelName) {
        channelCacheLock.lock();
        try {
            if (currentChannels.remove(channelName)) {
                String id = channelNameToChannelId.remove(channelName);
                if (id != null) channelIdToChannelName.remove(id);
                return true;
            } else {
                return false;
            }
        } finally {
            channelCacheLock.unlock();
        }
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean sendMessage(String channel, String message, Map<String, Object> tags) {
        StringBuilder sb = new StringBuilder();
        if (tags != null && !tags.isEmpty()) {
            sb.append('@');
            tags.forEach((k, v) -> sb.append(k).append('=').append(EscapeUtils.escapeTagValue(v)).append(';'));
            sb.setCharAt(sb.length() - 1, ' '); // replace last semi-colon with space
        }
        sb.append("PRIVMSG #").append(channel.toLowerCase()).append(" :").append(message);

        log.debug("Adding message for channel [{}] with content [{}] to the queue.", channel.toLowerCase(), message);
        return BucketUtils.scheduleAgainstBucket(getChannelMessageBucket(channel), taskExecutor, () -> sendRaw(sb.toString())) != null;
    }

    /**
     * Sends a user a private message
     *
     * @param targetUser username
     * @param message    message
     */
    public void sendPrivateMessage(String targetUser, String message) {
        log.debug("Adding private message for user [{}] with content [{}] to the queue.", targetUser, message);
        BucketUtils.scheduleAgainstBucket(
            ircWhisperBucket,
            taskExecutor,
            () -> queueCommand(String.format("PRIVMSG #%s :/w %s %s", chatCredential.getUserName().toLowerCase(), targetUser, message))
        );
    }

    /**
     * On Channel Message
     *
     * @param event ChannelMessageEvent
     */
    private void onChannelMessage(ChannelMessageEvent event) {
        Optional<String> prefix = Optional.empty();
        Optional<String> commandWithoutPrefix = Optional.empty();

        // try to find a `command` based on the prefix
        for (String commandPrefix : this.commandPrefixes) {
            if (event.getMessage().startsWith(commandPrefix)) {
                prefix = Optional.of(commandPrefix);
                commandWithoutPrefix = Optional.of(event.getMessage().substring(commandPrefix.length()));
                break;
            }
        }

        // is command?
        if (commandWithoutPrefix.isPresent()) {
            log.debug("Detected a command in channel {} with content: {}", event.getChannel().getName(), commandWithoutPrefix.get());

            // dispatch command event
            eventManager.publish(new CommandEvent(CommandSource.CHANNEL, event.getChannel().getName(), event.getUser(), prefix.get(), commandWithoutPrefix.get(), event.getPermissions()));
        }
    }

    /**
     * Close
     */
    @Override
    public void close() {
        this.stopQueueThread = true;
        queueThread.cancel(false);
        this.disconnect();
    }

    @Override
    public boolean isChannelJoined(String channelName) {
        return currentChannels.contains(channelName.toLowerCase());
    }

    /**
     * Returns a set of all currently joined channels (without # prefix)
     *
     * @return a set of channel names
     * @deprecated use getChannels() instead
     */
    @Deprecated
    public List<String> getCurrentChannels() {
        return Collections.unmodifiableList(new ArrayList<>(currentChannels));
    }

    @Override
    public Set<String> getChannels() {
        return Collections.unmodifiableSet(currentChannels);
    }

    /**
     * @return the cached map used for channel id to name mapping
     */
    @Override
    public Map<String, String> getChannelIdToChannelName() {
        return Collections.unmodifiableMap(channelIdToChannelName);
    }

    /**
     * @return the cached map used for channel name to id mapping
     */
    @Override
    public Map<String, String> getChannelNameToChannelId() {
        return Collections.unmodifiableMap(channelNameToChannelId);
    }

    public long getLatency() {
        return connection.getLatency();
    }

    /**
     * @return the connection state
     */
    public WebsocketConnectionState getState() {
        return connection.getConnectionState();
    }

    /**
     * @return the connection state
     * @deprecated use {@link #getState()} instead
     */
    @Deprecated
    public TMIConnectionState getConnectionState() {
        switch (connection.getConnectionState()) {
            case DISCONNECTING:
                return TMIConnectionState.DISCONNECTING;
            case RECONNECTING:
                return TMIConnectionState.RECONNECTING;
            case CONNECTING:
                return TMIConnectionState.CONNECTING;
            case CONNECTED:
                return TMIConnectionState.CONNECTED;
            default:
                return TMIConnectionState.DISCONNECTED;
        }
    }

    private Bucket getChannelMessageBucket(@NotNull String channelName) {
        return bucketByChannelName.get(channelName.toLowerCase(), k -> BucketUtils.createBucket(perChannelRateLimit));
    }

}
