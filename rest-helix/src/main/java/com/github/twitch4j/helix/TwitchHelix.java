package com.github.twitch4j.helix;

import com.github.twitch4j.helix.domain.*;
import com.netflix.hystrix.HystrixCommand;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Twitch - Helix API
 */
public interface TwitchHelix {

    /**
     * Gets a URL that extension developers can use to download analytics reports (CSV files) for their extensions. The URL is valid for 5 minutes.
     * <p>
     * For detail about analytics and the fields returned, see the Insights and Analytics guide.
     *
     * @param authToken   Auth Token
     * @param after       Cursor for forward pagination: tells the server where to start fetching the next set of results, in a multi-page response.
     * @param limit       Maximum number of objects to return. Maximum: 100. Default: 20.
     * @param extensionId Client ID value assigned to the extension when it is created. If this is specified, the returned URL points to an analytics report for just the specified extension.
     * @param type        Type of analytics report that is returned. If this is specified, the response includes one URL, for the specified report type. If this is not specified, the response includes multiple URLs (paginated), one for each report type available for the authenticated user’s Extensions. Limit: 1. Valid values: "overview_v1", "overview_v2".
     * @param startedAt   Starting date/time for returned reports, in RFC3339 format with the hours, minutes, and seconds zeroed out and the UTC timezone: YYYY-MM-DDT00:00:00Z.
     * @param endedAt     Ending date/time for returned reports, in RFC3339 format with the hours, minutes, and seconds zeroed out and the UTC timezone: YYYY-MM-DDT00:00:00Z.
     * @return ExtensionAnalyticsList
     */
    @RequestLine("GET /analytics/extensions?after={after}&ended_at={ended_at}&first={first}&extension_id={extension_id}&started_at={started_at}&type={type}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<ExtensionAnalyticsList> getExtensionAnalyticUrl(
        @Param("token") String authToken,
        @Param("after") String after,
        @Param("first") Integer limit,
        @Param("extension_id") String extensionId,
        @Param("type") String type,
        @Param("started_at") String startedAt,
        @Param("ended_at") String endedAt
    );

    /**
     * Gets a URL that game developers can use to download analytics reports (CSV files) for their games. The URL is valid for 5 minutes.
     * <p>
     * For detail about analytics and the fields returned, see the Insights and Analytics guide.
     *
     * @param authToken Auth Token
     * @param after     Cursor for forward pagination: tells the server where to start fetching the next set of results, in a multi-page response.
     * @param limit     Maximum number of objects to return. Maximum: 100. Default: 20.
     * @param gameId    Game ID. If this is specified, the returned URL points to an analytics report for just the specified game.
     * @param type      Type of analytics report that is returned. If this is specified, the response includes one URL, for the specified report type. If this is not specified, the response includes multiple URLs (paginated), one for each report type available for the authenticated user’s Extensions. Limit: 1. Valid values: "overview_v1", "overview_v2".
     * @param startedAt Starting date/time for returned reports, in RFC3339 format with the hours, minutes, and seconds zeroed out and the UTC timezone: YYYY-MM-DDT00:00:00Z.
     * @param endedAt   Ending date/time for returned reports, in RFC3339 format with the hours, minutes, and seconds zeroed out and the UTC timezone: YYYY-MM-DDT00:00:00Z.
     * @return GameAnalyticsList
     */
    @RequestLine("GET /analytics/games?after={after}&ended_at={ended_at}&first={first}&game_id={game_id}&started_at={started_at}&type={type}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<GameAnalyticsList> getGameAnalyticUrl(
        @Param("token") String authToken,
        @Param("after") String after,
        @Param("first") Integer limit,
        @Param("game_id") String gameId,
        @Param("type") String type,
        @Param("started_at") String startedAt,
        @Param("ended_at") String endedAt
    );

    /**
     * Gets a ranked list of Bits leaderboard information for an authorized broadcaster.
     *
     * @param authToken Auth Token
     * @param count     Number of results to be returned. Maximum: 100. Default: 10.
     * @param period    Time period over which data is aggregated (PST time zone). This parameter interacts with started_at. Valid values are given below. Default: "all".
     * @param startedAt Timestamp for the period over which the returned data is aggregated. Must be in RFC 3339 format. If this is not provided, data is aggregated over the current period; e.g., the current day/week/month/year. This value is ignored if period is "all".
     * @param userId    ID of the user whose results are returned; i.e., the person who paid for the Bits.
     * @return StreamList
     */
    @RequestLine("GET /bits/leaderboard?count={count}&period={period}&started_at={started_at}&user_id={user_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<BitsLeaderboard> getBitsLeaderboard(
        @Param("token") String authToken,
        @Param("count") String count,
        @Param("period") String period,
        @Param("started_at") String startedAt,
        @Param("user_id") String userId
    );

