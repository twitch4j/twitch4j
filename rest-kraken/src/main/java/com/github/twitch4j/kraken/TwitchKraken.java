package com.github.twitch4j.kraken;

import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.kraken.domain.*;
import com.netflix.hystrix.HystrixCommand;
import feign.Body;
import feign.CollectionFormat;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.function.Supplier;

/**
 * Twitch - Kraken API
 * <p>
 * Kraken is already deprecated, so we only offer methods which haven't been added to the new helix api yet. Please use the helix api if available.
 */
public interface TwitchKraken {

    /**
     * The default baseUrl to pass to {@link TwitchKraken#uploadVideoPart(URI, String, String, int, byte[])} and {@link TwitchKraken#completeVideoUpload(URI, String, String)}
     */
    URI UPLOADS_BASE_URL = ((Supplier<URI>) () -> {
        // Not pretty but needed for "Overriding the Request Line" - see: https://github.com/OpenFeign/feign/blob/master/README.md#interface-annotations
        try {
            return new URI("https://uploads.twitch.tv");
        } catch (Exception e) {
            return null;
        }
    }).get();

    /**
     * Get Channel Editors
     * <p>
     * Gets a list of users who are editors for a specified channel.
     *
     * @param authToken User Access Token (scope: channel_read)
     * @param channelId The ID of the channel to retrieve editors from
     * @return {@link KrakenUserList}
     */
    @RequestLine("GET /channels/{channelId}/editors")
    @Headers({
        "Authorization: OAuth {token}",
        "Accept: application/vnd.twitchtv.v5+json"
    })
    HystrixCommand<KrakenUserList> getChannelEditors(
        @Param("token") String authToken,
        @Param("channelId") String channelId
    );

    /**
     * Get Channel Followers
     * <p>
     * Gets a list of users who follow a specified channel, sorted by the date when they started following the channel (newest first, unless specified otherwise).
     *
     * @param channelId Channel Id
     * @param limit     Maximum number of objects to return. Default: 25. Maximum: 100.
     * @param offset    Object offset for pagination of results. Default: 0.
     * @param cursor    Tells the server where to start fetching the next set of results, in a multi-page response.
     * @param direction Direction of sorting. Valid values: asc, desc (newest first). Default: desc.
     * @return {@link KrakenFollowList}
     */
    @RequestLine("GET /channels/{channelId}/follows?limit={limit}&offset={offset}&cursor={cursor}&direction={direction}")
    @Headers({
        "Accept: application/vnd.twitchtv.v5+json"
    })
    HystrixCommand<KrakenFollowList> getChannelFollowers(
        @Param("channelId") String channelId,
        @Param("limit") Integer limit,
        @Param("offset") Integer offset,
        @Param("cursor") String cursor,
        @Param("direction") String direction
    );

    /**
     * Reset Channel Stream Key
     * <p>
     * Deletes the stream key for a specified channel. Once it is deleted, the stream key is automatically reset.
     *
     * @param authToken User Access Token (scope: channel_stream)
     * @param channelId Channel Id
     * @return {@link KrakenChannel}
     */
    @RequestLine("DELETE /channels/{channelId}/stream_key")
    @Headers({
        "Authorization: OAuth {token}",
        "Accept: application/vnd.twitchtv.v5+json"
    })
    HystrixCommand<KrakenChannel> resetChannelStreamKey(
        @Param("token") String authToken,
        @Param("channelId") String channelId
    );

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
     * Approve Automod
     * <p>
     * Approve a message that was flagged by Automod
     *
     * @param authToken Auth Token
     * @param msgId unique id for the message
     * @return no content for a successful call
     */
    @Unofficial
    @RequestLine("POST /chat/twitchbot/approve")
    @Headers({
        "Authorization: OAuth {token}",
        "Accept: application/vnd.twitchtv.v5+json",
        "Content-Type: application/json"
    })
    @Body("%7B\"msg_id\":\"{msg_id}\"%7D")
    HystrixCommand<Void> approveAutomodMessage(
        @Param("token") String authToken,
        @Param("msg_id") String msgId
    );

    /**
     * Deny Automod
     * <p>
     * Deny a message that was flagged by Automod
     *
     * @param authToken Auth Token
     * @param msgId unique id for the message
     * @return no content for a successful call
     */
    @Unofficial
    @RequestLine("POST /chat/twitchbot/deny")
    @Headers({
        "Authorization: OAuth {token}",
        "Accept: application/vnd.twitchtv.v5+json",
        "Content-Type: application/json"
    })
    @Body("%7B\"msg_id\":\"{msg_id}\"%7D")
    HystrixCommand<Void> denyAutomodMessage(
        @Param("token") String authToken,
        @Param("msg_id") String msgId
    );

    /**
     * Get Clip
     * <p>
     * Gets details about a specified clip.
     *
     * @param slug The globally unique string to reference the clip
     * @return {@link KrakenClip}
     */
    @RequestLine("GET /clips/{slug}")
    @Headers({
        "Accept: application/vnd.twitchtv.v5+json"
    })
    HystrixCommand<KrakenClip> getClip(
        @Param("slug") String slug
    );

