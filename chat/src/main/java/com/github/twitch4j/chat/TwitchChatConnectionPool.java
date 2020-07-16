package com.github.twitch4j.chat;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.common.events.ForwardingEventHandler;
import com.github.twitch4j.common.pool.TwitchModuleConnectionPool;
import lombok.Builder;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A pool for {@link TwitchChat} connections.
 * <p>
 * This pool is easiest to use for:
 * <ul>
 *     <li>Reading from many channels without an account</li>
 *     <li>Reading from many channels with an account</li>
 *     <li>Reading from many channels using valid proxies</li>
 *     <li>Reading and sending messages/whispers with only one account</li>
 * </ul>
 * Other applications are possible, but harder to configure. See below for a list of warnings.
 * <p>
 * Warning: If a custom {@link java.util.concurrent.ScheduledThreadPoolExecutor} is specified,
 * its corePoolSize must be large enough for the threads required by connections made by this class.
 * <p>
 * Warning: If a chatAccount is to be shared across multiple connections and used to send messages,
 * you should use advancedConfiguration to ensure the two are using a shared {@link io.github.bucket4j.Bucket}.
 * <p>
 * Warning: If whispers are to be sent using this pool, one must manually join the channel to send the whisper from first.
 * If chatAccount's are dynamically supplied such that no two connections are using the same account, one can set
 * twitchChatBuilder.withAutoJoinOwnChannel(true) via advancedConfiguration to avoid the manual join.
 * <p>
 * Warning: If one specifies an eventManager for this pool, it must not already be used by a {@link TwitchChat} instance,
 * or events could be duplicated. It is acceptable, however, to share the same {@link EventManager} across connection pools.
 * <p>
 * Warning: One should not naively override connectionEventManager to point to the pool's eventManager, as this can lead to
 * duplicated events. Instead, each connection uses its own event manager, and events are forwarded from that instance to the pool.
 */
@SuperBuilder
public class TwitchChatConnectionPool extends TwitchModuleConnectionPool<TwitchChat, String, String, Class<Void>> {

    /**
     * Provides a chat account to be used when constructing a new {@link TwitchChat} instance.
     * By default, this yields null, which corresponds to an anonymous connection.
     */
    @NonNull
    @Builder.Default
    protected final Supplier<OAuth2Credential> chatAccount = () -> null;

    /**
     * Further configuration that should be applied to {@link TwitchChatBuilder} when creating new connections.
     * Can specify custom rate limits, command triggers, and so on here.
     */
    @NonNull
    @Builder.Default
    protected final Function<@NonNull TwitchChatBuilder, @NonNull TwitchChatBuilder> advancedConfiguration = Function.identity();

    /**
     * Sends the specified message to the channel, if it has been subscribed to.
     *
     * @param channel the channel to send the message to
     * @param message the message to send
     * @return whether a {@link TwitchChat} instance subscribed to that channel was identified and used
     */
    public boolean sendMessage(final String channel, final String message) {
        return this.sendMessage(channel, channel, message);
    }

    /**
     * Sends a message from the {@link TwitchChat} identified either to a channel or directly on the socket.
     *
     * @param channelToIdentifyChatInstance the channel used to identify which {@link TwitchChat} instance should be used to send the message; the instance must be subscribed to this channel.
     * @param targetChannel                 the channel to send the message to, if not null (otherwise it is sent directly on the socket)
     * @param message                       the message to be sent
     * @return whether a {@link TwitchChat} instance was found and used to send the message
     */
    public boolean sendMessage(final String channelToIdentifyChatInstance, final String targetChannel, final String message) {
        if (channelToIdentifyChatInstance == null)
            return false;

        final TwitchChat chat = subscriptions.get(channelToIdentifyChatInstance.toLowerCase());
        if (chat == null)
            return false;

        if (targetChannel != null)
            chat.sendMessage(targetChannel, message);
        else
            chat.sendRaw(message);

        return true;
    }

    /**
     * Sends a whisper.
     *
     * @param channelToIdentifyChatInstance the channel used to identify which {@link TwitchChat} instance should be used to send the message; the instance must be subscribed to this channel.
     * @param toChannel                     the channel to send the whisper to
     * @param message                       the message to send in the whisper
     * @return whether a {@link TwitchChat} instance was identified to send the message from
     * @throws NullPointerException if the identified {@link TwitchChat} does not have a valid chatCredential
     */
    public boolean sendPrivateMessage(final String channelToIdentifyChatInstance, final String toChannel, final String message) {
        final TwitchChat chat;
        if (channelToIdentifyChatInstance == null || (chat = subscriptions.get(channelToIdentifyChatInstance.toLowerCase())) == null)
            return false;

        chat.sendPrivateMessage(toChannel, message);
        return true;
    }

    /**
     * Joins a channel.
     *
     * @param s the channel name
     * @return the channel name
     */
    @Override
    public String subscribe(String s) {
        return super.subscribe(s != null ? s.toLowerCase() : null);
    }

    /**
     * Parts from a channel.
     *
     * @param s the channel name
     * @return a non-null class if able to part, null otherwise
     */
    @Override
    public Class<Void> unsubscribe(String s) {
        return super.unsubscribe(s != null ? s.toLowerCase() : null);
    }

    @Override
    protected EventManager getDefaultConnectionEventManager() {
        final EventManager em = new EventManager();
        em.registerEventHandler(new SimpleEventHandler()); // necessary for IRCEventHandler
        em.registerEventHandler(new ForwardingEventHandler(getEventManager())); // this is also problematic if there's a twitchchat actually running on the defaultEventManager
        return em;
    }

    @Override
    protected String handleSubscription(TwitchChat twitchChat, String s) {
        if (twitchChat == null) return null;
        twitchChat.joinChannel(s);
        return s;
    }

    @Override
    protected String handleDuplicateSubscription(TwitchChat twitchChat, String s) {
        return null;
    }

    @Override
    protected Class<Void> handleUnsubscription(TwitchChat twitchChat, String s) {
        if (twitchChat == null) return null;
        twitchChat.leaveChannel(s);
        return Void.TYPE;
    }

    @Override
    protected String getRequestFromSubscription(String s) {
        return s;
    }

    @Override
    protected TwitchChat createConnection() {
        return advancedConfiguration.apply(
            TwitchChatBuilder.builder()
                .withChatAccount(chatAccount.get())
                .withEventManager(getConnectionEventManager())
                .withScheduledThreadPoolExecutor(executor.get())
                .withProxyConfig(proxyConfig.get())
                .withAutoJoinOwnChannel(false) // user will have to manually send a subscribe call to enable whispers
        ).build();
    }

    @Override
    protected void disposeConnection(TwitchChat connection) {
        connection.close();
    }

}