    /**
     * Creates a clip programmatically. This returns both an ID and an edit URL for the new clip.
     *
     * @param authToken     Auth Token
     * @param broadcasterId ID of the stream from which the clip will be made.
     * @param hasDelay      If false, the clip is captured from the live stream when the API is called; otherwise, a delay is added before the clip is captured (to account for the brief delay between the broadcaster’s stream and the viewer’s experience of that stream). Default: false.
     * @return CreateClip
     */
    @RequestLine("POST /clips?broadcaster_id={broadcaster_id}&has_delay={has_delay}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<CreateClipList> createClip(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("has_delay") Boolean hasDelay
    );

    /**
     * Gets clip information by clip ID (one or more), broadcaster ID (one only), or game ID (one only).
     *
     * @param broadcasterId ID of the broadcaster for whom clips are returned. The number of clips returned is determined by the first query-string parameter (default: 20). Results are ordered by view count.
     * @param gameId        ID of the game for which clips are returned. The number of clips returned is determined by the first query-string parameter (default: 20). Results are ordered by view count.
     * @param id            ID of the clip being queried. Limit: 100.
     * @param after         Cursor for forward pagination: tells the server where to start fetching the next set of results, in a multi-page response.
     * @param before        Cursor for backward pagination: tells the server where to start fetching the next set of results, in a multi-page response.
     * @param limit         Maximum number of objects to return. Maximum: 100. Default: 20.
     * @param startedAt     Starting date/time for returned clips, in RFC3339 format. (Note that the seconds value is ignored.)
     * @param endedAt       Ending date/time for returned clips, in RFC3339 format. (Note that the seconds value is ignored.)
     * @return ClipList Clip List
     */
    @RequestLine("GET /clips?broadcaster_id={broadcaster_id}&game_id={game_id}&id={id}&after={after}&before={before}&first={first}&started_at={started_at}&ended_at={ended_at}")
    HystrixCommand<ClipList> getClips(
        @Param("broadcaster_id") String broadcasterId,
        @Param("game_id") String gameId,
        @Param("id") String id,
        @Param("after") String after,
        @Param("before") String before,
        @Param("first") Integer limit,
        @Param("started_at") Date startedAt,
        @Param("ended_at") Date endedAt
    );

    /**
     * TODO: Create Entitlement Grants Upload URL
     */

    /**
     * Gets game information by game ID or name.
     *
     * @param id Game ID. At most 100 id values can be specified.
     * @param name Game name. The name must be an exact match. For instance, “Pokemon” will not return a list of Pokemon games; instead, query the specific Pokemon game(s) in which you are interested. At most 100 name values can be specified.
     * @return GameList
     */
    @RequestLine("GET /games?id={id}&name={name}")
    HystrixCommand<GameList> getGames(
        @Param("id") List<String> id,
        @Param("name") List<String> name
    );

    /**
     * Gets games sorted by number of current viewers on Twitch, most popular first.
     *
     * @param after Cursor for forward pagination: tells the server where to start fetching the next set of results, in a multi-page response. The cursor value specified here is from the pagination response field of a prior query.
     * @param before Cursor for backward pagination: tells the server where to start fetching the next set of results, in a multi-page response. The cursor value specified here is from the pagination response field of a prior query.
     * @param first Maximum number of objects to return. Maximum: 100. Default: 20.
     * @return GameList
     */
    @RequestLine("GET /games/top?after={after}&before={before}&first={first}")
    HystrixCommand<GameTopList> getTopGames(
        @Param("after") String after,
        @Param("before") String before,
        @Param("first") String first
    );