    /**
     * Get User Block List
     * <p>
     * Gets a specified user’s block list. List sorted by recency, newest first.
     *
     * @param authToken User Access Token (scope: user_blocks_read)
     * @param userId    The user ID associated with the token
     * @param limit     Maximum number of objects in array. Default: 25. Maximum: 100.
     * @param offset    Object offset for pagination. Default: 0.
     * @return {@link KrakenBlockList}
     */
    @RequestLine("GET /users/{user}/blocks?limit={limit}&offset={offset}")
    @Headers({
        "Authorization: OAuth {token}",
        "Accept: application/vnd.twitchtv.v5+json"
    })
    HystrixCommand<KrakenBlockList> getUserBlockList(
        @Param("token") String authToken,
        @Param("user") String userId,
        @Param("limit") Integer limit,
        @Param("offset") Integer offset
    );

    /**
     * Block User
     * <p>
     * Blocks a user; that is, adds a specified target user to the blocks list of a specified source user.
     *
     * @param authToken    User Access Token (scope: user_blocks_edit)
     * @param sourceUserId The ID of the user that is doing the blocking.
     * @param targetUserId The ID of the user that is getting blocked by the source.
     * @return {@link KrakenBlockTransaction}
     */
    @RequestLine("PUT /users/{from_id}/blocks/{to_id}")
    @Headers({
        "Authorization: OAuth {token}",
        "Accept: application/vnd.twitchtv.v5+json"
    })
    HystrixCommand<KrakenBlockTransaction> blockUser(
        @Param("token") String authToken,
        @Param("from_id") String sourceUserId,
        @Param("to_id") String targetUserId
    );

    /**
     * Unblock User
     * <p>
     * Unblocks a user; that is, deletes a specified target user from the blocks list of a specified source user.
     *
     * @param authToken    User Access Token (scope: user_blocks_edit)
     * @param sourceUserId The ID of the user that is doing the unblocking.
     * @param targetUserId The ID of the user that is getting unblocked by the source.
     * @return 204 No Content upon a successful request
     */
    @RequestLine("DELETE /users/{from_id}/blocks/{to_id}")
    @Headers({
        "Authorization: OAuth {token}",
        "Accept: application/vnd.twitchtv.v5+json"
    })
    HystrixCommand<Void> unblockUser(
        @Param("token") String authToken,
        @Param("from_id") String sourceUserId,
        @Param("to_id") String targetUserId
    );

