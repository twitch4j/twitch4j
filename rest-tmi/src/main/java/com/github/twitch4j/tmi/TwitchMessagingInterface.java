package com.github.twitch4j.tmi;

import com.github.twitch4j.tmi.domain.Chatters;
import com.github.twitch4j.tmi.domain.HostList;
import com.netflix.hystrix.HystrixCommand;
import feign.Param;
import feign.RequestLine;

import java.util.List;

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
    
    /**
     * Get Hosts
     * <p>
     * This endpoint returns a "host" record for each channel ID provided. If the channel is not hosting anyone,
     * the target_id and target_login fields will not be present.
     * Since it is not official and undocumented, this could disappear at any time.
     * Hopefully they would provide an official way to retrieve this data before removing it, though.
     *
     *
     * @param channelIds A list containing a user ID for each channel to check.
     * @return Host information for each channel ID passed.
     */
    @RequestLine("GET /hosts?include_logins=1&host={id}")
    HystrixCommand<HostList> getHosts(
        @Param("id") List<String> channelIds
    );
    
    /**
     * Get Hosts of target channel
     * <p>
     * This endpoint returns a "host" record for each channel hosting the channel with the provided targetId.
     * It does not return the login of the target, only of the hosts.
     * Therefore getTargetLogin will return null for each Host in HostList.getHosts().
     * Since it is not official and undocumented, this could disappear at any time.
     * Hopefully they would provide an official way to retrieve this data before removing it, though.
     *
     * @param targetId The user ID of the channel for which to get host information.
     * @return List of hosts of the target channel.
     */
    @RequestLine("GET /hosts?include_logins=1&target={id}")
    HystrixCommand<HostList> getHostsOf(
        @Param("id") String targetId
    );
}
