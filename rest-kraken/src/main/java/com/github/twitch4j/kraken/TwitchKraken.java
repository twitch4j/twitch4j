package com.github.twitch4j.kraken;

import com.github.twitch4j.kraken.domain.*;
import com.netflix.hystrix.HystrixCommand;
import feign.CollectionFormat;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;

/**
 * Twitch - Kraken API
 * <p>
 * Kraken is already deprecated, so we only offer methods which haven't been added to the new helix api yet. Please use the helix api if available.
 */
public interface TwitchKraken {

    /**
     * Get Channel Subscribers
     * <p>
     * Gets a list of users subscribed to a specified channel, sorted by the date when they subscribed.
     *
     * @param authToken Auth Token
     * @param channelId Channnel Id
     * @param limit     Maximum number of objects to return. Default: 25. Maximum: 100.
     * @param offset    Object offset for pagination of results. Default: 0.
     * @param direction Sorting direction. Valid values: asc, desc. Default: asc (oldest first).
     * @return Object
     */
    @RequestLine("GET /channels/{channelId}/subscriptions?limit={limit}&offset={offset}&direction={direction}")
    @Headers({
        "Authorization: OAuth {token}",
        "Accept: application/vnd.twitchtv.v5+json"
    })
    HystrixCommand<KrakenSubscriptionList> getChannelSubscribers(
        @Param("token") String authToken,
        @Param("channelId") String channelId,
        @Param("limit") Integer limit,
        @Param("offset") Integer offset,
        @Param("direction") String direction
    );

    /**
     * Follow Channel
     * <p>
     * Adds a specified user to the followers of a specified channel.
     *
     * @param authToken    Auth Token
     * @param userId       User Id
     * @param targetUserId Target User Id (the Channel the user will follow)
     * @return Object
     */
    @RequestLine("PUT /users/{user}/follows/channels/{targetUser}")
    @Headers({
        "Authorization: OAuth {token}",
        "Accept: application/vnd.twitchtv.v5+json"
    })
    HystrixCommand<Object> addFollow(
        @Param("token") String authToken,
        @Param("user") String userId,
        @Param("targetUser") String targetUserId
    );

    /**
     * Get Ingest Server List
     * <p>
     * The Twitch ingesting system is the first stop for a broadcast stream. An ingest server receives your stream, and the ingesting system authorizes and registers streams, then prepares them for viewers.
     *
     * @return KrakenIngestList
     */
    @RequestLine("GET /ingests")
    @Headers("Accept: application/vnd.twitchtv.v5+json")
    HystrixCommand<KrakenIngestList> getIngestServers();

    /**
     * Get All Teams
     * <p>
     * Gets all active teams.
     *
     * @param limit  Maximum number of objects to return, sorted by creation date. Default: 25. Maximum: 100.
     * @param offset Object offset for pagination of results. Default: 0.
     * @return KrakenTeamList
     */
    @RequestLine("GET /teams?limit={limit}&offset={offset}")
    @Headers("Accept: application/vnd.twitchtv.v5+json")
    HystrixCommand<KrakenTeamList> getAllTeams(
        @Param("limit") Integer limit,
        @Param("offset") Integer offset
    );

    /**
     * Get Team
     * <p>
     * Gets a specified team object.
     *
     * @param name team name
     * @return KrakenTeam
     */
    @RequestLine("GET /teams/{name}")
    @Headers("Accept: application/vnd.twitchtv.v5+json")
    HystrixCommand<KrakenTeam> getTeamByName(
        @Param("name") String name
    );

    /**
     * Get Users
     * <p>
     * Gets a list of specified user objects.
     *
     * @param logins User login name (lower case channelname/username). Multiple login names can be specified. Limit: 100.
     * @return KrakenUser
     */
    @RequestLine(value = "GET /users?login={logins}", collectionFormat = CollectionFormat.CSV)
    @Headers({
        "Accept: application/vnd.twitchtv.v5+json"
    })
    HystrixCommand<KrakenUserList> getUsersByLogin(
    	@Param("logins") List<String> logins
    );

}