    /**
     * Get User Emotes
     * <p>
     * Gets a list of the emojis and emoticons that the specified user can use in chat.
     * These are both the globally available ones and the channel-specific ones (which can be accessed by any user subscribed to the channel).
     *
     * @param authToken User Access Token (scope: user_subscriptions)
     * @param userId    The user ID associated with the token
     * @return {@link KrakenEmoticonSetList}
     */
    @RequestLine("GET /users/{user}/emotes")
    @Headers({
        "Authorization: OAuth {token}",
        "Accept: application/vnd.twitchtv.v5+json"
    })
    HystrixCommand<KrakenEmoticonSetList> getUserEmotes(
        @Param("token") String authToken,
        @Param("user") String userId
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
     * @deprecated in favor of TwitchHelix#createFollow
     */
    @Deprecated
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

    /**
     * Update title
     * <p>
     * Updates the title of a specified channel.
     *
     * @param authToken    Auth Token
     * @param channelId    Channel Id
     * @param title        New stream title
     * @return Object
     * @deprecated in favor of TwitchHelix#updateChannelInformation
     */
    @Deprecated
    @Headers({
        "Authorization: OAuth {token}",
        "Accept: application/vnd.twitchtv.v5+json",
    })
    @RequestLine("PUT /channels/{channelId}?channel[status]={title}")
    HystrixCommand<Object> updateTitle(
        @Param("token") String authToken,
        @Param("channelId") String channelId,
        @Param("title") String title
    );

    /**
     * Create Video
     * <p>
     * Creates a new video in a specified channel.
     * Videos with the following formats can be uploaded:
     * <ul>
     * <li>MP4, MOV, AVI and FLV file formats</li>
     * <li>AAC audio</li>
     * <li>h264 codec</li>
     * <li>Up to 10Mbps bitrate</li>
     * <li>Up to 1080p/60FPS</li>
     * </ul>
     * There is a rate limit of 5 simultaneous uploads per user, with a maximum of 100 uploads in 24 hours.
     *
     * @param authToken   Auth Token (scope: channel_editor)
     * @param channelId   Channel Id (Required)
     * @param title       Title of the video. Maximum 100 characters. (Required)
     * @param description Short description of the video. (Optional)
     * @param game        Name of the game in the video. (Optional)
     * @param language    Language of the video (for example, en). (Optional)
     * @param tags        Tags describing the video. Maximum: 100 characters per tag, 500 characters for the entire list. (Optional)
     * @param viewable    Specifies who can view the video. Valid values: public (the video is viewable by everyone) or private (the video is viewable only by the owner and channel editors).
     *                    Default: public. (Optional)
     * @param viewableAt  Date when the video will become public. This takes effect only if viewable=private. (Optional)
     * @return {@link KrakenCreatedVideo}
     */
    @RequestLine(
        value = "POST /videos?channel_id={channel_id}&title={title}&description={description}&game={game}&language={language}&tag_list={tag_list}&viewable={viewable}&viewable_at={viewable_at}",
        collectionFormat = CollectionFormat.CSV
    )
    @Headers({
        "Authorization: OAuth {token}",
        "Accept: application/vnd.twitchtv.v5+json"
    })
    HystrixCommand<KrakenCreatedVideo> createVideo(
        @Param("token") String authToken,
        @Param("channel_id") String channelId,
        @Param("title") String title,
        @Param("description") String description,
        @Param("game") String game,
        @Param("language") String language,
        @Param("tag_list") List<String> tags,
        @Param("viewable") String viewable,
        @Param("viewable_at") Instant viewableAt
    );

    @RequestLine("PUT /upload/{video_id}?part={part}&upload_token={upload_token}")
    @Headers({
        "Accept: application/vnd.twitchtv.v5+json",
        "Content-Type: application/x-www-form-urlencoded"
    })
    HystrixCommand<Void> uploadVideoPart(
        URI baseUrl,
        @Param("video_id") String videoId,
        @Param("upload_token") String uploadToken,
        @Param("part") int partIndex,
        byte[] videoPart
    );

    /**
     * Upload Video Part
     * <p>
     * Uploads part of a video. Each part of a video is uploaded with a separate request.
     *
     * @param videoId     The video id returned by {@link TwitchKraken#createVideo(String, String, String, String, String, String, List, String, Instant)}. (Required)
     * @param uploadToken The upload token returned by {@link TwitchKraken#createVideo(String, String, String, String, String, String, List, String, Instant)}. (Required)
     * @param partIndex   The location of the video part in the complete video. The value of this is 1-based. (Required)
     * @param videoPart   The body of the request is a byte[] that contains the video data. (Required)
     * @return 200 OK upon a successful part upload
     */
    default HystrixCommand<Void> uploadVideoPart(String videoId, String uploadToken, int partIndex, byte[] videoPart) {
        return uploadVideoPart(UPLOADS_BASE_URL, videoId, uploadToken, partIndex, videoPart);
    }

    @RequestLine("POST /upload/{video_id}/complete?upload_token={upload_token}")
    @Headers("Accept: application/vnd.twitchtv.v5+json")
    HystrixCommand<Void> completeVideoUpload(
        URI baseUrl,
        @Param("video_id") String videoId,
        @Param("upload_token") String uploadToken
    );

    /**
     * Complete Video Upload
     * <p>
     * After you upload all the parts of a video, you complete the upload process with this endpoint.
     *
     * @param videoId     The video id returned by {@link TwitchKraken#createVideo(String, String, String, String, String, String, List, String, Instant)}. (Required)
     * @param uploadToken The upload token returned by {@link TwitchKraken#createVideo(String, String, String, String, String, String, List, String, Instant)}. (Required)
     * @return 200 OK upon a successful POST
     */
    default HystrixCommand<Void> completeVideoUpload(String videoId, String uploadToken) {
        return completeVideoUpload(UPLOADS_BASE_URL, videoId, uploadToken);
    }

    /**
     * Update Video
     * <p>
     * Updates information about a specified video that was already created.
     *
     * @param authToken   Auth Token. (scope: channel_editor)
     * @param videoId     Video ID. (Required)
     * @param description Short description of the video. (Optional)
     * @param game        Name of the game in the video.
     * @param language    Language of the video (for example, en).
     * @param tags        Tags describing the video. Maximum: 100 characters per tag, 500 characters for the entire list.
     * @param title       tags describing the video (for example, “airplanes,scary”). Maximum: 100 characters per tag, 500 characters for the entire list.
     * @return {@link KrakenVideo}, the updated video
     */
    @RequestLine(
        value = "POST /videos/{video_id}?description={description}&game={game}&language={language}&tag_list={tag_list}&title={title}",
        collectionFormat = CollectionFormat.CSV
    )
    @Headers({
        "Authorization: OAuth {token}",
        "Accept: application/vnd.twitchtv.v5+json"
    })
    HystrixCommand<KrakenVideo> updateVideo(
        @Param("token") String authToken,
        @Param("video_id") String videoId,
        @Param("description") String description,
        @Param("game") String game,
        @Param("language") String language,
        @Param("tag_list") List<String> tags,
        @Param("title") String title
    );

    /**
     * Delete Video
     * <p>
     * Deletes a specified video (can be a VOD, highlight, or upload).
     *
     * @param authToken Auth Token. (scope: channel_editor)
     * @param videoId   Video ID. (Required)
     * @return 200 OK upon a successful call
     */
    @RequestLine("DELETE /videos/{video_id}")
    @Headers({
        "Authorization: OAuth {token}",
        "Accept: application/vnd.twitchtv.v5+json"
    })
    HystrixCommand<Void> deleteVideo(
        @Param("token") String authToken,
        @Param("video_id") String videoId
    );

}
