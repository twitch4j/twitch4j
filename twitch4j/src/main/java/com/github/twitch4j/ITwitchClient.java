package com.github.twitch4j;

import com.github.philippheuer.events4j.core.EventManager;
import com.github.twitch4j.chat.ITwitchChat;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.eventsub.socket.IEventSubSocket;
import com.github.twitch4j.extensions.TwitchExtensions;
import com.github.twitch4j.graphql.TwitchGraphQL;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.kraken.TwitchKraken;
import com.github.twitch4j.modules.ModuleLoader;
import com.github.twitch4j.pubsub.ITwitchPubSub;
import com.github.twitch4j.tmi.TwitchMessagingInterface;
import org.jetbrains.annotations.ApiStatus;

public interface ITwitchClient extends AutoCloseable {

    /**
     * Get the event manager
     *
     * @return EventManager
     */
    EventManager getEventManager();

    /**
     * Get Extensions
     *
     * @return TwitchExtensions
     * @deprecated Migrate to Helix.
     */
    @Deprecated
    TwitchExtensions getExtensions();

    /**
     * Get Helix
     *
     * @return TwitchHelix
     */
    TwitchHelix getHelix();

    /**
     * Get Kraken
     *
     * @return TwitchKraken
     * @deprecated Decommissioned by Twitch.
     */
    @Deprecated
    TwitchKraken getKraken();

    /**
     * Get MessagingInterface (API)
     *
     * @return TwitchMessagingInterface
     */
    @Unofficial
    TwitchMessagingInterface getMessagingInterface();

    /**
     * Get Chat
     *
     * @return ITwitchChat
     */
    ITwitchChat getChat();

    /**
     * Get EventSocket (in public beta)
     *
     * @return IEventSubSocket
     * @implNote the underlying implementation allows for multiple users and multiple sockets per user.
     */
    @ApiStatus.Experimental
    IEventSubSocket getEventSocket();

    /**
     * Get PubSub
     *
     * @return ITwitchPubSub
     */
    ITwitchPubSub getPubSub();

    /**
     * Get GraphQL
     * <p>
     * This is an unofficial API that is not intended for third-party use. Use at your own risk. Methods could change or stop working at any time.
     *
     * @return TwitchGraphQL
     */
    @Unofficial
    TwitchGraphQL getGraphQL();

    /**
     * Get Module Loader
     *
     * @return ModuleLoader
     */
    ModuleLoader getModuleLoader();

    /**
     * Get TwitchClientHelper
     *
     * @return TwitchClientHelper
     */
    TwitchClientHelper getClientHelper();

    @Override
    default void close() {
        ITwitchChat chat = null;
        try {
            chat = getChat();
        } catch (Exception ignored) {
        }
        if (chat != null)
            chat.close();

        try {
            getEventSocket().close();
        } catch (Exception ignored) {
        }

        ITwitchPubSub pubsub = null;
        try {
            pubsub = getPubSub();
        } catch (Exception ignored) {
        }
        if (pubsub != null)
            pubsub.close();

        TwitchClientHelper clientHelper = getClientHelper();
        if (clientHelper != null)
            clientHelper.close();
    }

}