    /**
     * Gets information about active streams. Streams are returned sorted by number of current viewers, in descending order. Across multiple pages of results, there may be duplicate or missing streams, as viewers join and leave streams.
     *
     * @param after       Cursor for forward pagination: tells the server where to start fetching the next set of results, in a multi-page response. The cursor value specified here is from the pagination response field of a prior query.
     * @param before      Cursor for backward pagination: tells the server where to start fetching the next set of results, in a multi-page response. The cursor value specified here is from the pagination response field of a prior query.
     * @param limit       Maximum number of objects to return. Maximum: 100. Default: 20.
     * @param communityId Returns streams in a specified community ID. You can specify up to 100 IDs.
     * @param gameIds     Returns streams broadcasting a specified game ID. You can specify up to 100 IDs.
     * @param language    Stream language. You can specify up to 100 languages.
     * @param userIds     Returns streams broadcast by one or more specified user IDs. You can specify up to 100 IDs.
     * @param userLogins  Returns streams broadcast by one or more specified user login names. You can specify up to 100 names.
     * @return StreamList
     */
    @RequestLine("GET /streams?after={after}&before={before}&community_id={community_id}&first={first}&game_id={game_id}&language={language}&user_id={user_id}&user_login={user_login}")
    HystrixCommand<StreamList> getStreams(
        @Param("after") String after,
        @Param("before") String before,
        @Param("first") Integer limit,
        @Param("community_id") List<UUID> communityId,
        @Param("game_id") List<String> gameIds,
        @Param("language") String language,
        @Param("user_id") List<String> userIds,
        @Param("user_login") List<String> userLogins
    );

    /**
     * Gets metadata information about active streams playing Overwatch or Hearthstone. Streams are sorted by number of current viewers, in descending order. Across multiple pages of results, there may be duplicate or missing streams, as viewers join and leave streams.
     *
     * @param after       Cursor for forward pagination: tells the server where to start fetching the next set of results, in a multi-page response. The cursor value specified here is from the pagination response field of a prior query.
     * @param before      Cursor for backward pagination: tells the server where to start fetching the next set of results, in a multi-page response. The cursor value specified here is from the pagination response field of a prior query.
     * @param limit       Maximum number of objects to return. Maximum: 100. Default: 20.
     * @param communityId Returns streams in a specified community ID. You can specify up to 100 IDs.
     * @param gameIds     Returns streams broadcasting a specified game ID. You can specify up to 100 IDs.
     * @param language    Stream language. You can specify up to 100 languages.
     * @param userIds     Returns streams broadcast by one or more specified user IDs. You can specify up to 100 IDs.
     * @param userLogins  Returns streams broadcast by one or more specified user login names. You can specify up to 100 names.
     * @return StreamMetadataList
     */
    @RequestLine("GET /streams/metadata?after={after}&before={before}&community_id={community_id}&first={first}&game_id={game_id}&language={language}&user_id={user_id}&user_login={user_login}")
    HystrixCommand<StreamMetadataList> getStreamsMetadata(
        @Param("after") String after,
        @Param("before") String before,
        @Param("first") Integer limit,
        @Param("community_id") List<UUID> communityId,
        @Param("game_id") List<String> gameIds,
        @Param("language") String language,
        @Param("user_id") List<String> userIds,
        @Param("user_login") List<String> userLogins
    );

    /**
     * TODO: Create Stream Marker
     */

    /**
     * Gets a list of markers for either a specified user’s most recent stream or a specified VOD/video (stream), ordered by recency. A marker is an arbitrary point in a stream that the broadcaster wants to mark; e.g., to easily return to later. The only markers returned are those created by the user identified by the Bearer token.
     *
     * @param authToken User Auth Token
     * @param after     Cursor for forward pagination: tells the server where to start fetching the next set of results, in a multi-page response. The cursor value specified here is from the pagination response field of a prior query.
     * @param before    Cursor for backward pagination: tells the server where to start fetching the next set of results, in a multi-page response. The cursor value specified here is from the pagination response field of a prior query.
     * @param limit     Maximum number of objects to return. Maximum: 100. Default: 20.
     * @param userId    ID of the broadcaster from whose stream markers are returned.
     * @param videoId   ID of the VOD/video whose stream markers are returned.
     * @return StreamMarkersList
     */
    @RequestLine("GET /streams/markers?after={after}&before={before}&first={first}&user_id={user_id}&video_id={video_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<StreamMarkersList> getStreamMarkers(
        @Param("token") String authToken,
        @Param("after") String after,
        @Param("before") String before,
        @Param("first") Integer limit,
        @Param("user_id") String userId,
        @Param("video_id") String videoId
    );

    /**
     * Get Users
     * <p>
     * Gets information about one or more specified Twitch users. Users are identified by optional user IDs and/or login name. If neither a user ID nor a login name is specified, the user is looked up by Bearer token.
     *
     * @param authToken Auth Token, optional, will include the users email address
     * @param userIds   User ID. Multiple user IDs can be specified. Limit: 100.
     * @param userNames User login name. Multiple login names can be specified. Limit: 100.
     * @return HelixUser
     */
    @RequestLine("GET /users?id={id}&login={login}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<UserList> getUsers(
        @Param("token") String authToken,
        @Param("id") List<String> userIds,
        @Param("login") List<String> userNames
    );

