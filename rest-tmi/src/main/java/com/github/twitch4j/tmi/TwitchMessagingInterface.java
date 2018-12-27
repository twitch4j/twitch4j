package com.github.twitch4j.tmi;

import com.github.twitch4j.tmi.domain.Chatters;
import com.netflix.hystrix.HystrixCommand;
import feign.Param;
import feign.RequestLine;

/**
 * Twitch - Messaging Interface
 */
public interface TwitchMessagingInterface {

    /**
     * Get Chatters
     * <p>
     * This endpoint returns all chatters in a channnel. It's not official, bot pretty much every single bot depends on this so i doubt that they will ever remove it.
     *
     * @param channelName Channel Name
     * @return List of all Viewers/mods/...
     */
    @RequestLine("GET /group/user/{channel}/chatters")
    HystrixCommand<Chatters> getChatters(
        @Param("channel") String channelName
    );
}
