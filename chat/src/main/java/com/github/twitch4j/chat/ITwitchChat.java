package com.github.twitch4j.chat;

import com.github.philippheuer.events4j.core.EventManager;

public interface ITwitchChat extends AutoCloseable {

    EventManager getEventManager();

    /**
     * Joins a channel
     *
     * @param channelName The target channel name.
     */
    void joinChannel(String channelName);

    /**
     * Parts from a channel
     *
     * @param channelName The target channel name.
     * @return whether the channel was previously joined
     */
    boolean leaveChannel(String channelName);

    /**
     * Sends a message in a joined channel.
     *
     * @param channel The target channel name.
     * @param message The message to be sent.
     */
    boolean sendMessage(String channel, String message);

    @Override
    void close();

}