    /**
     * Get Followers
     * <p>
     * Gets information on follow relationships between two Twitch users. Information returned is sorted in order, most recent follow first. This can return information like “who is lirik following,” “who is following lirik,” or “is user X following user Y.”
     *
     * @param fromId User ID. The request returns information about users who are being followed by the from_id user.
     * @param toId   User ID. The request returns information about users who are following the to_id user.
     * @param after  Cursor for forward pagination: tells the server where to start fetching the next set of results, in a multi-page response.
     * @param limit  Maximum number of objects to return. Maximum: 100. Default: 20.
     * @return FollowList
     */
    @RequestLine("GET /users/follows?from_id={from_id}&to_id={to_id}&after={after}&first={first}")
    HystrixCommand<FollowList> getFollowers(
        @Param("from_id") String fromId,
        @Param("to_id") String toId,
        @Param("after") String after,
        @Param("first") Integer limit
    );

    /**
     * Update User
     * <p>
     * Updates the description of a user specified by a Bearer token.
     * Requires scope: user:edit
     *
     * @param authToken   Auth Token
     * @param description New user description
     * @return UserList
     */
    @RequestLine("PUT /users?description={description}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<UserList> updateUser(
        @Param("token") String authToken,
        @Param("description") String description
    );

    /**
     * Get User Extensions
     * <p>
     * Gets a list of all extensions (both active and inactive) for a specified user, identified by a Bearer token. The response has a JSON payload with a data field containing an array of user-information elements.
     * Required scope: user:read:broadcast
     *
     * @param authToken Auth Token
     * @return ExtensionList
     */
    @RequestLine("GET /users/extensions/list")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<ExtensionList> getUserExtensions(
        @Param("token") String authToken
    );

    /**
     * Get User Active Extensions
     * <p>
     * Gets information about active extensions installed by a specified user, identified by a user ID or Bearer token.
     * Optional scope: user:read:broadcast or user:edit:broadcast
     *
     * @param authToken Auth Token
     * @param userId    ID of the user whose installed extensions will be returned. Limit: 1.
     * @return ExtensionActiveList
     */
    @RequestLine("GET /users/extensions?user_id={user_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<ExtensionActiveList> getUserActiveExtensions(
        @Param("token") String authToken,
        @Param("user_id") String userId
    );

    /**
     * TODO: Update User Extensions
     */

    /**
     * Get Videos
     * <p>
     * Gets video information by video ID (one or more), user ID (one only), or game ID (one only).
     * The response has a JSON payload with a data field containing an array of video elements. For lookup by user or game, pagination is available, along with several filters that can be specified as query string parameters.
     *
     * @param id       ID of the video being queried. Limit: 100. If this is specified, you cannot use any of the optional query string parameters below.
     * @param userId   ID of the user who owns the video. Limit 1.
     * @param gameId   ID of the game the video is of. Limit 1.
     * @param language Language of the video being queried. Limit: 1.
     * @param period   Period during which the video was created. Valid values: "all", "day", "week", "month". Default: "all".
     * @param sort     Sort order of the videos. Valid values: "time", "trending", "views". Default: "time".
     * @param type     Type of video. Valid values: "all", "upload", "archive", "highlight". Default: "all".
     * @param after    Cursor for forward pagination: tells the server where to start fetching the next set of results, in a multi-page response.
     * @param before   Cursor for backward pagination: tells the server where to start fetching the next set of results, in a multi-page response.
     * @param limit    Number of values to be returned when getting videos by user or game ID. Limit: 100. Default: 20.
     * @return VideoList
     */
    @RequestLine("GET /videos?id={id}&user_id={user_id}&game_id={game_id}&language={language}&period={period}&sort={sort}&type={type}&after={after}&before={before}&first={first}")
    HystrixCommand<VideoList> getVideos(
        @Param("id") String id,
        @Param("user_id") String userId,
        @Param("game_id") String gameId,
        @Param("language") String language,
        @Param("period") String period,
        @Param("sort") String sort,
        @Param("type") String type,
        @Param("after") String after,
        @Param("before") String before,
        @Param("first") Integer limit
    );

    /**
     * TODO: Get Webhook Subscriptions
     */

}
