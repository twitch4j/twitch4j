package com.github.twitch4j;

import com.github.philippheuer.events4j.core.EventManager;
import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.graphql.TwitchGraphQL;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.kraken.TwitchKraken;
import com.github.twitch4j.modules.ModuleLoader;
import com.github.twitch4j.pubsub.TwitchPubSub;
import com.github.twitch4j.tmi.TwitchMessagingInterface;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TwitchClient implements AutoCloseable {

    /**
     * Event Manager
     */
    private final EventManager eventManager;

    /**
     * API: Helix
     */
    private final TwitchHelix helix;

    /**
     * API: Kraken
     */
    private final TwitchKraken kraken;

    /**
     * API: TMI
     */
    private final TwitchMessagingInterface messagingInterface;

    /**
     * Chat
     */
    private final TwitchChat chat;

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
    private final ModuleLoader moduleLoader;

    /**
     * TwitchClientHelper
     * <p>
     * A helper method that contains some common use-cases, like follow events / go live event listeners / ...
     */
    private final TwitchClientHelper twitchClientHelper = new TwitchClientHelper(this);

    /**
     * Constructor
     *
     * @param eventManager EventManager
     * @param helix TwitchHelix
     * @param kraken TwitchKraken
     * @param messagingInterface TwitchMessagingInterface
     * @param chat TwitchChat
     * @param pubsub TwitchPubSub
     * @param graphql TwitchGraphQL
     */
    public TwitchClient(EventManager eventManager, TwitchHelix helix, TwitchKraken kraken, TwitchMessagingInterface messagingInterface, TwitchChat chat, TwitchPubSub pubsub, TwitchGraphQL graphql) {
        this.eventManager = eventManager;
        this.helix = helix;
        this.kraken = kraken;
        this.messagingInterface = messagingInterface;
        this.chat = chat;
        this.pubsub = pubsub;
        this.graphql = graphql;

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
     * Get Kraken
     *
     * @return TwitchKraken
     */
    @Deprecated
    public TwitchKraken getKraken() {
        if (this.kraken == null) {
            throw new RuntimeException("You have not enabled the Kraken Module! Please check out the documentation on Twitch4J -> Kraken.");
        }

        return this.kraken;
    }

    /**
     * Get MessagingInterface (API)
     *
     * @return TwitchMessagingInterface
     */
    @Unofficial
    public TwitchMessagingInterface getMessagingInterface() {
        if (this.messagingInterface == null) {
            throw new RuntimeException("You have not enabled the Twitch Messaging Interface Module! Please check out the documentation on Twitch4J -> TMI.");
        }

        return this.messagingInterface;
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

    /**
     * Get GraphQL
     *
     * @return TwitchGraphQL
     */
    @Unofficial
    public TwitchGraphQL getGraphQL() {
        if (this.graphql == null) {
            throw new RuntimeException("You have not enabled the GraphQL Module! Please check out the documentation on Twitch4J -> GraphQL.");
        }

        return this.graphql;
    }

    /**
     * Get Module Loader
     *
     * @return ModuleLoader
     */
    public ModuleLoader getModuleLoader() {
        return this.moduleLoader;
    }

    /**
     * Get TwitchClientHelper
     *
     * @return TwitchClientHelper
     */
    public TwitchClientHelper getClientHelper() {
        return this.twitchClientHelper;
    }

    /**
     * Close
     */
    public void close() {
        log.info("Closing TwitchClient ...");

        // Modules
        if (this.chat != null) {
            this.chat.close();
        }
        if (this.pubsub != null) {
            this.pubsub.close();
        }
        if (this.twitchClientHelper != null) {
            twitchClientHelper.close();
        }
    }
}
