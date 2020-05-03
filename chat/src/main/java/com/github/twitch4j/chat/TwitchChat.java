package com.github.twitch4j.chat;

import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.chat.enums.CommandSource;
import com.github.twitch4j.chat.enums.TMIConnectionState;
import com.github.twitch4j.chat.events.CommandEvent;
import com.github.twitch4j.chat.events.IRCEventHandler;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.chat.events.channel.IRCMessageEvent;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TwitchChat implements AutoCloseable {

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
    @Getter(AccessLevel.NONE)
    private Optional<OAuth2Credential> chatCredential;

    /**
     * The WebSocket Server
     */
    private final String webSocketServer = "wss://irc-ws.chat.twitch.tv:443";

    /**
     * WebSocket Client
     */
    @Setter(AccessLevel.NONE)
    private WebSocket webSocket;

    /**
     * The connection state
     * Default: ({@link TMIConnectionState#DISCONNECTED})
     */
    private volatile TMIConnectionState connectionState = TMIConnectionState.DISCONNECTED;

    /**
     * Channel Cache
     */
    protected final Map<String, Boolean> channelCache = new ConcurrentHashMap<>();

    /**
     * IRC Message Bucket
     */
    protected final Bucket ircMessageBucket;

    /**
     * IRC Command Queue
     */
    protected final ArrayBlockingQueue<String> ircCommandQueue;

    /**
     * Custom RateLimit for ChatMessages
     */
    protected final Bandwidth chatRateLimit;

    /**
     * IRC Command Queue Thread
     */
    protected final Thread queueThread;

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
    protected final ScheduledThreadPoolExecutor taskExecutor;

    /**
     * Time to wait for an item on the chat queue before continuing to next iteration
     * If set too high your thread will be late check to shutdown
     */
    protected final long chatQueueTimeout;

    /**
     * Constructor
     *
     * @param eventManager EventManager
     * @param credentialManager CredentialManager
     * @param chatCredential Chat Credential
     * @param commandPrefixes Command Prefixes
     * @param chatQueueSize Chat Queue Size
     * @param chatRateLimit Bandwidth / Bucket
     * @param taskExecutor ScheduledThreadPoolExecutor
     * @param chatQueueTimeout Timeout to wait for events in Chat Queue
     */
    public TwitchChat(EventManager eventManager, CredentialManager credentialManager, OAuth2Credential chatCredential, List<String> commandPrefixes, Integer chatQueueSize, Bandwidth chatRateLimit, ScheduledThreadPoolExecutor taskExecutor, long chatQueueTimeout) {
        this.eventManager = eventManager;
        this.credentialManager = credentialManager;
        this.chatCredential = Optional.ofNullable(chatCredential);
        this.commandPrefixes = commandPrefixes;
        this.ircCommandQueue = new ArrayBlockingQueue<>(chatQueueSize, true);
        this.chatRateLimit = chatRateLimit;
        this.taskExecutor = taskExecutor;
        this.chatQueueTimeout = chatQueueTimeout;

        // credential validation
        if (this.chatCredential.isPresent() == false) {
            log.error("TwitchChat: No ChatAccount provided, Chat will not be available! Please look at the docs Twitch4J -> Chat");
        } else if(this.chatCredential.get().getUserName() == null) {
            log.info("TwitchChat: AccessToken does not contain any user information, fetching using the CredentialManager ...");

            // credential manager
            Optional<OAuth2Credential> credential = credentialManager.getOAuth2IdentityProviderByName("twitch").get().getAdditionalCredentialInformation(this.chatCredential.get());
            if (credential.isPresent()) {
                this.chatCredential = credential;
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

        // connect to irc
        this.connect();

        // queue command worker
        this.queueThread = new Thread(() -> {
            while (!stopQueueThread) {
                String command = null;
                try {
                    // wait for queue, only have a timeout set to allow multiple loops to check stopQueueThread
                    command = ircCommandQueue.poll(this.chatQueueTimeout, TimeUnit.MILLISECONDS);
                    if(command != null) {
                        // Send the message, retrying forever until we are connected.
                        while (!stopQueueThread) {
                            if (connectionState.equals(TMIConnectionState.CONNECTED)) {
                                // block thread, until we can continue
                                ircMessageBucket.asScheduler().consume(1);

                                sendCommand(command);
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
                    if(command != null) {
                        try {
                            ircCommandQueue.put(command);
                        } catch (InterruptedException e) {
                            log.error("Failed to reschedule command", e);
                        }
                    }

                }
            }

        });

        // Thread will start right now
        taskExecutor.schedule(this.queueThread, 1L, TimeUnit.MILLISECONDS);
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
            connectionState = TMIConnectionState.DISCONNECTING;
            sendCommand("QUIT"); // safe disconnect
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
    private void createWebSocket() {
        try {
            // WebSocket
            this.webSocket = new WebSocketFactory().createSocket(webSocketServer);

            // WebSocket Listeners
            this.webSocket.clearListeners();
            this.webSocket.addListener(new WebSocketAdapter() {

                @Override
                public void onConnected(WebSocket ws, Map<String, List<String>> headers) {
                    log.info("Connecting to Twitch IRC {}", webSocketServer);

                    // if credentials is null, it will automatically disconnect
                    if (!chatCredential.isPresent()) {
                        log.error("Can't find credentials for the chat account!");
                        disconnect();
                        return; // do not continue script
                    }

                    // acquire capabilities
                    sendCommand("CAP REQ :twitch.tv/tags twitch.tv/commands twitch.tv/membership");
                    sendCommand("CAP END");

                    // sign in
                    sendCommand(String.format("pass oauth:%s", chatCredential.get().getAccessToken()));
                    sendCommand(String.format("nick %s", chatCredential.get().getUserName()));

                    // Join defined channels, in case we reconnect or weren't connected yet when we called joinChannel
                    if (!channelCache.isEmpty()) {
                        for (String channel : channelCache.keySet()) {
                            sendCommand("join #" + channel);
                        }
                    }

                    // then join to own channel - required for sending or receiving whispers
                    if (chatCredential.get().getUserName() != null) {
                        sendCommand("join #" + chatCredential.get().getUserName());
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
                                    sendCommand("PONG :tmi.twitch.tv");
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
        ircCommandQueue.add(String.format("%s %s", command.toUpperCase(), String.join(" ", args)));
    }

    /**
     * Send raw irc command
     *
     * @param command raw irc command
     */
    public void sendRaw(String command) {
        ircCommandQueue.add(command);
    }

    /**
     * Send IRC Command
     *
     * @param command IRC Command
     */
    private void sendCommand(String command) {
        // will send command if connection has been established
        if (connectionState.equals(TMIConnectionState.CONNECTED) || connectionState.equals(TMIConnectionState.CONNECTING)) {
            // command will be uppercase.
            this.webSocket.sendText(command);
        } else {
            log.warn("Can't send IRC-WS Command [{}]", command);
        }
    }

    /**
     * Joining the channel
     * @param channelName channel name
     */
    public void joinChannel(String channelName) {
        if (!channelCache.containsKey(channelName.toLowerCase())) {
            sendCommand("join", "#" + channelName.toLowerCase());
            log.debug("Joining Channel [{}].", channelName.toLowerCase());
            channelCache.put(channelName.toLowerCase(), true);
        } else {
            log.warn("Already joined channel {}", channelName);
        }
    }

    /**
     * leaving the channel
     * @param channelName channel name
     */
    public void leaveChannel(String channelName) {
        if (channelCache.containsKey(channelName.toLowerCase())) {
            sendCommand("part", "#" + channelName.toLowerCase());
            log.debug("Leaving Channel [{}].", channelName.toLowerCase());
            channelCache.remove(channelName.toLowerCase());
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
        ircCommandQueue.add(String.format("PRIVMSG #%s :%s", channel.toLowerCase(), message));
    }

    /**
     * Sends a user a private message
     *
     * @param targetUser username
     * @param message message
     */
    public void sendPrivateMessage(String targetUser, String message) {
        log.debug("Adding private message for user [{}] with content [{}] to the queue.", targetUser, message);
        ircCommandQueue.add(String.format("PRIVMSG #%s :/w %s %s", chatCredential.get().getUserName(), targetUser, message));
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
        StringBuilder sb = new StringBuilder(user).append(duration.getSeconds());
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
        this.taskExecutor.remove(this.queueThread);
        this.disconnect();
    }

    /**
     * Check if Chat is currently in a channel
     * @param channelName channel to check (without # prefix)
     * @return boolean
     */
    public boolean isChannelJoined(String channelName) {
        return channelCache.getOrDefault(channelName, false);
    }

    /**
     * Returns a list of all channels currently joined (without # prefix)
     *
     * @return List Channel Names
     */
    public List<String> getCurrentChannels() {
        return Collections.unmodifiableList(new ArrayList<>(channelCache.keySet()));
    }
}
