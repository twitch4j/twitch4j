package com.github.twitch4j;

import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.eventsub.socket.IEventSubSocket;
import com.github.twitch4j.graphql.TwitchGraphQL;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.modules.ModuleLoader;
import com.github.twitch4j.pubsub.TwitchPubSub;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.jetbrains.annotations.ApiStatus;

import java.util.concurrent.ScheduledThreadPoolExecutor;

@Slf4j
public class TwitchClient implements ITwitchClient {

    /**
     * Event Manager
     */
    private final EventManager eventManager;

    /**
     * API: Helix
     */
    private final TwitchHelix helix;

    /**
     * Chat
     */
    private final TwitchChat chat;

    /**
     * EventSub over Websocket
     */
    private final IEventSubSocket eventSocket;

    /**
     * PubSub
     */
    private final TwitchPubSub pubsub;

    /**
     * GraphQL
     */
    private final TwitchGraphQL graphql;

    /**
     * Modules
     */
    @Getter
    private final ModuleLoader moduleLoader;

    /**
     * Scheduled Thread Pool Executor
     */
    private final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    /**
     * TwitchClientHelper
     * <p>
     * A helper method that contains some common use-cases, like follow events / go live event listeners / ...
     */
    @Getter
    private final TwitchClientHelper clientHelper;

    /**
     * Constructor
     *
     * @param eventManager       EventManager
     * @param helix              TwitchHelix
     * @param chat               TwitchChat
     * @param pubsub             TwitchPubSub
     * @param graphql            TwitchGraphQL
     * @param eventSocket        Twitch EventSub over Websocket
     * @param threadPoolExecutor ScheduledThreadPoolExecutor
     * @param credentialManager  CredentialManager
     * @param defaultAuthToken   OAuth2Credential
     */
    @ApiStatus.Internal
    public TwitchClient(EventManager eventManager, TwitchHelix helix, TwitchChat chat, TwitchPubSub pubsub, TwitchGraphQL graphql, IEventSubSocket eventSocket, ScheduledThreadPoolExecutor threadPoolExecutor, CredentialManager credentialManager, OAuth2Credential defaultAuthToken) {
        this.eventManager = eventManager;
        this.helix = helix;
        this.chat = chat;
        this.eventSocket = eventSocket;
        this.pubsub = pubsub;
        this.graphql = graphql;
        this.clientHelper = new TwitchClientHelper(helix, eventManager, threadPoolExecutor, credentialManager, defaultAuthToken);
        this.scheduledThreadPoolExecutor = threadPoolExecutor;

        // module loader
        this.moduleLoader = new ModuleLoader(this);

        // register with serviceMediator
        this.eventManager.getServiceMediator().addService("twitch4j", this);
    }

    /**
     * Get the event manager
     *
     * @return EventManager
     */
    public EventManager getEventManager() {
        return this.eventManager;
    }

    /**
     * Get Helix
     *
     * @return TwitchHelix
     */
    public TwitchHelix getHelix() {
        if (this.helix == null) {
            throw new RuntimeException("You have not enabled the Helix Module! Please check out the documentation on Twitch4J -> Helix.");
        }

        return this.helix;
    }

    /**
     * Get Chat
     *
     * @return TwitchChat
     */
    public TwitchChat getChat() {
        if (this.chat == null) {
            throw new RuntimeException("You have not enabled the Chat Module! Please check out the documentation on Twitch4J -> Chat.");
        }

        return this.chat;
    }

    /**
     * Get EventSocket
     *
     * @return IEventSubSocket
     * @implNote the underlying implementation allows for multiple users and multiple sockets per user.
     */
    public IEventSubSocket getEventSocket() {
        if (this.eventSocket == null) {
            throw new RuntimeException("You have not enabled the EventSub over WebSocket Module! Please check out the documentation on Twitch4J -> EventSub.");
        }

        return this.eventSocket;
    }

    /**
     * Get PubSub
     *
     * @return TwitchPubSub
     */
    public TwitchPubSub getPubSub() {
        if (this.pubsub == null) {
            throw new RuntimeException("You have not enabled the PubSub Module! Please check out the documentation on Twitch4J -> PubSub.");
        }

        return this.pubsub;
    }

    @Unofficial
    public TwitchGraphQL getGraphQL() {
        if (this.graphql == null) {
            throw new RuntimeException("You have not enabled the GraphQL Module! Please check out the documentation on Twitch4J -> GraphQL.");
        }

        return this.graphql;
    }

    /**
     * Close
     */
    @Override
    public void close() {
        log.info("Closing TwitchClient ...");

        // Modules
        ITwitchClient.super.close();

        // Shutdown ThreadPools created by Twitch4J
        if (scheduledThreadPoolExecutor.getThreadFactory() instanceof BasicThreadFactory) {
            BasicThreadFactory threadFactory = (BasicThreadFactory) scheduledThreadPoolExecutor.getThreadFactory();

            String pattern = threadFactory.getNamingPattern();
            if (pattern != null && pattern.startsWith("twitch4j-") && pattern.endsWith("-%d")) {
                scheduledThreadPoolExecutor.shutdownNow();
            }
        }
    }
}
