package com.github.twitch4j.kraken;

import com.netflix.hystrix.HystrixCommand;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import com.github.twitch4j.kraken.domain.KrakenIngestList;
import com.github.twitch4j.kraken.domain.KrakenTeam;
import com.github.twitch4j.kraken.domain.KrakenTeamList;

/**
 * Twitch - Kraken API
 * <p>
 * Kraken is already deprecated, so we only offer methods which haven't been added to the new helix api yet. Please use the helix api if available.
 */
public interface TwitchKraken {

    /**
     * Follow Channel
     * <p>
     * Adds a specified user to the followers of a specified channel.
     *
     * @param authToken Auth Token
     * @param userId       User Id
     * @param targetUserId 	Target User Id (the Channel the user will follow)
     * @return Object
     */
    @RequestLine("PUT /users/{user}/follows/channels/{targetUser}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<Object> addFollow(
        @Param("token") String authToken,
        @Param("user") Long userId,
        @Param("targetUser") Long targetUserId
    );

    /**
     * Get Ingest Server List
     * <p>
     * The Twitch ingesting system is the first stop for a broadcast stream. An ingest server receives your stream, and the ingesting system authorizes and registers streams, then prepares them for viewers.
     *
     * @return KrakenIngestList
     */
    @RequestLine("GET /ingests")
    HystrixCommand<KrakenIngestList> getIngestServers();

    /**
     * Get All Teams
     * <p>
     * Gets all active teams.
     *
     * @param limit       Maximum number of objects to return, sorted by creation date. Default: 25. Maximum: 100.
     * @param offset 	Object offset for pagination of results. Default: 0.
     * @return KrakenTeamList
     */
    @RequestLine("GET /teams?limit={limit}&offset={offset}")
    HystrixCommand<KrakenTeamList> getAllTeams(
        @Param("limit") Long limit,
        @Param("offset") Long offset
    );

    /**
     * Get Team
     * <p>
     * Gets a specified team object.
     *
     * @param name       team name
     * @return KrakenTeam
     */
    @RequestLine("GET /teams/{name}")
    HystrixCommand<KrakenTeam> getTeamByName(
        @Param("name") String name
    );
}
