package com.github.twitch4j;

import com.github.philippheuer.events4j.core.EventManager;
import com.github.twitch4j.chat.ITwitchChat;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.extensions.TwitchExtensions;
import com.github.twitch4j.graphql.TwitchGraphQL;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.kraken.TwitchKraken;
import com.github.twitch4j.modules.ModuleLoader;
import com.github.twitch4j.pubsub.ITwitchPubSub;
import com.github.twitch4j.tmi.TwitchMessagingInterface;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ScheduledThreadPoolExecutor;

@Slf4j
public class TwitchClientPool implements ITwitchClient {

    /**
     * Event Manager
     */
    @Getter
    private final EventManager eventManager;

    /**
     * API: Extensions
     */
    private final TwitchExtensions extensions;

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
    private final ITwitchChat chat;

    /**
     * PubSub
     */
    private final ITwitchPubSub pubsub;

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
     * @param extensions         TwitchExtensions
     * @param helix              TwitchHelix
     * @param kraken             TwitchKraken
     * @param messagingInterface TwitchMessagingInterface
     * @param chat               TwitchChat
     * @param pubsub             TwitchPubSub
     * @param graphql            TwitchGraphQL
     * @param threadPoolExecutor ScheduledThreadPoolExecutor
     */
    public TwitchClientPool(EventManager eventManager, TwitchExtensions extensions, TwitchHelix helix, TwitchKraken kraken, TwitchMessagingInterface messagingInterface, ITwitchChat chat, ITwitchPubSub pubsub, TwitchGraphQL graphql, ScheduledThreadPoolExecutor threadPoolExecutor) {
        this.eventManager = eventManager;
        this.extensions = extensions;
        this.helix = helix;
        this.kraken = kraken;
        this.messagingInterface = messagingInterface;
        this.chat = chat;
        this.pubsub = pubsub;
        this.graphql = graphql;
        this.clientHelper = new TwitchClientHelper(helix, eventManager, threadPoolExecutor);
        this.scheduledThreadPoolExecutor = threadPoolExecutor;

        // module loader
        this.moduleLoader = new ModuleLoader(this);

        // register with serviceMediator
        this.eventManager.getServiceMediator().addService("twitch4j", this);
    }

    /**
     * Get Extensions
     *
     * @return TwitchExtensions
     */
    public TwitchExtensions getExtensions() {
        if (this.extensions == null) {
            throw new RuntimeException("You have not enabled the Extensions Module! Please check out the documentation on Twitch4J -> Extensions.");
        }

        return this.extensions;
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
     * @return ITwitchChat
     */
    public ITwitchChat getChat() {
        if (this.chat == null) {
            throw new RuntimeException("You have not enabled the Chat Module! Please check out the documentation on Twitch4J -> Chat.");
        }

        return this.chat;
    }

    /**
     * Get PubSub
     *
     * @return ITwitchPubSub
     */
    public ITwitchPubSub getPubSub() {
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
    @SneakyThrows
    public void close() {
        log.info("Closing TwitchClientPool ...");

        // Modules
        ITwitchClient.super.close();

        // Shutdown ThreadPools created by Twitch4J
        if (scheduledThreadPoolExecutor.getThreadFactory() instanceof BasicThreadFactory) {
            BasicThreadFactory threadFactory = (BasicThreadFactory) scheduledThreadPoolExecutor.getThreadFactory();

            if (threadFactory.getNamingPattern().equalsIgnoreCase("twitch4j-%d")) {
                scheduledThreadPoolExecutor.shutdownNow();
            }
        }
    }
}
