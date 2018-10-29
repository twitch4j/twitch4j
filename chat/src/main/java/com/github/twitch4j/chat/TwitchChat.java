package com.github.twitch4j.chat;

import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.chat.enums.TMIConnectionState;
import com.github.twitch4j.chat.events.IRCEventHandler;
import com.github.twitch4j.chat.events.channel.IRCMessageEvent;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class TwitchChat {

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
    private Optional<OAuth2Credential> chatCredential = Optional.empty();

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
    private TMIConnectionState connectionState = TMIConnectionState.DISCONNECTED;

    /**
     * Enable Channel Cache
     */
    private Boolean enableChannelCache = false;

    /**
     * Channel Cache
     */
    protected final Map<String, Boolean> channelCache = new HashMap<>();

    /**
     * Constructor
     *
     * @param eventManager EventManager
     */
    public TwitchChat(EventManager eventManager, CredentialManager credentialManager, OAuth2Credential chatCredential, Boolean enableChannelCache) {
        this.eventManager = eventManager;
        this.credentialManager = credentialManager;
        this.chatCredential = Optional.ofNullable(chatCredential);
        this.enableChannelCache = enableChannelCache;

        // register with serviceMediator
        this.eventManager.getServiceMediator().addService("twitch4j-chat", this);

        // register event listeners
        IRCEventHandler ircEventHandler = new IRCEventHandler(this);

        // connect to irc
        this.connect();
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
                log.error("Connection to Twitch IRC failed: {} - Retrying ...", ex.getMessage());

                // Sleep 1 second before trying to reconnect
                try {
                    Thread.sleep(1000);
                } catch (Exception et) {

                }

                // reconnect
                reconnect();
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

                    sendCommand("cap req", ":twitch.tv/membership twitch.tv/tags twitch.tv/commands");

                    // if credentials is null, it will automatically disconnect
                    if (!chatCredential.isPresent()) {
                        log.error("Can't find credentials for the chat account!");
                        disconnect();
                        return; // do not continue script
                    }

                    sendCommand("pass", String.format("oauth:%s", chatCredential.get().getAuthToken()));
                    sendCommand("nick", chatCredential.get().getUserName());

                    // Join defined channels, in case we reconnect or weren't connected yet when we called joinChannel
                    if (!channelCache.isEmpty()) {
                        for (String channel : channelCache.keySet()) {
                            sendCommand("join", "#" + channel);
                        }
                    }

                    // then join to own channel - required for sending or receiving whispers
                    sendCommand("join", "#" + chatCredential.get().getUserName());

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
                                // - Ping
                                if(message.contains("PING :tmi.twitch.tv")) {
                                    sendPong(":tmi.twitch.tv");
                                    log.trace("Responding to PING with PONG!");
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
                                            eventManager.dispatchEvent(event);
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
     * @param command IRC Command
     * @param args command arguments
     */
    private void sendCommand(String command, String... args) {
        // will send command if connection has been established
        if (connectionState.equals(TMIConnectionState.CONNECTED) || connectionState.equals(TMIConnectionState.CONNECTING)) {
            // command will be uppercase.
            this.webSocket.sendText(String.format("%s %s", command.toUpperCase(), String.join(" ", args)));
        } else {
            log.warn("Can't send IRC-WS Command [{} {}]", command.toUpperCase(), String.join(" ", args));
        }
    }

    /**
     * Answer to twitch's ping request
     *
     * @param arg
     */
    private void sendPong(String arg) {
        sendCommand("PONG", arg);
    }

    /**
     * Joining the channel
     * @param channelName channel name
     */
    public void joinChannel(String channelName) {
        if (!channelCache.containsKey(channelName.toLowerCase())) { {
            sendCommand("join", "#" + channelName.toLowerCase());
            log.debug("Joining Channel [{}].", channelName.toLowerCase());
            channelCache.put(channelName.toLowerCase(), true);
        }}

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
        }
    }

}
