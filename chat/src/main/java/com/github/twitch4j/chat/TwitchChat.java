package com.github.twitch4j.chat;

import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.auth.providers.TwitchIdentityProvider;
import com.github.twitch4j.chat.enums.CommandSource;
import com.github.twitch4j.chat.enums.TMIConnectionState;
import com.github.twitch4j.chat.events.CommandEvent;
import com.github.twitch4j.chat.events.IRCEventHandler;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.chat.events.channel.IRCMessageEvent;
import com.github.twitch4j.common.config.ProxyConfig;
import com.github.twitch4j.common.util.CryptoUtils;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.local.LocalBucketBuilder;
import lombok.Getter;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TwitchChat implements AutoCloseable {

    public static final int REQUIRED_THREAD_COUNT = 1;

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
     * The websocket url for the chat client to connect to.
     */
    protected final String baseUrl;

    /**
     * Whether the {@link OAuth2Credential} password should be sent when the baseUrl does not
     * match the official twitch websocket server, thus bypassing a security check in the library.
     */
    protected final boolean sendCredentialToThirdPartyHost;

    /**
     * WebSocket Client
     */
    private volatile WebSocket webSocket;

    /**
     * The connection state
     * Default: ({@link TMIConnectionState#DISCONNECTED})
     */
    @Getter
    private volatile TMIConnectionState connectionState = TMIConnectionState.DISCONNECTED;

    /**
     * Channel Cache
     */
    protected final Set<String> channelCache = ConcurrentHashMap.newKeySet();

    /**
     * IRC Message Bucket
     */
    protected final Bucket ircMessageBucket;

    /**
     * IRC Whisper Bucket
     */
    protected final Bucket ircWhisperBucket;

    /**
     * IRC Command Queue
     */
    protected final BlockingQueue<String> ircCommandQueue;

    /**
     * Whisper-specific Command Queue
     */
    protected final BlockingQueue<String> whisperCommandQueue;

    /**
     * Custom RateLimit for ChatMessages
     */
    protected final Bandwidth chatRateLimit;

    /**
     * Custom RateLimit for Whispers
     */
    protected final Bandwidth[] whisperRateLimit;

    /**
     * IRC Command Queue Thread
     */
    protected final ScheduledFuture<?> queueThread;

    /**
     * Command Queue Thread stop flag
     */
    protected volatile Boolean stopQueueThread = false;

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
     * WebSocket Factory
     */
    protected final WebSocketFactory webSocketFactory;

    /**
     * Constructor
     *
     * @param eventManager EventManager
     * @param credentialManager CredentialManager
     * @param chatCredential Chat Credential
     * @param baseUrl The websocket url for the chat client to connect to
     * @param sendCredentialToThirdPartyHost Whether the password should be sent when the baseUrl is not official
     * @param commandPrefixes Command Prefixes
     * @param chatQueueSize Chat Queue Size
     * @param chatRateLimit Bandwidth / Bucket for chat
     * @param whisperRateLimit Bandwidth / Buckets for whispers
     * @param taskExecutor ScheduledThreadPoolExecutor
     * @param chatQueueTimeout Timeout to wait for events in Chat Queue
     * @param proxyConfig Proxy Configuration
     */
    public TwitchChat(EventManager eventManager, CredentialManager credentialManager, OAuth2Credential chatCredential, String baseUrl, boolean sendCredentialToThirdPartyHost, List<String> commandPrefixes, Integer chatQueueSize, Bandwidth chatRateLimit, Bandwidth[] whisperRateLimit, ScheduledThreadPoolExecutor taskExecutor, long chatQueueTimeout, ProxyConfig proxyConfig) {
        this.eventManager = eventManager;
        this.credentialManager = credentialManager;
        this.chatCredential = chatCredential;
        this.baseUrl = baseUrl;
        this.sendCredentialToThirdPartyHost = sendCredentialToThirdPartyHost;
        this.commandPrefixes = commandPrefixes;
        this.ircCommandQueue = new ArrayBlockingQueue<>(chatQueueSize, true);
        this.whisperCommandQueue = new LinkedBlockingQueue<>();
        this.chatRateLimit = chatRateLimit;
        this.whisperRateLimit = whisperRateLimit;
        this.taskExecutor = taskExecutor;
        this.chatQueueTimeout = chatQueueTimeout;

        // Create WebSocketFactory and apply proxy settings
        this.webSocketFactory = new WebSocketFactory();
        if (proxyConfig != null)
            proxyConfig.applyWs(webSocketFactory.getProxySettings());

        // credential validation
        if (this.chatCredential == null) {
            log.info("TwitchChat: No ChatAccount provided, Chat will be joined anonymously! Please look at the docs Twitch4J -> Chat if this is unintentional");
        } else if (this.chatCredential.getUserName() == null) {
            log.info("TwitchChat: AccessToken does not contain any user information, fetching using the CredentialManager ...");

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

        // initialize rate-limiting
        this.ircMessageBucket = Bucket4j.builder()
            .addLimit(this.chatRateLimit)
            .build();

        final LocalBucketBuilder whisperBucketBuilder = Bucket4j.builder();
        for (Bandwidth bandwidth : whisperRateLimit) {
            whisperBucketBuilder.addLimit(bandwidth);
        }
        this.ircWhisperBucket = whisperBucketBuilder.build();

        // connect to irc
        this.connect();

        // queue command worker
        Runnable queueTask = () -> {
            while (!stopQueueThread) {
                String command = null;
                Bucket bucket;
                try {
                    // wait for queue, only have a timeout set to allow multiple loops to check stopQueueThread
                    // attempt to grab command from whisper queue before falling back to the general queue
                    if (!whisperCommandQueue.isEmpty() && ircWhisperBucket.tryConsume(1L)) {
                        ircWhisperBucket.addTokens(1L);
                        command = whisperCommandQueue.poll(this.chatQueueTimeout, TimeUnit.MILLISECONDS);
                        bucket = ircWhisperBucket;
                    } else {
                        command = ircCommandQueue.poll(this.chatQueueTimeout, TimeUnit.MILLISECONDS);
                        bucket = ircMessageBucket;
                    }

                    if (command != null) {
                        // Send the message, retrying forever until we are connected.
                        while (!stopQueueThread) {
                            if (connectionState.equals(TMIConnectionState.CONNECTED)) {
                                // block thread, until we can continue
                                bucket.asScheduler().consume(1);

                                sendTextToWebSocket(command, false);
                                break;
                            }
                            // Sleep for 25 milliseconds to wait for reconnection
                            TimeUnit.MILLISECONDS.sleep(25L);
                        }
                        // Logging
                        log.debug("Processed command from queue: [{}].", command.startsWith("PASS") ? "***OAUTH TOKEN HIDDEN***" : command);
                        log.debug("{} messages left before hitting the rate-limit!", ircMessageBucket.getAvailableTokens());
                    }
                } catch (Exception ex) {
                    log.error("Failed to process message from command queue", ex);

                    // Reschedule command for processing
                    if (command != null) {
                        try {
                            ircCommandQueue.put(command);
                        } catch (InterruptedException e) {
                            log.error("Failed to reschedule command", e);
                        }
                    }

                }
            }
        };

        // Thread will start right now
        this.queueThread = taskExecutor.schedule(queueTask, 1L, TimeUnit.MILLISECONDS);
        log.debug("Started IRC Queue Worker");

        // Event Handlers
        log.debug("Registering the following command triggers: " + commandPrefixes.toString());

        // register event handler
        eventManager.getEventHandler(SimpleEventHandler.class).onEvent(ChannelMessageEvent.class, this::onChannelMessage);
    }

    /**
     * Connecting to IRC-WS
     */
    @Synchronized
    public void connect() {
        if (connectionState.equals(TMIConnectionState.DISCONNECTED) || connectionState.equals(TMIConnectionState.RECONNECTING)) {
            try {
                // Change Connection State
                connectionState = TMIConnectionState.CONNECTING;

                // Recreate Socket if state does not equal CREATED
                createWebSocket();

                // Connect to IRC WebSocket
                this.webSocket.connect();
            } catch (Exception ex) {
                log.error("Connection to Twitch IRC failed: Retrying ...", ex);
                // Sleep half before trying to reconnect
                try {
                    TimeUnit.MILLISECONDS.sleep(500L);
                } catch (Exception ignored) {

                } finally {
                    // reconnect
                    reconnect();
                }
            }
        }
    }

    /**
     * Disconnecting from IRC-WS
     */
    @Synchronized
    public void disconnect() {
        if (connectionState.equals(TMIConnectionState.CONNECTED)) {
            sendTextToWebSocket("QUIT", true); // safe disconnect
            connectionState = TMIConnectionState.DISCONNECTING;
        }

        connectionState = TMIConnectionState.DISCONNECTED;

        // CleanUp
        this.webSocket.clearListeners();
        this.webSocket.disconnect();
        this.webSocket = null;
    }

    /**
     * Reconnecting to IRC-WS
     */
    @Synchronized
    public void reconnect() {
        connectionState = TMIConnectionState.RECONNECTING;
        disconnect();
        connect();
    }

    /**
     * Recreate the WebSocket and the listeners
     */
    @Synchronized
    private void createWebSocket() {
        try {
            // WebSocket
            this.webSocket = webSocketFactory.createSocket(this.baseUrl);

            // WebSocket Listeners
            this.webSocket.clearListeners();
            this.webSocket.addListener(new WebSocketAdapter() {

                @Override
                public void onConnected(WebSocket ws, Map<String, List<String>> headers) {
                    log.info("Connecting to Twitch IRC {}", baseUrl);

                    // acquire capabilities
                    sendTextToWebSocket("CAP REQ :twitch.tv/tags twitch.tv/commands twitch.tv/membership", true);
                    sendTextToWebSocket("CAP END", true);

                    // sign in
                    String userName;
                    if (chatCredential != null) {
                        boolean sendRealPass = sendCredentialToThirdPartyHost // check whether this security feature has been overridden
                            || baseUrl.equalsIgnoreCase(TWITCH_WEB_SOCKET_SERVER) // check whether the url is exactly the official one
                            || baseUrl.equalsIgnoreCase(TWITCH_WEB_SOCKET_SERVER.substring(0, TWITCH_WEB_SOCKET_SERVER.length() - 4)); // check whether the url matches without the port
                        sendTextToWebSocket(String.format("pass oauth:%s", sendRealPass ? chatCredential.getAccessToken() : CryptoUtils.generateNonce(30)), true);
                        userName = chatCredential.getUserName();
                    } else {
                        userName = "justinfan" + ThreadLocalRandom.current().nextInt(100000);
                    }
                    sendTextToWebSocket(String.format("nick %s", userName), true);

                    // Join defined channels, in case we reconnect or weren't connected yet when we called joinChannel
                    for (String channel : channelCache) {
                        sendCommand("join", '#' + channel);
                    }

                    // then join to own channel - required for sending or receiving whispers
                    if (chatCredential != null && userName != null) {
                        sendCommand("join", '#' + chatCredential.getUserName());
                    } else {
                        log.warn("Chat: The whispers feature is currently not available because the provided credential does not hold information about the user. Please check the documentation on how to pass the token to the credentialManager where it will be enriched with the required information.");
                    }

                    // Connection Success
                    connectionState = TMIConnectionState.CONNECTED;
                }

                @Override
                public void onTextMessage(WebSocket ws, String text) {
                    Arrays.asList(text.replace("\n\r", "\n")
                        .replace("\r", "\n").split("\n"))
                        .forEach(message -> {
                            if (!message.equals("")) {
                                // Handle messages
                                log.trace("Received WebSocketMessage: " + message);
                                // - CAP
                                if (message.contains(":req Invalid CAP command")) {
                                    log.error("Failed to acquire requested IRC capabilities!");
                                }
                                else if (message.contains(":tmi.twitch.tv CAP * ACK :")) {
                                    List<String> capabilities = Arrays.asList(message.replace(":tmi.twitch.tv CAP * ACK :", "").split(" "));
                                    capabilities.forEach(cap -> log.debug("Acquired chat capability: " + cap ));
                                }
                                // - Ping
                                else if(message.contains("PING :tmi.twitch.tv")) {
                                    sendTextToWebSocket("PONG :tmi.twitch.tv", true);
                                    log.debug("Responding to PING request!");
                                }
                                // - Login failed.
                                else if(message.equals(":tmi.twitch.tv NOTICE * :Login authentication failed")) {
                                    log.error("Invalid IRC Credentials. Login failed!");
                                }
                                // - Parse IRC Message
                                else
                                {
                                    try {
                                        IRCMessageEvent event = new IRCMessageEvent(message);

                                        if(event.isValid()) {
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

                @Override
                public void onDisconnected(WebSocket websocket,
                                           WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame,
                                           boolean closedByServer) {
                    if (!connectionState.equals(TMIConnectionState.DISCONNECTING)) {
                        log.info("Connection to Twitch IRC lost (WebSocket)! Retrying ...");

                        // connection lost - reconnecting
                        reconnect();
                    } else {
                        connectionState = TMIConnectionState.DISCONNECTED;
                        log.info("Disconnected from Twitch IRC (WebSocket)!");
                    }
                }

            });

        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    /**
     * Send IRC Command
     *
     * @param command IRC Command
     * @param args command arguments
     */
    protected void sendCommand(String command, String... args) {
        ircCommandQueue.offer(String.format("%s %s", command.toUpperCase(), String.join(" ", args)));
    }

    /**
     * Send raw irc command
     *
     * @param command raw irc command
     */
    public void sendRaw(String command) {
        ircCommandQueue.offer(command);
    }

    /**
     * Send IRC Command (for Login/...)
     * <p>
     * Sends important irc commands for login / capabilities and similar.
     * Will consume tokens to respect the ratelimit, but will bypass the limit if the bucket is empty.
     *
     * @param command IRC Command
     * @param consumeToken should a token be consumed when sending this text?
     */
    private boolean sendTextToWebSocket(String command, Boolean consumeToken) {
        // will send text only if CONNECTED or CONNECTING
        if (!connectionState.equals(TMIConnectionState.CONNECTED) && !connectionState.equals(TMIConnectionState.CONNECTING)) {
            return false;
        }

        // consume tokens if available, but ignore if not as those are important system commands (CAP, Login, ...)
        if (consumeToken)
            ircMessageBucket.tryConsume(1L);

        // command will be uppercase.
        this.webSocket.sendText(command);

        return true;
    }

    /**
     * Joining the channel
     * @param channelName channel name
     */
    public void joinChannel(String channelName) {
        String lowerChannelName = channelName.toLowerCase();
        if (channelCache.add(lowerChannelName)) {
            sendCommand("join", "#" + lowerChannelName);
            log.debug("Joining Channel [{}].", lowerChannelName);
        } else {
            log.warn("Already joined channel {}", channelName);
        }
    }

    /**
     * leaving the channel
     * @param channelName channel name
     */
    public void leaveChannel(String channelName) {
        String lowerChannelName = channelName.toLowerCase();
        if (channelCache.remove(lowerChannelName)) {
            sendCommand("part", "#" + lowerChannelName);
            log.debug("Leaving Channel [{}].", lowerChannelName);
        } else {
            log.warn("Already left channel {}", channelName);
        }
    }

    /**
     * Sending message to the joined channel
     * @param channel channel name
     * @param message message
     */
    public void sendMessage(String channel, String message) {
        log.debug("Adding message for channel [{}] with content [{}] to the queue.", channel.toLowerCase(), message);
        ircCommandQueue.offer(String.format("PRIVMSG #%s :%s", channel.toLowerCase(), message));
    }

    /**
     * Sends a user a private message
     *
     * @param targetUser username
     * @param message message
     */
    public void sendPrivateMessage(String targetUser, String message) {
        log.debug("Adding private message for user [{}] with content [{}] to the queue.", targetUser, message);
        whisperCommandQueue.offer(String.format("PRIVMSG #%s :/w %s %s", chatCredential.getUserName().toLowerCase(), targetUser, message));
    }

    /**
     * Timeout a user
     *
     * @param channel channel
     * @param user username
     * @param duration duration
     * @param reason reason
     */
    public void timeout(String channel, String user, Duration duration, String reason) {
        StringBuilder sb = new StringBuilder(user).append(' ').append(duration.getSeconds());
        if (reason != null) {
            sb.append(" ").append(reason);
        }

        sendMessage(channel, String.format("/timeout %s", sb.toString()));
    }

    /**
     * Ban a user
     *
     * @param channel channel
     * @param user username
     * @param reason reason
     */
    public void ban(String channel, String user, String reason) {
        StringBuilder sb = new StringBuilder(user);
        if (reason != null) {
            sb.append(" ").append(reason);
        }

        sendMessage(channel, String.format("/ban %s", sb.toString()));
    }

    /**
     * Unban a user
     *
     * @param channel channel
     * @param user username
     */
    public void unban(String channel, String user) {
        sendMessage(channel, String.format("/unban %s", user));
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
    public void close() {
        this.stopQueueThread = true;
        queueThread.cancel(false);
        this.disconnect();
    }

    /**
     * Check if Chat is currently in a channel
     * @param channelName channel to check (without # prefix)
     * @return boolean
     */
    public boolean isChannelJoined(String channelName) {
        return channelCache.contains(channelName.toLowerCase());
    }

    /**
     * Returns a list of all channels currently joined (without # prefix)
     *
     * @return List Channel Names
     */
    public List<String> getCurrentChannels() {
        return Collections.unmodifiableList(new ArrayList<>(channelCache));
    }
}
