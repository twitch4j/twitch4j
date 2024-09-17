package com.github.twitch4j.helix;

import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.common.feign.JsonStringExpander;
import com.github.twitch4j.common.feign.ObjectToJsonExpander;
import com.github.twitch4j.eventsub.EventSubSubscription;
import com.github.twitch4j.eventsub.EventSubSubscriptionStatus;
import com.github.twitch4j.eventsub.ShardStatus;
import com.github.twitch4j.eventsub.domain.RedemptionStatus;
import com.github.twitch4j.eventsub.subscriptions.SubscriptionType;
import com.github.twitch4j.helix.domain.*;
import com.github.twitch4j.helix.interceptor.EventSubSubscriptionNameExpander;
import com.github.twitch4j.helix.webhooks.domain.WebhookRequest;
import com.netflix.hystrix.HystrixCommand;
import feign.Body;
import feign.CollectionFormat;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Twitch - Helix API
 */
public interface TwitchHelix {

    /**
     * The default baseUrl to pass to {@link #getIngestServers(URI)}.
     */
    URI INGESTS_BASE_URL = ((Supplier<URI>) () -> {
        // Not pretty but needed for "Overriding the Request Line" - see: https://github.com/OpenFeign/feign/blob/master/README.md#interface-annotations
        try {
            return new URI("https://ingest.twitch.tv/");
        } catch (Exception e) {
            return null;
        }
    }).get();

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
     * Retrieves the list of available Cheermotes, animated emotes to which viewers can assign Bits, to cheer in chat
     *
     * @param authToken Auth Token
     * @param broadcasterId ID for the broadcaster who might own specialized Cheermotes (optional)
     * @return CheermoteList
     */
    @RequestLine("GET /bits/cheermotes?broadcaster_id={broadcaster_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<CheermoteList> getCheermotes(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId
    );

    /**
     * Gets a list of Bits products that belongs to an Extension.
     *
     * @param authToken  App Access Token associated with the Extension client ID
     * @param includeAll Optional: Whether Bits products that are disabled/expired should be included in the response. Default: false
     * @return ExtensionBitsProductList
     */
    @RequestLine("GET /bits/extensions?should_include_all={should_include_all}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<ExtensionBitsProductList> getExtensionBitsProducts(
        @Param("token") String authToken,
        @Param("should_include_all") Boolean includeAll
    );

    /**
     * Add or update a Bits products that belongs to an Extension.
     * <p>
     * Required body fields: sku, cost.amount, cost.type, display_name.
     * Optional fields: in_development, expiration, is_broadcast.
     *
     * @param authToken App Access Token associated with the Extension client ID
     * @param product   The extension bits product to add or update
     * @return ExtensionBitsProductList
     */
    @RequestLine("PUT /bits/extensions")
    @Headers({
        "Authorization: Bearer {token}",
        "Content-Type: application/json"
    })
    HystrixCommand<ExtensionBitsProductList> updateExtensionBitsProduct(
        @Param("token") String authToken,
        ExtensionBitsProduct product
    );

    /**
     * Gets a ranked list of Bits leaderboard information for an authorized broadcaster.
     *
     * @param authToken Auth Token
     * @param count     Number of results to be returned. Maximum: 100. Default: 10.
     * @param period    Time period over which data is aggregated (PST time zone). This parameter interacts with started_at. Valid values are given below. Default: "all".
     * @param startedAt Timestamp for the period over which the returned data is aggregated. Must be in RFC 3339 format. If this is not provided, data is aggregated over the current period; e.g., the current day/week/month/year. This value is ignored if period is "all".
     * @param userId    ID of the user whose results are returned; i.e., the person who paid for the Bits.
     * @return BitsLeaderboard
     */
    @RequestLine("GET /bits/leaderboard?count={count}&period={period}&started_at={started_at}&user_id={user_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<BitsLeaderboard> getBitsLeaderboard(
        @Param("token") String authToken,
        @Param("count") Integer count,
        @Param("period") String period,
        @Param("started_at") String startedAt,
        @Param("user_id") String userId
    );

    /**
     * Gets a ranked list of Bits leaderboard information for an authorized broadcaster.
     *
     * @param authToken Auth Token
     * @param count     Number of results to be returned. Maximum: 100. Default: 10.
     * @param period    Time period over which data is aggregated (PST time zone). This parameter interacts with started_at. Valid values are given below. Default: "all".
     * @param startedAt Timestamp for the period over which the returned data is aggregated. Must be in RFC 3339 format. If this is not provided, data is aggregated over the current period; e.g., the current day/week/month/year. This value is ignored if period is "all".
     * @param userId    ID of the user whose results are returned; i.e., the person who paid for the Bits.
     * @return BitsLeaderboard
     * @deprecated utilize getBitsLeaderboard where count is an Integer
     */
    @Deprecated
    default HystrixCommand<BitsLeaderboard> getBitsLeaderboard(
        String authToken,
        String count,
        String period,
        String startedAt,
        String userId
    ) {
        return getBitsLeaderboard(authToken, count == null || count.isEmpty() ? null : Integer.parseInt(count), period, startedAt, userId);
    }

    /**
     * Creates a Custom Reward on a channel.
     * <p>
     * Query parameter broadcaster_id must match the user_id in the User-Access token.
     *
     * @param authToken     User access token for the broadcaster (scope: channel:manage:redemptions).
     * @param broadcasterId The id of the target channel, which must match the token user id.
     * @param newReward     The Custom Reward to be created.
     * @return CustomRewardList
     */
    @RequestLine("POST /channel_points/custom_rewards?broadcaster_id={broadcaster_id}")
    @Headers({
        "Authorization: Bearer {token}",
        "Content-Type: application/json"
    })
    HystrixCommand<CustomRewardList> createCustomReward(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        CustomReward newReward
    );

    /**
     * Deletes a Custom Reward on a channel.
     * <p>
     * Only rewards created programmatically by the same client_id can be deleted.
     * Any UNFULFILLED Custom Reward Redemptions of the deleted Custom Reward will be updated to the FULFILLED status.
     *
     * @param authToken     User access token for the broadcaster (scope: channel:manage:redemptions).
     * @param broadcasterId The id of the target channel, which must match the token user id.
     * @param rewardId      ID of the Custom Reward to delete, must match a Custom Reward on broadcaster_id’s channel.
     * @return 204 No Content upon a successful deletion
     */
    @RequestLine("DELETE /channel_points/custom_rewards?broadcaster_id={broadcaster_id}&id={id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<Void> deleteCustomReward(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("id") String rewardId
    );

    /**
     * Returns a list of Custom Reward objects for the Custom Rewards on a channel.
     * <p>
     * Developers only have access to update and delete rewards that were created programmatically by the same/calling client_id.
     * <p>
     * There is a limit of 50 Custom Rewards on a channel at a time. This includes both enabled and disabled Custom Rewards.
     *
     * @param authToken             User access token for the broadcaster (scope: channel:manage:redemptions).
     * @param broadcasterId         Required: The id of the target channel, which must match the token user id.
     * @param rewardIds             Optional: Filters the results to only returns reward objects for the Custom Rewards with matching ID. Maximum: 50.
     * @param onlyManageableRewards Optional: When set to true, only returns custom rewards that the calling client_id can manage. Default: false.
     * @return CustomRewardList
     */
    @RequestLine("GET /channel_points/custom_rewards?broadcaster_id={broadcaster_id}&id={id}&only_manageable_rewards={only_manageable_rewards}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<CustomRewardList> getCustomRewards(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("id") Collection<String> rewardIds,
        @Param("only_manageable_rewards") Boolean onlyManageableRewards
    );

    /**
     * Returns Custom Reward Redemption objects for a Custom Reward on a channel that was created by the same client_id.
     * <p>
     * Developers only have access to get and update redemptions for the rewards created programmatically by the same client_id.
     *
     * @param authToken     User access token for the broadcaster (scope: channel:manage:redemptions).
     * @param broadcasterId The id of the target channel, which must match the token user id.
     * @param rewardId      When ID is not provided, this parameter returns paginated Custom Reward Redemption objects for redemptions of the Custom Reward with ID reward_id.
     * @param redemptionIds When used, this param filters the results and only returns Custom Reward Redemption objects for the redemptions with matching ID. Maximum: 50.
     * @param status        When id is not provided, this param is required and filters the paginated Custom Reward Redemption objects for redemptions with the matching status.
     * @param sort          Sort order of redemptions returned when getting the paginated Custom Reward Redemption objects for a reward. One of: OLDEST, NEWEST. Default: OLDEST.
     * @param after         Cursor for forward pagination: tells the server where to start fetching the next set of results, in a multi-page response. This applies only to queries without ID.
     * @param limit         Number of results to be returned when getting the paginated Custom Reward Redemption objects for a reward. Maximum: 50. Default: 20.
     * @return CustomRewardRedemptionList
     */
    @RequestLine("GET /channel_points/custom_rewards/redemptions?broadcaster_id={broadcaster_id}&reward_id={reward_id}&id={id}&status={status}&sort={sort}&after={after}&first={first}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<CustomRewardRedemptionList> getCustomRewardRedemption(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("reward_id") String rewardId,
        @Param("id") Collection<String> redemptionIds,
        @Param("status") RedemptionStatus status,
        @Param("sort") String sort,
        @Param("after") String after,
        @Param("first") Integer limit
    );

    /**
     * Updates a Custom Reward created on a channel.
     * <p>
     * Only rewards created programmatically by the same client_id can be updated.
     *
     * @param authToken     User access token for the broadcaster (scope: channel:manage:redemptions).
     * @param broadcasterId The id of the target channel, which must match the token user id.
     * @param rewardId      ID of the Custom Reward to delete, must match a Custom Reward on broadcaster_id’s channel.
     * @param updatedReward A CustomReward object with specific field(s) updated.
     * @return CustomRewardList
     */
    @RequestLine("PATCH /channel_points/custom_rewards?broadcaster_id={broadcaster_id}&id={id}")
    @Headers({
        "Authorization: Bearer {token}",
        "Content-Type: application/json"
    })
    HystrixCommand<CustomRewardList> updateCustomReward(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("id") String rewardId,
        CustomReward updatedReward
    );

    /**
     * Updates the status of Custom Reward Redemption objects on a channel that are in the UNFULFILLED status.
     * <p>
     * Only redemptions for a reward created programmatically by the same client_id as attached to the access token can be updated.
     *
     * @param authToken     User access token for the broadcaster (scope: channel:manage:redemptions).
     * @param broadcasterId The id of the target channel, which must match the token user id.
     * @param rewardId      ID of the Custom Reward the redemptions to be updated are for.
     * @param redemptionIds ID of the Custom Reward Redemption to update, must match a Custom Reward Redemption on broadcaster_id’s channel. Max: 50.
     * @param newStatus     The new status to set redemptions to. Can be either FULFILLED or CANCELED. Updating to CANCELED will refund the user their points.
     * @return CustomRewardRedemptionList
     */
    @RequestLine("PATCH /channel_points/custom_rewards/redemptions?broadcaster_id={broadcaster_id}&reward_id={reward_id}&id={id}")
    @Headers({
        "Authorization: Bearer {token}",
        "Content-Type: application/json"
    })
    @Body("%7B\"status\":\"{status}\"%7D")
    HystrixCommand<CustomRewardRedemptionList> updateRedemptionStatus(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("reward_id") String rewardId,
        @Param("id") Collection<String> redemptionIds,
        @Param("status") RedemptionStatus newStatus
    );

    /**
     * Gets information about the charity campaign that a broadcaster is running,
     * such as their fundraising goal and the amount that’s been donated so far.
     * <p>
     * The ID in the broadcaster_id query parameter must match the user ID in the access token.
     * <p>
     * To receive events as donations occur, use {@link com.github.twitch4j.eventsub.subscriptions.SubscriptionTypes#CHANNEL_CHARITY_DONATE}.
     *
     * @param authToken     Broadcaster user access token with the channel:read:charity scope.
     * @param broadcasterId The ID of the broadcaster that’s actively running a charity campaign.
     * @return CharityCampaignWrapper
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_CHARITY_READ
     */
    @RequestLine("GET /charity/campaigns?broadcaster_id={broadcaster_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<CharityCampaignWrapper> getCharityCampaign(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId
    );

    /**
     * Gets the list of donations that users have made to the broadcaster’s active charity campaign.
     *
     * @param authToken     Broadcaster user access token with the channel:read:charity scope.
     * @param broadcasterId The ID of the broadcaster that’s actively running a charity campaign.
     * @param limit         The maximum number of items to return per page in the response. Minimum: 1. Maximum: 100. Default: 20.
     * @param after         The cursor used to get the next page of results.
     * @return CharityDonationList
     */
    @RequestLine("GET /charity/donations?broadcaster_id={broadcaster_id}&first={first}&after={after}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<CharityDonationList> getCharityDonations(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("first") Integer limit,
        @Param("after") String after
    );

    /**
     * Sends an announcement to the broadcaster’s chat room.
     *
     * @param authToken     User access token (scope: moderator:manage:announcements) of the broadcaster or a moderator.
     * @param broadcasterId The ID of the broadcaster that owns the chat room to send the announcement to.
     * @param moderatorId   The ID of a user who has permission to moderate the broadcaster’s chat room. This ID must match the user ID in the OAuth token, which can be a moderator or the broadcaster.
     * @param message       The announcement to make in the broadcaster’s chat room. Announcements are limited to a maximum of 500 characters.
     * @param color         The color used to highlight the announcement.
     * @return 204 No Content upon a successful call
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHAT_ANNOUNCEMENTS_MANAGE
     */
    @RequestLine("POST /chat/announcements?broadcaster_id={broadcaster_id}&moderator_id={moderator_id}")
    @Headers({
        "Authorization: Bearer {token}",
        "Content-Type: application/json"
    })
    @Body("%7B\"message\":\"{message}\",\"color\":\"{color}\"%7D")
    HystrixCommand<Void> sendChatAnnouncement(
        @Param("token") String authToken,
        @NotNull @Param("broadcaster_id") String broadcasterId,
        @NotNull @Param("moderator_id") String moderatorId,
        @NotNull @Param(value = "message", expander = JsonStringExpander.class) String message,
        @NotNull @Param("color") com.github.twitch4j.common.enums.AnnouncementColor color
    );

    /**
     * Sends an announcement to the broadcaster’s chat room.
     *
     * @param authToken     User access token (scope: moderator:manage:announcements) of the broadcaster or a moderator.
     * @param broadcasterId The ID of the broadcaster that owns the chat room to send the announcement to.
     * @param moderatorId   The ID of a user who has permission to moderate the broadcaster’s chat room. This ID must match the user ID in the OAuth token, which can be a moderator or the broadcaster.
     * @param message       The announcement to make in the broadcaster’s chat room. Announcements are limited to a maximum of 500 characters.
     * @param color         The color used to highlight the announcement.
     * @return 204 No Content upon a successful call
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHAT_ANNOUNCEMENTS_MANAGE
     * @deprecated in favor of {@link #sendChatAnnouncement(String, String, String, String, com.github.twitch4j.common.enums.AnnouncementColor)}
     */
    @Deprecated
    default HystrixCommand<Void> sendChatAnnouncement(String authToken, String broadcasterId, String moderatorId, String message, AnnouncementColor color) {
        return sendChatAnnouncement(authToken, broadcasterId, moderatorId, message, color.converted());
    }

    /**
     * Gets a list of custom chat badges that can be used in chat for the specified channel.
     * This includes <a href="https://help.twitch.tv/s/article/subscriber-badge-guide">subscriber badges</a> and <a href="https://help.twitch.tv/s/article/custom-bit-badges-guide">Bit badges</a>.
     *
     * @param authToken     App or User OAuth Token.
     * @param broadcasterId The id of the broadcaster whose chat badges are being requested.
     * @return ChatBadgeSetList
     */
    @RequestLine("GET /chat/badges?broadcaster_id={broadcaster_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<ChatBadgeSetList> getChannelChatBadges(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId
    );

    /**
     * Gets a list of chat badges that can be used in chat for any channel.
     *
     * @param authToken User OAuth Token or App Access Token.
     * @return ChatBadgeSetList
     */
    @RequestLine("GET /chat/badges/global")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<ChatBadgeSetList> getGlobalChatBadges(
        @Param("token") String authToken
    );

    /**
     * Gets the list of users that are connected to the specified broadcaster’s chat session.
     * <p>
     * This endpoint is in open beta, and could face breaking changes without warning.
     *
     * @param authToken     User access token that includes the moderator:read:chatters scope.
     * @param broadcasterId The ID of the broadcaster whose list of chatters you want to get.
     * @param moderatorId   The ID of the moderator or the specified broadcaster that’s requesting the list of chatters. This ID must match the user ID associated with the user access token.
     * @param limit         Optional: The maximum number of items to return per page in the response. Minimum: 1. Default: 100. Maximum: 1000.
     * @param after         Optional: The cursor used to get the next page of results.
     * @return ChattersList
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHATTERS_READ
     */
    @Unofficial
    @RequestLine("GET /chat/chatters?broadcaster_id={broadcaster_id}&moderator_id={moderator_id}&first={first}&after={after}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<ChattersList> getChatters(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("moderator_id") String moderatorId,
        @Param("first") Integer limit,
        @Param("after") String after
    );

    /**
     * Gets the color used for the user’s name in chat.
     *
     * @param authToken App access token or user access token.
     * @param userIds   The ID of the users whose color you want to get. Maximum: 100.
     * @return UserChatColorList
     * @see ChatUserColor#getColor()
     */
    @RequestLine("GET /chat/color?user_id={user_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<UserChatColorList> getUserChatColor(
        @Param("token") String authToken,
        @NotNull @Param("user_id") List<String> userIds
    );

    /**
     * Updates the color used for the user’s name in chat.
     * <p>
     * All users may specify one of the following named color values in {@link NamedUserChatColor}.
     * Turbo and Prime users may specify a named color or a Hex color code like #9146FF.
     *
     * @param authToken User access token that includes the user:manage:chat_color scope.
     * @param userId    The ID of the user whose chat color you want to update. This must match the user ID in the access token.
     * @param color     The color to use for the user’s name in chat.
     * @return 204 No Content upon a successful call
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_USER_COLOR_MANAGE
     */
    @RequestLine("PUT /chat/color?user_id={user_id}&color={color}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<Void> updateUserChatColor(
        @Param("token") String authToken,
        @NotNull @Param("user_id") String userId,
        @NotNull @Param("color") String color
    );

    /**
     * Gets all custom emotes for a specific Twitch channel including subscriber emotes, Bits tier emotes, and follower emotes.
     * <p>
     * Custom channel emotes are custom emoticons that viewers may use in Twitch chat once they are subscribed to, cheered in, or followed the channel that owns the emotes.
     *
     * @param authToken     Any User OAuth Token or App Access Token.
     * @param broadcasterId The broadcaster whose emotes are being requested.
     * @return EmoteList
     */
    @RequestLine("GET /chat/emotes?broadcaster_id={broadcaster_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<EmoteList> getChannelEmotes(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId
    );

    /**
     * Gets all global emotes.
     * <p>
     * Global emotes are Twitch-specific emoticons that every user can use in Twitch chat.
     *
     * @param authToken Any User OAuth Token or App Access Token.
     * @return EmoteList
     */
    @RequestLine("GET /chat/emotes/global")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<EmoteList> getGlobalEmotes(
        @Param("token") String authToken
    );

    /**
     * Gets all Twitch emotes for one or more specific emote sets.
     *
     * @param authToken Any User OAuth Token or App Access Token.
     * @param ids       IDs of the emote sets. Minimum: 1. Maximum: 25. Warning: at the time of writing, the enforced maximum is actually 10.
     * @return EmoteList
     */
    @RequestLine("GET /chat/emotes/set?emote_set_id={emote_set_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<EmoteList> getEmoteSets(
        @Param("token") String authToken,
        @Param("emote_set_id") Collection<String> ids
    );

    /**
     * Retrieves emotes available to the user across all channels.
     *
     * @param authToken     User access token (scope: user:read:emotes).
     * @param userId        The ID of the user. This ID must match the user ID in the user access token.
     * @param broadcasterId Returns all emotes available to the user within the chat owned by the specified broadcaster.
     *                      This includes the Global and Subscriber Emotes the user has access to,
     *                      as well as channel-only specific emotes such as Follower Emotes.
     *                      If the user specified in user_id is subscribed to the broadcaster specified, their follower emotes
     *                      will appear in the response body regardless if this query parameter is used.
     * @param cursor        The cursor used to get the next page of results.
     * @return {@link EmoteList}
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_USER_EMOTES_READ
     */
    @RequestLine("GET /chat/emotes/user?user_id={user_id}&broadcaster_id={broadcaster_id}&after={after}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<EmoteList> getUserEmotes(
        @Param("token") String authToken,
        @NotNull @Param("user_id") String userId,
        @Nullable @Param("broadcaster_id") String broadcasterId,
        @Nullable @Param("after") String cursor
    );

    /**
     * Sends a message to the broadcaster’s chat room.
     * <p>
     * If an app access token is used, the chatting user must have authorized to the Client ID with the user:bot scope,
     * and the broadcaster or a moderator must have authorized to the Client ID with the channel:bot scope.
     *
     * @param authToken User access token (scope: user:write:chat) or App access token (with Client ID authorized).
     * @param message   The message to be sent, including the source and target user IDs.
     * @return metadata about the send message attempt
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_USER_CHAT_WRITE
     * @see com.github.twitch4j.auth.domain.TwitchScopes#CHAT_CHANNEL_BOT
     * @see com.github.twitch4j.auth.domain.TwitchScopes#CHAT_USER_BOT
     */
    @RequestLine("POST /chat/messages")
    @Headers({
        "Authorization: Bearer {token}",
        "Content-Type: application/json"
    })
    HystrixCommand<SentChatMessageWrapper> sendChatMessage(
        @Param("token") String authToken,
        ChatMessage message
    );

    /**
     * Sends a Shoutout to the specified broadcaster.
     * <p>
     * Rate Limits: The broadcaster may send a Shoutout once every 2 minutes.
     * They may send the same broadcaster a Shoutout once every 60 minutes.
     * Note: these limits are not currently implemented by the library.
     *
     * @param authToken         A user access token that includes the moderator:manage:shoutouts scope.
     * @param fromBroadcasterId The ID of the broadcaster that’s sending the Shoutout.
     * @param toBroadcasterId   The ID of the broadcaster that’s receiving the Shoutout.
     * @param moderatorId       The ID of the broadcaster or a user that is one of the broadcaster’s moderators. This ID must match the user ID in the access token.
     * @return 204 No Content upon a successful request
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_SHOUTOUTS_MANAGE
     */
    @RequestLine("POST /chat/shoutouts?from_broadcaster_id={from_broadcaster_id}&to_broadcaster_id={to_broadcaster_id}&moderator_id={moderator_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<Void> sendShoutout(
        @Param("token") String authToken,
        @Param("from_broadcaster_id") String fromBroadcasterId,
        @Param("to_broadcaster_id") String toBroadcasterId,
        @Param("moderator_id") String moderatorId
    );

    /**
     * Gets the broadcaster’s chat settings.
     *
     * @param authToken     Required: OAuth access token. To read the non-moderator chat delay, a moderator's user access token must be specified with the moderator:read:chat_settings scope.
     * @param broadcasterId Required: The ID of the broadcaster whose chat settings you want to get.
     * @param moderatorId   Optional: The ID of a user that has permission to moderate the broadcaster’s chat room. Required only to access non moderator chat delay fields.
     * @return ChatSettingsWrapper
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHAT_SETTINGS_READ
     */
    @RequestLine("GET /chat/settings?broadcaster_id={broadcaster_id}&moderator_id={moderator_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<ChatSettingsWrapper> getChatSettings(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("moderator_id") String moderatorId
    );

    /**
     * Updates the broadcaster’s chat settings.
     *
     * @param authToken     Required: User access token (of the broadcaster or a moderator) with scope set to moderator:manage:chat_settings, associated with moderatorId.
     * @param broadcasterId Required: The ID of the broadcaster whose chat settings you want to update.
     * @param moderatorId   Required: The ID of a user that has permission to moderate the broadcaster’s chat room. Set this to the same value as broadcasterId if a broadcaster token is being used.
     * @param chatSettings  Required: The chat settings that you want to update.
     * @return ChatSettingsWrapper
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHAT_SETTINGS_MANAGE
     */
    @RequestLine("PATCH /chat/settings?broadcaster_id={broadcaster_id}&moderator_id={moderator_id}")
    @Headers({
        "Authorization: Bearer {token}",
        "Content-Type: application/json"
    })
    HystrixCommand<ChatSettingsWrapper> updateChatSettings(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("moderator_id") String moderatorId,
        ChatSettings chatSettings
    );

    /**
     * Gets the status of one or more provided codes.
     * <p>
     * The API is throttled to one request per second per authenticated user.
     *
     * @param authToken App access token. The client ID associated with the app access token must be approved by Twitch as part of a contracted arrangement.
     * @param code      The code to get the status of. 1-20 code parameters are allowed.
     * @param userId    Represents a numeric Twitch user ID. The user account which is going to receive the entitlement associated with the code.
     * @return CodeStatusList
     * @deprecated Twitch decommissioned this endpoint on 2023-02-27.
     */
    @Deprecated
    @RequestLine("GET /entitlements/codes?code={code}&user_id={user_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<CodeStatusList> getCodeStatus(
        @Param("token") String authToken,
        @Param("code") List<String> code,
        @Param("user_id") Integer userId
    );

    /**
     * Redeems one or more provided codes to the authenticated Twitch user.
     * <p>
     * The API is throttled to one request per second per authenticated user.
     *
     * @param authToken App access token. The client ID associated with the app access token must be one approved by Twitch.
     * @param code      The code to redeem to the authenticated user’s account. 1-20 code parameters are allowed.
     * @param userId    Represents a numeric Twitch user ID. The user account which is going to receive the entitlement associated with the code.
     * @return CodeStatusList
     * @deprecated Twitch decommissioned this endpoint on 2023-02-27.
     */
    @Deprecated
    @RequestLine("POST /entitlements/codes?code={code}&user_id={user_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<CodeStatusList> redeemCode(
        @Param("token") String authToken,
        @Param("code") List<String> code,
        @Param("user_id") Integer userId
    );

    /**
     * Gets a list of entitlements for a given organization that have been granted to a game, user, or both.
     * <p>
     * Valid combinations of requests are:
     * <ul>
     *     <li>No fields - All entitlements with benefits owned by your organization.</li>
     *     <li>Only userId - All entitlements for a user with benefits owned by your organization.</li>
     *     <li>Only gameId - All entitlements for all users for a game. Your organization must own the game.</li>
     *     <li>Both userId and gameId - All entitlements for the game granted to a user. Your organization must own the game.</li>
     * </ul>
     * <p>
     * Pagination support: Forward only
     *
     * @param authToken Required: App Access OAuth Token. OAuth Token Client ID must have ownership of Game: Client ID {@literal >} RBAC Organization ID {@literal >} Game ID.
     * @param id        Optional: Unique Identifier of the entitlement.
     * @param userId    Optional: A Twitch User ID.
     * @param gameId    Optional: A Twitch Game ID.
     * @param status    Optional: Fulfillment status used to filter entitlements.
     * @param after     Optional: The cursor used to fetch the next page of data.
     * @param limit     Optional: Maximum number of entitlements to return. Default: 20. Max: 1000.
     * @return DropsEntitlementList
     */
    @RequestLine("GET /entitlements/drops?id={id}&user_id={user_id}&game_id={game_id}&fulfillment_status={fulfillment_status}&after={after}&first={first}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<DropsEntitlementList> getDropsEntitlements(
        @Param("token") String authToken,
        @Param("id") String id,
        @Param("user_id") String userId,
        @Param("game_id") String gameId,
        @Param("fulfillment_status") DropFulfillmentStatus status,
        @Param("after") String after,
        @Param("first") Integer limit
    );

    @Deprecated
    default HystrixCommand<DropsEntitlementList> getDropsEntitlements(
        @Param("token") String authToken,
        @Param("id") String id,
        @Param("user_id") String userId,
        @Param("game_id") String gameId,
        @Param("after") String after,
        @Param("first") Integer limit
    ) {
        return getDropsEntitlements(authToken, id, userId, gameId, null, after, limit);
    }

    /**
     * Updates the fulfillment status on a set of Drops entitlements, specified by their entitlement IDs.
     *
     * @param authToken User OAuth Token or App Access Token where the client ID associated with the access token must have ownership of the game.
     * @param input     The fulfillment_status to assign to each of the entitlement_ids.
     * @return UpdatedDropEntitlementsList
     */
    @RequestLine("PATCH /entitlements/drops")
    @Headers({
        "Authorization: Bearer {token}",
        "Content-Type: application/json"
    })
    HystrixCommand<UpdatedDropEntitlementsList> updateDropsEntitlements(
        @Param("token") String authToken,
        UpdateDropEntitlementInput input
    );

    /**
     * Creates a new EventSub conduit.
     *
     * @param authToken  App access token.
     * @param shardCount The number of shards to create for this conduit.
     * @return ConduitList
     */
    @RequestLine("POST /eventsub/conduits")
    @Headers({
        "Authorization: Bearer {token}",
        "Content-Type: application/json"
    })
    @Body("%7B\"shard_count\":{shard_count}%7D")
    HystrixCommand<ConduitList> createConduit(
        @Param("token") String authToken,
        @Param("shard_count") int shardCount
    );

    /**
     * Gets the conduits for a client ID.
     *
     * @param authToken App access token.
     * @return ConduitList
     */
    @RequestLine("GET /eventsub/conduits")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<ConduitList> getConduits(
        @Param("token") String authToken
    );

    /**
     * Updates a conduit’s shard count.
     * <p>
     * To delete shards, update the count to a lower number, and the shards above the count will be deleted.
     * For example, if the existing shard count is 100, by resetting shard count to 50, shards 50-99 are disabled.
     *
     * @param authToken  App access token.
     * @param conduitId  The ID of the conduit to update.
     * @param shardCount The updated shard count for the conduit.
     * @return ConduitList
     */
    @RequestLine("PATCH /eventsub/conduits")
    @Headers({
        "Authorization: Bearer {token}",
        "Content-Type: application/json"
    })
    @Body("%7B\"id\":\"{id}\",\"shard_count\":{shard_count}%7D")
    HystrixCommand<ConduitList> updateConduit(
        @Param("token") String authToken,
        @NotNull @Param("id") String conduitId,
        @Param("shard_count") int shardCount
    );

    /**
     * Deletes a specified conduit.
     * <p>
     * Note that it may take some time for Eventsub subscriptions on a deleted conduit
     * to show as disabled when calling Get Eventsub Subscriptions.
     *
     * @param authToken App access token.
     * @param conduitId The ID of the conduit to delete.
     * @return 204 No Content upon a successful deletion.
     */
    @RequestLine("DELETE /eventsub/conduits?id={id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<Void> deleteConduit(
        @Param("token") String authToken,
        @NotNull @Param("id") String conduitId
    );

    /**
     * Gets a lists of all shards for a conduit.
     *
     * @param authToken App access token.
     * @param conduitId The Conduit ID.
     * @param status    Status to filter by.
     * @param after     The cursor used to get the next page of results.
     * @return ConduitShardList
     */
    @RequestLine("GET /eventsub/conduits/shards?conduit_id={conduit_id}&status={status}&after={after}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<ConduitShardList> getConduitShards(
        @Param("token") String authToken,
        @NotNull @Param("conduit_id") String conduitId,
        @Nullable @Param("status") ShardStatus status,
        @Nullable @Param("after") String after
    );

    /**
     * Updates shard(s) for a conduit.
     * <p>
     * NOTE: Shard IDs are indexed starting at 0,
     * so a conduit with a {@code shard_count} of 5 will have shards with IDs 0 through 4.
     *
     * @param authToken App access token.
     * @param shards    The shards to update.
     * @return ConduitShardList
     */
    @RequestLine("PATCH /eventsub/conduits/shards")
    @Headers({
        "Authorization: Bearer {token}",
        "Content-Type: application/json"
    })
    HystrixCommand<ConduitShardList> updateConduitShards(
        @Param("token") String authToken,
        @NotNull ShardsInput shards
    );

    /**
     * Creates an EventSub subscription.
     *
     * @param authToken    Required: App access token for Webhooks, User access token for Websockets.
     * @param subscription Required: The subscription that is being created. Must include type, version, condition, and transport.
     * @return EventSubSubscriptionList
     */
    @RequestLine("POST /eventsub/subscriptions")
    @Headers({
        "Authorization: Bearer {token}",
        "Content-Type: application/json"
    })
    HystrixCommand<EventSubSubscriptionList> createEventSubSubscription(
        @Param("token") String authToken,
        EventSubSubscription subscription
    );

    /**
     * Delete an EventSub subscription.
     *
     * @param authToken      Required: App Access Token.
     * @param subscriptionId Required: The subscription ID for the subscription you want to delete.
     * @return 204 No Content upon a successful deletion
     */
    @RequestLine("DELETE /eventsub/subscriptions?id={id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<Void> deleteEventSubSubscription(
        @Param("token") String authToken,
        @Param("id") String subscriptionId
    );

    /**
     * Get a list of your EventSub subscriptions.
     * <p>
     * The list is paginated and ordered by the oldest subscription first.
     * <p>
     * Use the status, type, and user_id query parameters to filter the list of subscriptions that are returned.
     * The filters are mutually exclusive; the request fails if you specify more than one filter.
     *
     * @param authToken Required: App Access Token.
     * @param status    Optional: Include this parameter to filter subscriptions by their status.
     * @param type      Optional: Include this parameter to filter subscriptions by subscription type.
     * @param userId    Optional: Include this parameter to filter subscriptions by user ID in the condition object.
     * @param after     Optional: Cursor for forward pagination.
     * @param limit     Optional: Maximum number of objects to return. Maximum: 100. Minimum: 10.
     * @return EventSubSubscriptionList
     */
    @RequestLine("GET /eventsub/subscriptions?status={status}&type={type}&user_id={user_id}&after={after}&first={first}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<EventSubSubscriptionList> getEventSubSubscriptions(
        @Param("token") String authToken,
        @Param("status") EventSubSubscriptionStatus status,
        @Param(value = "type", expander = EventSubSubscriptionNameExpander.class) SubscriptionType<?, ?, ?> type,
        @Param("user_id") String userId,
        @Param("after") String after,
        @Param("first") Integer limit
    );

    /**
     * Get a list of your EventSub subscriptions.
     *
     * @param authToken Required: App Access Token.
     * @param status    Optional: Include this parameter to filter subscriptions by their status.
     * @param after     Optional: Cursor for forward pagination.
     * @param limit     Optional: Maximum number of objects to return. Maximum: 100. Minimum: 10.
     * @return EventSubSubscriptionList
     * @deprecated in favor of {@link #getEventSubSubscriptions(String, EventSubSubscriptionStatus, SubscriptionType, String, String, Integer)}
     */
    @Deprecated
    default HystrixCommand<EventSubSubscriptionList> getEventSubSubscriptions(
        @Param("token") String authToken,
        @Param("status") EventSubSubscriptionStatus status,
        @Param("after") String after,
        @Param("first") Integer limit
    ) {
        return getEventSubSubscriptions(authToken, status, null, null, after, limit);
    }

    /**
     * Gets information about your Extensions; either the current version or a specified version.
     *
     * @param jwtToken         Signed JWT with role set to "external".
     * @param extensionId      ID of the Extension.
     * @param extensionVersion The specific version of the Extension to return. If not provided, the current version is returned.
     * @return ReleasedExtensionList
     */
    @RequestLine("GET /extensions?extension_id={extension_id}&extension_version={extension_version}")
    @Headers({
        "Authorization: Bearer {token}",
        "Client-Id: {extension_id}"
    })
    HystrixCommand<ReleasedExtensionList> getExtensions(
        @Param("token") String jwtToken,
        @Param("extension_id") String extensionId,
        @Param("extension_version") String extensionVersion
    );

    /**
     * Sends a specified chat message to a specified channel.
     * <p>
     * The message will appear in the channel’s chat as a normal message.
     * The “username” of the message is the Extension name.
     * <p>
     * There is a limit of 12 messages per minute, per channel.
     *
     * @param jwtToken         Signed JWT with user_id and role (set to "external").
     * @param extensionId      Client ID associated with the Extension.
     * @param extensionVersion Version of the Extension sending this message.
     * @param broadcasterId    User ID of the broadcaster whose channel has the Extension activated.
     * @param text             Message for Twitch chat. Maximum: 280 characters.
     * @return 204 No Content upon a successful request
     */
    @RequestLine("POST /extensions/chat?broadcaster_id={broadcaster_id}")
    @Headers({
        "Authorization: Bearer {token}",
        "Client-Id: {extension_id}",
        "Content-Type: application/json"
    })
    @Body("%7B\"extension_id\":\"{extension_id}\",\"extension_version\":\"{extension_version}\",\"text\":\"{text}\"%7D")
    HystrixCommand<Void> sendExtensionChatMessage(
        @Param("token") String jwtToken,
        @Param("extension_id") String extensionId,
        @Param("extension_version") String extensionVersion,
        @Param("broadcaster_id") String broadcasterId,
        @Param("text") String text
    );

    /**
     * Gets the specified configuration segment from the specified extension.
     * <p>
     * You can retrieve each segment a maximum of 20 times per minute.
     * If you exceed the limit, the request returns HTTP status code 429.
     *
     * @param jwtToken      Signed JWT with exp, user_id, and role (set to "external").
     * @param extensionId   The ID of the extension that contains the configuration segment you want to get.
     * @param segment       The type of configuration segment to get.
     * @param broadcasterId The ID of the broadcaster for the configuration returned. This parameter is required if you set the segment parameter to broadcaster or developer. Do not specify this parameter if you set segment to global.
     * @return ExtensionConfigurationSegmentList
     */
    @RequestLine("GET /extensions/configurations?broadcaster_id={broadcaster_id}&extension_id={extension_id}&segment={segment}")
    @Headers({
        "Authorization: Bearer {token}",
        "Client-Id: {extension_id}"
    })
    HystrixCommand<ExtensionConfigurationSegmentList> getExtensionConfigurationSegment(
        @Param("token") String jwtToken,
        @Param("extension_id") String extensionId,
        @Param("segment") List<ExtensionSegment> segment,
        @Param("broadcaster_id") String broadcasterId
    );

    /**
     * Sets a single configuration segment of any type.
     * <p>
     * Each segment is limited to 5 KB and can be set at most 20 times per minute.
     * Updates to this data are not delivered to Extensions that have already been rendered.
     *
     * @param jwtToken    Signed JWT with exp, user_id, and role (set to "external").
     * @param extensionId ID for the Extension which the configuration is for.
     * @param input       Segment configuration info.
     * @return 204 No Content upon a successful request.
     */
    @RequestLine("PUT /extensions/configurations")
    @Headers({
        "Authorization: Bearer {token}",
        "Client-Id: {extension_id}",
        "Content-Type: application/json"
    })
    HystrixCommand<Void> setExtensionConfigurationSegment(
        @Param("token") String jwtToken,
        @Param("extension_id") String extensionId,
        ExtensionConfigurationSegmentInput input
    );

    /**
     * Retrieves a specified Extension’s secret data consisting of a version and an array of secret objects.
     * <p>
     * Each secret object contains a base64-encoded secret, a UTC timestamp when the secret becomes active, and a timestamp when the secret expires.
     * <p>
     * Signed JWT created by an Extension Backend Service (EBS), following the requirements documented in Signing the JWT.
     * A signed JWT must include the exp, user_id, and role fields documented in JWT Schema, and role must be set to "external".
     *
     * @param jwtToken    Signed JWT with exp, user_id, and role (set to "external").
     * @param extensionId The Client ID associated with the extension.
     * @return ExtensionSecretsList
     */
    @RequestLine("GET /extensions/jwt/secrets?extension_id={extension_id}")
    @Headers({
        "Authorization: Bearer {token}",
        "Client-Id: {extension_id}"
    })
    HystrixCommand<ExtensionSecretsList> getExtensionSecrets(
        @Param("token") String jwtToken,
        @Param("extension_id") String extensionId
    );

    /**
     * Creates a JWT signing secret for a specific Extension.
     * <p>
     * Also rotates any current secrets out of service, with enough time for instances of the Extension to gracefully switch over to the new secret.
     * Use this function only when you are ready to install the new secret it returns.
     *
     * @param jwtToken    Signed JWT with exp, user_id, and role (set to "external").
     * @param extensionId The Client ID associated with the extension.
     * @param delay       Optional: JWT signing activation delay for the newly created secret in seconds. Minimum: 300. Default: 300.
     * @return ExtensionSecretsList
     */
    @RequestLine("POST /extensions/jwt/secrets?extension_id={extension_id}&delay={delay}")
    @Headers({
        "Authorization: Bearer {token}",
        "Client-Id: {extension_id}"
    })
    HystrixCommand<ExtensionSecretsList> createExtensionSecret(
        @Param("token") String jwtToken,
        @Param("extension_id") String extensionId,
        @Param("delay") Integer delay
    );

    /**
     * Returns one page of live channels that have installed or activated a specific Extension,
     * identified by a client ID value assigned to the Extension when it is created.
     * <p>
     * A channel that recently went live may take a few minutes to appear in this list,
     * and a channel may continue to appear on this list for a few minutes after it stops broadcasting.
     *
     * @param authToken   User OAuth Token or App Access Token
     * @param extensionId ID of the Extension to search for.
     * @param limit       Maximum number of objects to return. Maximum: 100. Default: 20.
     * @param after       The cursor used to fetch the next page of data.
     * @return ExtensionLiveChannelsList
     */
    @RequestLine("GET /extensions/live?extension_id={extension_id}&first={first}&after={after}&cursor={after}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<ExtensionLiveChannelsList> getExtensionLiveChannels(
        @Param("token") String authToken,
        @Param("extension_id") String extensionId,
        @Param("first") Integer limit,
        @Param("after") String after
    );

    @Deprecated // meant to only be called internally
    @SuppressWarnings("DeprecatedIsStillUsed")
    @RequestLine("POST /extensions/pubsub")
    @Headers({
        "Authorization: Bearer {token}",
        "Client-Id: {extension_id}",
        "Content-Type: application/json",
        "Twitch4J-Target: {twitch4j_target}"
    })
    HystrixCommand<Void> sendExtensionPubSubMessage(
        @Param("token") String jwtToken,
        @Param("extension_id") String extensionId,
        @Param("twitch4j_target") String target,
        SendPubSubMessageInput input
    );

    /**
     * Twitch provides a publish-subscribe system for your EBS to communicate with both the broadcaster and viewers.
     * Calling this endpoint forwards your message using the same mechanism as the send JavaScript helper function.
     * <p>
     * A message can be sent to either a specified channel or globally (all channels on which your extension is active).
     * <p>
     * Extension PubSub has a rate limit of 100 requests per minute for a combination of Extension client ID and broadcaster ID.
     * <p>
     * A signed JWT must include the channel_id and pubsub_perms fields documented in JWT Schema.
     *
     * @param jwtToken    Signed JWT with exp, user_id, role, channel_id, pubsub_perms.send
     * @param extensionId Client ID associated with the Extension.
     * @param input       Details on the message to be sent and its targets.
     * @return 204 No Content upon a successful request.
     */
    default HystrixCommand<Void> sendExtensionPubSubMessage(
        String jwtToken,
        @NotNull String extensionId,
        @NotNull SendPubSubMessageInput input
    ) {
        final String target = input.isGlobalBroadcast() ? SendPubSubMessageInput.GLOBAL_TARGET : input.getBroadcasterId() != null ? input.getBroadcasterId() : input.getTargets().get(0);
        return this.sendExtensionPubSubMessage(jwtToken, extensionId, target, input);
    }

    /**
     * Gets information about a released Extension; either the current version or a specified version.
     *
     * @param authToken        User OAuth Token or App Access Token
     * @param extensionId      ID of the Extension.
     * @param extensionVersion The specific version of the Extension to return. If not provided, the current version is returned.
     * @return ReleasedExtensionList
     */
    @RequestLine("GET /extensions/released?extension_id={extension_id}&extension_version={extension_version}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<ReleasedExtensionList> getReleasedExtensions(
        @Param("token") String authToken,
        @Param("extension_id") String extensionId,
        @Param("extension_version") String extensionVersion
    );

    /**
     * Enable activation of a specified Extension, after any required broadcaster configuration is correct.
     * <p>
     * This is for Extensions that require broadcaster configuration before activation.
     * Use this if, in Extension Capabilities, you select Custom/My Own Service.
     * <p>
     * You enforce required broadcaster configuration with a required_configuration string in the Extension manifest. The contents of this string can be whatever you want.
     * Once your EBS determines that the Extension is correctly configured on a channel, use this endpoint to provide that same configuration string, which enables activation on the channel.
     * The endpoint URL includes the channel ID of the page where the Extension is iframe embedded.
     * <p>
     * If a future version of the Extension requires a different configuration, change the required_configuration string in your manifest.
     * When the new version is released, broadcasters will be required to re-configure that new version.
     *
     * @param jwtToken             Signed JWT with exp, user_id, and role (set to "external").
     * @param extensionId          ID for the Extension to activate.
     * @param extensionVersion     The version fo the Extension to release.
     * @param configurationVersion The version of the configuration to use with the Extension.
     * @param broadcasterId        User ID of the broadcaster who has activated the specified Extension on their channel.
     * @return 204 No Content upon a successful request.
     */
    @RequestLine("PUT /extensions/required_configuration?broadcaster_id={broadcaster_id}")
    @Headers({
        "Authorization: Bearer {token}",
        "Client-Id: {extension_id}",
        "Content-Type: application/json"
    })
    @Body("%7B\"extension_id\":\"{extension_id}\",\"extension_version\":\"{extension_version}\",\"required_configuration\":\"{configuration_version}\",\"configuration_version\":\"{configuration_version}\"%7D")
    HystrixCommand<Void> setExtensionRequiredConfiguration(
        @Param("token") String jwtToken,
        @Param("extension_id") String extensionId,
        @Param("extension_version") String extensionVersion,
        @Param("configuration_version") String configurationVersion,
        @Param("broadcaster_id") String broadcasterId
    );

    /**
     * Get Extension Transactions allows extension back end servers to fetch a list of transactions that have occurred for their extension across all of Twitch.
     *
     * @param authToken App Access  OAuth Token
     * @param extensionId ID of the extension to list transactions for.
     * @param transactionIds Transaction IDs to look up. Can include multiple to fetch multiple transactions in a single request. Maximum: 100
     * @param after The cursor used to fetch the next page of data. This only applies to queries without ID. If an ID is specified, it supersedes the cursor.
     * @param limit Maximum number of objects to return. Maximum: 100 Default: 20
     * @return ExtensionTransactionList
     */
    @RequestLine("GET /extensions/transactions?after={after}&extension_id={extension_id}&first={first}&id={id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<ExtensionTransactionList> getExtensionTransactions(
        @Param("token") String authToken,
        @Param("extension_id") String extensionId,
        @Param("id") List<String> transactionIds,
        @Param("after") String after,
        @Param("first") Integer limit
    );

    /**
     * Returns a list of games or categories that match the query via name either entirely or partially
     *
     * @param authToken Auth Token
     * @param query the search query
     * @param limit Maximum number of objects to return. Maximum: 100. Default: 20.
     * @param after Cursor for forward pagination
     * @return CategorySearchList
     */
    @RequestLine("GET /search/categories?after={after}&first={first}&query={query}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<CategorySearchList> searchCategories(
        @Param("token") String authToken,
        @Param("query") String query,
        @Param("first") Integer limit,
        @Param("after") String after
    );

    /**
     * Gets channel information for users
     *
     * @param authToken Auth Token
     * @param broadcasterIds IDs of the channels to be retrieved (up to 100)
     * @return ChannelInformationList
     */
    @RequestLine("GET /channels?broadcaster_id={broadcaster_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<ChannelInformationList> getChannelInformation(
        @Param("token") String authToken,
        @Param("broadcaster_id") List<String> broadcasterIds
    );

    /**
     * Modifies channel information for users
     *
     * @param authToken Auth Token (scope: channel:manage:broadcast or user:edit:broadcast)
     * @param broadcasterId ID of the channel to be updated (required)
     * @param channelInformation {@link ChannelInformation} (at least one parameter must be provided)
     * @return 204 No Content upon a successful update
     */
    @RequestLine("PATCH /channels?broadcaster_id={broadcaster_id}")
    @Headers({
        "Authorization: Bearer {token}",
        "Content-Type: application/json"
    })
    HystrixCommand<Void> updateChannelInformation(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        ChannelInformation channelInformation
    );

    /**
     * Gets the channels that match the specified query and have streamed content within the past 6 months.
     * <p>
     * The fields that the API uses for comparison depends on the value that the {@code live_only} query parameter is set to.
     * If live_only is false, the API matches on the broadcaster’s login name.
     * However, if {@code live_only} is true, the API matches on the broadcaster’s name and category name.
     * <p>
     * To match, the beginning of the broadcaster’s name or category must match the query string.
     * The comparison is case-insensitive. If the query string is angel_of_death, it matches all names that begin with angel_of_death.
     * However, if the query string is a phrase like angel of death, it matches to names starting with angelofdeath or names starting with angel_of_death.
     * <p>
     * At the time of writing, {@code query} does <a href="https://github.com/twitchdev/issues/issues/545">not</a> search stream titles.
     *
     * @param authToken Auth Token
     * @param query the search query
     * @param limit Maximum number of objects to return. Maximum: 100. Default: 20.
     * @param after Cursor for forward pagination
     * @param liveOnly Filter results for live streams only. Default: false
     * @return ChannelSearchList
     */
    @RequestLine("GET /search/channels?after={after}&first={first}&live_only={live_only}&query={query}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<ChannelSearchList> searchChannels(
        @Param("token") String authToken,
        @Param("query") String query,
        @Param("first") Integer limit,
        @Param("after") String after,
        @Param("live_only") Boolean liveOnly
    );

    /**
     * Retrieves the active shared chat session for a channel.
     *
     * @param authToken App access token or user access token.
     * @param broadcasterId The User ID of the channel broadcaster.
     * @return {@link SharedChatSessionWrapper}
     */
    @Headers("Authorization: Bearer {token}")
    @RequestLine("GET /shared_chat/session?broadcaster_id={broadcaster_id}")
    HystrixCommand<SharedChatSessionWrapper> getSharedChatSession(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId
    );

    /**
     * Gets the Soundtrack track that the broadcaster is playing.
     * <p>
     * If the broadcaster is not playing a track, the endpoint returns HTTP status code 404 Not Found.
     *
     * @param authToken     App access token or User access token.
     * @param broadcasterId The ID of the broadcaster that’s playing a Soundtrack track.
     * @return SoundtrackCurrentTrackWrapper
     * @deprecated <a href="https://discuss.dev.twitch.tv/t/withdrawal-of-twitch-api-endpoints-for-soundtrack/">Twitch is decommissioning Soundtrack on 2023-07-17</a>
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    @RequestLine("GET /soundtrack/current_track?broadcaster_id={broadcaster_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<SoundtrackCurrentTrackWrapper> getSoundtrackCurrentTrack(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId
    );

    /**
     * Gets the tracks of a Soundtrack playlist.
     *
     * @param authToken Required: App access token or User access token.
     * @param id        Required: The ID of the Soundtrack playlist to get.
     * @param limit     Optional: The maximum number of tracks to return for this playlist in the response. The minimum number of tracks is 1 and the maximum is 50. The default is 20.
     * @param after     Optional: The cursor used to get the next page of tracks for this playlist. The Pagination object in the response contains the cursor’s value.
     * @return SoundtrackPlaylistTracksList
     * @deprecated <a href="https://discuss.dev.twitch.tv/t/withdrawal-of-twitch-api-endpoints-for-soundtrack/">Twitch is decommissioning Soundtrack on 2023-07-17</a>
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    @RequestLine("GET /soundtrack/playlist?id={id}&first={first}&after={after}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<SoundtrackPlaylistTracksList> getSoundtrackPlaylist(
        @Param("token") String authToken,
        @Param("id") String id,
        @Param("first") Integer limit,
        @Param("after") String after
    );

    /**
     * Gets a list of Soundtrack playlists.
     * <p>
     * The list contains information about the playlists, such as their titles and descriptions.
     * To get a playlist’s tracks, call {@link #getSoundtrackPlaylist(String, String, Integer, String)} and specify the playlist’s ID.
     *
     * @param authToken Required: App access token or User access token.
     * @param id        Optional: The ID of the Soundtrack playlist to get. Specify an ID only if you want to get a single playlist instead of all playlists.
     * @param limit     Optional: The maximum number of playlists to return in the response. The minimum number of playlists is 1 and the maximum is 50. The default is 20.
     * @param after     Optional: The cursor used to get the next page of playlists. The Pagination object in the response contains the cursor’s value.
     * @return SoundtrackPlaylistMetadataList
     * @deprecated <a href="https://discuss.dev.twitch.tv/t/withdrawal-of-twitch-api-endpoints-for-soundtrack/">Twitch is decommissioning Soundtrack on 2023-07-17</a>
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    @RequestLine("GET /soundtrack/playlists?id={id}&first={first}&after={after}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<SoundtrackPlaylistMetadataList> getSoundtrackPlaylists(
        @Param("token") String authToken,
        @Param("id") String id,
        @Param("first") Integer limit,
        @Param("after") String after
    );

    /**
     * Obtains ad schedule related information, including snooze, when the last ad was run,
     * when the next ad is scheduled, and if the channel is currently in pre-roll free time.
     *
     * @param authToken     User access token with the channel:read:ads scope from the broadcaster.
     * @param broadcasterId The user id that corresponds with the user access token.
     * @return {@link AdScheduleWrapper}
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_ADS_READ
     */
    @RequestLine("GET /channels/ads?broadcaster_id={broadcaster_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<AdScheduleWrapper> getAdSchedule(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId
    );

    /**
     * If available, pushes back the timestamp of the upcoming automatic mid-roll ad by 5 minutes.
     * This endpoint duplicates the snooze functionality in the creator dashboard’s Ads Manager.
     * <p>
     * If the channel is not currently live or the channel does not have an upcoming scheduled ad break,
     * this endpoint will return Error 400: Bad Request.
     * <p>
     * If the channel has no snoozes left, this endpoint will return Error 429: Too Many Requests.
     *
     * @param authToken     User access token with the channel:manage:ads scope from the broadcaster.
     * @param broadcasterId The user id that corresponds with the user access token.
     * @return {@link SnoozedAdWrapper}
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_ADS_MANAGE
     */
    @RequestLine("POST /channels/ads/schedule/snooze?broadcaster_id={broadcaster_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<SnoozedAdWrapper> snoozeNextAd(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId
    );

    /**
     * Starts a commercial on a specified channel
     *
     * @param authToken Auth Token (scope: channel:edit:commercial)
     * @param broadcasterId ID of the channel requesting a commercial
     * @param length Desired length of the commercial in seconds. Valid options are 30, 60, 90, 120, 150, 180.
     * @return CommercialList
     */
    @RequestLine("POST /channels/commercial")
    @Headers({
        "Authorization: Bearer {token}",
        "Content-Type: application/json"
    })
    @Body("%7B\"broadcaster_id\":\"{broadcaster_id}\",\"length\":{length}%7D")
    HystrixCommand<CommercialList> startCommercial(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("length") Integer length
    );

    /**
     * Gets a list of users who have editor permissions for a specific channel.
     *
     * @param authToken     Required: User access token of the broadcaster with the channel:read:editors scope.
     * @param broadcasterId Required: Broadcaster’s user ID associated with the channel.
     * @return ChannelEditorList
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_EDITORS_READ
     */
    @RequestLine("GET /channels/editors?broadcaster_id={broadcaster_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<ChannelEditorList> getChannelEditors(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId
    );

    /**
     * Gets a list of broadcasters that the specified user follows.
     * <p>
     * You can also use this endpoint to see whether a user follows a specific broadcaster.
     * <p>
     * Requires a user access token that includes the user:read:follows scope.
     *
     * @param authToken     User access token (scope: user:read:follows) that matches the specified userId.
     * @param userId        Required: A user’s ID. Returns the list of broadcasters that this user follows.
     * @param broadcasterId Optional: The ID of a channel to see whether this user follows the channel. If not specified, the response contains all broadcasters that the user follows.
     * @param limit         Optional: The maximum number of items to return per page in the response. Default: 20. Minimum: 1. Maximum: 100.
     * @param after         Optional: The cursor used to get the next page of results.
     * @return {@link OutboundFollowing}
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_USER_FOLLOWS_READ
     */
    @RequestLine("GET /channels/followed?user_id={user_id}&broadcaster_id={broadcaster_id}&first={first}&after={after}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<OutboundFollowing> getFollowedChannels(
        @Param("token") String authToken,
        @NotNull @Param("user_id") String userId,
        @Nullable @Param("broadcaster_id") String broadcasterId,
        @Nullable @Param("first") Integer limit,
        @Nullable @Param("after") String after
    );

    /**
     * Gets a list of users that follow the specified broadcaster.
     * <p>
     * You can also use this endpoint to see whether a specific user follows the broadcaster.
     * <p>
     * Requires a user access token that includes the moderator:read:followers scope.
     * The user ID in the access token must match the ID in the broadcaster_id query parameter or the ID of a moderator for the specified broadcaster.
     * If a scope is not provided, only the total follower account will be included in the response.
     *
     * @param authToken     User access token (scope: moderator:read:followers) from the broadcaster or their moderator
     * @param broadcasterId Required: The broadcaster's ID whose list of followers is being queried.
     * @param userId        Optional: The ID of a user to see whether the user follows this broadcaster. If not specified, the response contains all users that follow the broadcaster.
     * @param limit         Optional: The maximum number of items to return per page in the response. Default: 20. Minimum: 1. Maximum: 100.
     * @param after         Optional: The cursor used to get the next page of results.
     * @return {@link InboundFollowers}
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_FOLLOWERS_READ
     */
    @RequestLine("GET /channels/followers?broadcaster_id={broadcaster_id}&user_id={user_id}&first={first}&after={after}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<InboundFollowers> getChannelFollowers(
        @Param("token") String authToken,
        @NotNull @Param("broadcaster_id") String broadcasterId,
        @Nullable @Param("user_id") String userId,
        @Nullable @Param("first") Integer limit,
        @Nullable @Param("after") String after
    );

    /**
     * Gets a list of the channel’s VIPs.
     *
     * @param authToken     Broadcaster's user access token that includes the channel:read:vips scope.
     * @param broadcasterId The ID of the broadcaster whose list of VIPs you want to get.
     * @param userIds       Filters the list for specific VIPs. The maximum number of IDs that you may specify is 100.
     * @param limit         The maximum number of items to return per page in the response. Minimum: 1. Maximum: 100. Default: 20.
     * @param after         The cursor used to get the next page of results. The Pagination object in the response contains the cursor’s value.
     * @return ChannelVipList
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_VIPS_READ
     */
    @RequestLine("GET /channels/vips?broadcaster_id={broadcaster_id}&user_id={user_id}&first={first}&after={after}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<ChannelVipList> getChannelVips(
        @Param("token") String authToken,
        @NotNull @Param("broadcaster_id") String broadcasterId,
        @Nullable @Param("user_id") List<String> userIds,
        @Nullable @Param("first") Integer limit,
        @Nullable @Param("after") String after
    );

    /**
     * Adds a VIP to the broadcaster’s chat room.
     *
     * @param authToken     Broadcaster's user access token that includes the channel:manage:vips scope.
     * @param broadcasterId The ID of the broadcaster that’s granting VIP status to the user.
     * @param userId        The ID of the user to add as a VIP in the broadcaster’s chat room.
     * @return 204 No Content upon a successful call
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_VIPS_MANAGE
     */
    @RequestLine("POST /channels/vips?broadcaster_id={broadcaster_id}&user_id={user_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<Void> addChannelVip(
        @Param("token") String authToken,
        @NotNull @Param("broadcaster_id") String broadcasterId,
        @NotNull @Param("user_id") String userId
    );

    /**
     * Removes a VIP from the broadcaster’s chat room.
     *
     * @param authToken     Broadcaster's user access token that includes the channel:manage:vips scope.
     * @param broadcasterId The ID of the broadcaster that’s removing VIP status from the user.
     * @param userId        The ID of the user to remove as a VIP from the broadcaster’s chat room.
     * @return 204 No Content upon a successful call
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_VIPS_MANAGE
     */
    @RequestLine("DELETE /channels/vips?broadcaster_id={broadcaster_id}&user_id={user_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<Void> removeChannelVip(
        @Param("token") String authToken,
        @NotNull @Param("broadcaster_id") String broadcasterId,
        @NotNull @Param("user_id") String userId
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
     * <p>
     * The id, game_id, and broadcaster_id query parameters are mutually exclusive, and at least one must be specified.
     * <p>
     * For clips returned by game_id or broadcaster_id, the list is in descending order by view count.
     * For lists returned by id, the list is in the same order as the input IDs.
     *
     * @param authToken User or App access token.
     * @param broadcasterId ID of the broadcaster for whom clips are returned.
     * @param gameId        ID of the game for which clips are returned.
     * @param ids           ID of the clip being queried. Limit: 100.
     * @param after         Cursor for forward pagination: tells the server where to start fetching the next set of results, in a multi-page response.
     * @param before        Cursor for backward pagination: tells the server where to start fetching the next set of results, in a multi-page response.
     * @param limit         Maximum number of objects to return. Maximum: 100. Default: 20.
     * @param startedAt     Starting date/time for returned clips, in RFC3339 format.
     * @param endedAt       Ending date/time for returned clips, in RFC3339 format. If not specified, the time window is the start date plus one week.
     * @param isFeatured    Whether the response includes featured clips. If true, returns only clips that are featured. If false, returns only clips that aren’t featured. All clips are returned if this parameter is not present.
     * @return {@link ClipList}
     */
    @RequestLine("GET /clips?broadcaster_id={broadcaster_id}&game_id={game_id}&id={id}&after={after}&before={before}&first={first}&started_at={started_at}&ended_at={ended_at}&is_featured={is_featured}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<ClipList> getClips(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("game_id") String gameId,
        @Param("id") List<String> ids,
        @Param("after") String after,
        @Param("before") String before,
        @Param("first") Integer limit,
        @Param("started_at") Instant startedAt,
        @Param("ended_at") Instant endedAt,
        @Param("is_featured") Boolean isFeatured
    );

    /**
     * Gets one or more video clips that were captured from streams.
     *
     * @param authToken User or App access token.
     * @param broadcasterId ID of the broadcaster for whom clips are returned.
     * @param gameId        ID of the game for which clips are returned.
     * @param id            ID of the clip being queried.
     * @param after         Cursor for forward pagination: tells the server where to start fetching the next set of results, in a multi-page response.
     * @param before        Cursor for backward pagination: tells the server where to start fetching the next set of results, in a multi-page response.
     * @param limit         Maximum number of objects to return. Maximum: 100. Default: 20.
     * @param startedAt     Starting date/time for returned clips, in RFC3339 format. (Note that the seconds value is ignored.)
     * @param endedAt       Ending date/time for returned clips, in RFC3339 format. (Note that the seconds value is ignored.)
     * @return ClipList Clip List
     * @deprecated in favor of {@link #getClips(String, String, String, List, String, String, Integer, Instant, Instant, Boolean)}
     */
    @Deprecated
    default HystrixCommand<ClipList> getClips(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("game_id") String gameId,
        @Param("id") String id,
        @Param("after") String after,
        @Param("before") String before,
        @Param("first") Integer limit,
        @Param("started_at") Instant startedAt,
        @Param("ended_at") Instant endedAt
    ) {
        return getClips(authToken, broadcasterId, gameId, Collections.singletonList(id), after, before, limit, startedAt, endedAt, null);
    }

    /**
     * Gets information about Twitch content classification labels.
     *
     * @param authToken App Access Token or User Access Token.
     * @param locale    Locale for the Content Classification Labels. Default: "en-US".
     *                  Supported locales: "bg-BG", "cs-CZ", "da-DK", "da-DK", "de-DE", "el-GR", "en-GB", "en-US", "es-ES", "es-MX",
     *                  "fi-FI", "fr-FR", "hu-HU", "it-IT", "ja-JP", "ko-KR", "nl-NL", "no-NO", "pl-PL", "pt-BT", "pt-PT",
     *                  "ro-RO", "ru-RU", "sk-SK", "sv-SE", "th-TH", "tr-TR", "vi-VN", "zh-CN", "zh-TW"
     * @return ContentClassificationList
     */
    @RequestLine("GET /content_classification_labels?locale={locale}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<ContentClassificationList> getContentClassificationLabels(
        @Param("token") String authToken,
        @Param("locale") String locale
    );

    /**
     * Creates a URL where you can upload a manifest file and notify users that they have an entitlement
     *
     * @param authToken App access token
     * @param manifestId Unique identifier of the manifest file to be uploaded. Must be 1-64 characters.
     * @param type Type of entitlement being granted. Only "bulk_drops_grant" is supported.
     * @return UploadEntitlementUrlList
     */
    @RequestLine("POST /entitlements/upload?manifest_id={manifest_id}&type={type}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<UploadEntitlementUrlList> createEntitlementUploadUrl(
        @Param("token") String authToken,
        @Param("manifest_id") String manifestId,
        @Param("type") String type
    );

    /**
     * Gets game information by game ID or game name or IGBD ID.
     * <p>
     * If specifying a combination of IDs, names, or IGDB IDs,
     * the total count (across types) must not exceed 100.
     *
     * @param authToken User or App access token
     * @param id        Game ID. At most 100 id values can be specified.
     * @param name      Game name. The name must be an exact match. At most 100 name values can be specified.
     * @param igdbId    The IGDB ID of the game to get. Maximum: 100.
     * @return GameList
     */
    @RequestLine("GET /games?id={id}&name={name}&igdb_id={igdb_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<GameList> getGames(
        @Param("token") String authToken,
        @Param("id") List<String> id,
        @Param("name") List<String> name,
        @Param("igdb_id") List<String> igdbId
    );

    /**
     * Gets game information by game ID or game name.
     * <p>
     * If specifying a combination of IDs and, names,
     * the total count (across types) must not exceed 100.
     *
     * @param authToken User or App access token
     * @param id        Game ID. At most 100 id values can be specified.
     * @param name      Game name. The name must be an exact match. At most 100 name values can be specified.
     * @return GameList
     * @deprecated in favor of {@link #getGames(String, List, List, List)}; can simply pass null for the new fourth argument
     */
    @Deprecated
    default HystrixCommand<GameList> getGames(String authToken, List<String> id, List<String> name) {
        return getGames(authToken, id, name, null);
    }

    /**
     * Gets the broadcaster’s list of active goals.
     * <p>
     * Use this to get the current progress of each goal.
     * <p>
     * NOTE: Although the API currently supports only one goal, you should write your application to support one or more goals.
     *
     * @param authToken     User access token from the broadcaster with the channel:read:goals scope.
     * @param broadcasterId The ID of the broadcaster that created the goals.
     * @return CreatorGoalsList
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_GOALS_READ
     */
    @RequestLine("GET /goals?broadcaster_id={broadcaster_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<CreatorGoalsList> getCreatorGoals(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId
    );

    /**
     * Gets the information of the most recent Hype Train of the given channel ID.
     * After 5 days, if no Hype Train has been active, the endpoint will return an empty response.
     *
     * @param authToken     User Auth Token (scope: channel:read:hype_train)
     * @param broadcasterId User ID of the broadcaster (required)
     * @param limit         Maximum number of objects to return. Maximum: 100. Default: 1. (optional)
     * @param after         Cursor for forward pagination (optional)
     * @return HypeTrainEventList
     */
    @RequestLine("GET /hypetrain/events?broadcaster_id={broadcaster_id}&first={first}&after={after}&cursor={after}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<HypeTrainEventList> getHypeTrainEvents(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("first") Integer limit,
        @Param("after") String after
    );

    /**
     * Gets the information of the most recent Hype Train of the given channel ID.
     * After 5 days, if no Hype Train has been active, the endpoint will return an empty response.
     *
     * @param authToken     User Auth Token (scope: channel:read:hype_train)
     * @param broadcasterId User ID of the broadcaster (required)
     * @param limit         Maximum number of objects to return. Maximum: 100. Default: 1. (optional)
     * @param id            The id of the wanted event, if known. This field has been deprecated by twitch and has no effect. (optional)
     * @param after         Cursor for forward pagination (optional)
     * @return HypeTrainEventList
     * @see <a href="https://discuss.dev.twitch.tv/t/get-hype-train-events-api-endpoint-id-query-parameter-deprecation/37613">id parameter deprecation announcement</a>
     * @deprecated Use {@link #getHypeTrainEvents(String, String, Integer, String)} instead (no id argument)
     */
    @Deprecated
    default HystrixCommand<HypeTrainEventList> getHypeTrainEvents(
        String authToken,
        String broadcasterId,
        Integer limit,
        @SuppressWarnings("unused") String id,
        String after
    ) {
        return this.getHypeTrainEvents(authToken, broadcasterId, limit, after);
    }

    @RequestLine("GET /ingests")
    HystrixCommand<IngestServerList> getIngestServers(URI baseUrl);

    /**
     * Get Ingest Servers returns a list of endpoints for ingesting live video into Twitch.
     *
     * @return IngestServerList
     */
    default HystrixCommand<IngestServerList> getIngestServers() {
        return getIngestServers(INGESTS_BASE_URL);
    }

    /**
     * Allow or deny a message that was held for review by AutoMod.
     * <p>
     * In order to retrieve messages held for review, use ITwitchPubsub#listenForModerationEvents(OAuth2Credential, String, String) and ChatModerationEvent.
     * <p>
     * Note that the scope allows this endpoint to be used for any channel that the authenticated user is a moderator, including their own channel.
     *
     * @param authToken   User OAuth token from a moderator with the moderator:manage:automod scope.
     * @param moderatorId The moderator who is approving or rejecting the held message. Must match the user_id in the user OAuth token.
     * @param messageId   ID of the message to be allowed or denied. These message IDs are retrieved from IRC or PubSub.
     * @param action      The action to take for the message.
     * @return 204 No Content upon a successful request.
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_AUTOMOD_MANAGE
     */
    @RequestLine("POST /moderation/automod/message")
    @Headers({
        "Authorization: Bearer {token}",
        "Content-Type: application/json"
    })
    @Body("%7B\"user_id\":\"{user_id}\",\"msg_id\":\"{msg_id}\",\"action\":\"{action}\"%7D")
    HystrixCommand<Void> manageAutoModHeldMessage(
        @Param("token") String authToken,
        @Param("user_id") String moderatorId,
        @Param("msg_id") String messageId,
        @Param("action") AutoModHeldMessageAction action
    );

    /**
     * Gets the broadcaster’s AutoMod settings, which are used to automatically block inappropriate or harassing messages from appearing in the broadcaster’s chat room.
     *
     * @param authToken     Required: User access token (of the broadcaster or a moderator) with scope set to moderator:read:automod_settings
     * @param broadcasterId Required: The ID of the broadcaster whose AutoMod settings you want to get.
     * @param moderatorId   Required: The ID of a user that has permission to moderate the broadcaster’s chat room. Set this to the same value as broadcasterId if a broadcaster token is being used.
     * @return AutoModSettingsWrapper
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_AUTOMOD_SETTINGS_READ
     */
    @RequestLine("GET /moderation/automod/settings?broadcaster_id={broadcaster_id}&moderator_id={moderator_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<AutoModSettingsWrapper> getAutoModSettings(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("moderator_id") String moderatorId
    );

    /**
     * Updates the broadcaster’s AutoMod settings, which are used to automatically block inappropriate or harassing messages from appearing in the broadcaster’s chat room.
     * <p>
     * You can set either overall_level or the individual settings like aggression, but not both.
     * <p>
     * Setting overall_level applies default values to the individual settings. However, setting overall_level to 4 does not mean that it applies 4 to all the individual settings.
     * Instead, it applies a set of recommended defaults to the rest of the settings.
     * For example, if you set overall_level to 2, Twitch provides some filtering on discrimination and sexual content, but more filtering on hostility (see the first example response).
     * <p>
     * If overall_level is currently set and you update swearing to 3, overall_level will be set to null and all settings other than swearing will be set to 0.
     * The same is true if individual settings are set and you update overall_level to 3 — all the individual settings are updated to reflect the default level.
     * <p>
     * Note that if you set all the individual settings to values that match what overall_level would have set them to, Twitch changes AutoMod to use the default AutoMod level instead of using the individual settings.
     * <p>
     * Valid values for all levels are from 0 (no filtering) through 4 (most aggressive filtering).
     * These levels affect how aggressively AutoMod holds back messages for moderators to review before they appear in chat or are denied (not shown).
     *
     * @param authToken     Required: User access token (of the broadcaster or a moderator) with scope set to moderator:manage:automod_settings, associated with moderatorId.
     * @param broadcasterId Required: The ID of the broadcaster whose AutoMod settings you want to update.
     * @param moderatorId   Required: The ID of a user that has permission to moderate the broadcaster’s chat room. Set this to the same value as broadcasterId if a broadcaster token is being used.
     * @param settings      Required: The AutoMod Settings fields that should be overwritten.
     * @return AutoModSettingsWrapper
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_AUTOMOD_SETTINGS_MANAGE
     */
    @RequestLine("PUT /moderation/automod/settings?broadcaster_id={broadcaster_id}&moderator_id={moderator_id}")
    @Headers({
        "Authorization: Bearer {token}",
        "Content-Type: application/json"
    })
    HystrixCommand<AutoModSettingsWrapper> updateAutoModSettings(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("moderator_id") String moderatorId,
        AutoModSettings settings
    );

    /**
     * Returns all banned and timed-out users in a channel.
     *
     * @param authToken     Auth Token (scope: moderation:read)
     * @param broadcasterId Required: Provided broadcaster_id must match the user_id in the auth token.
     * @param userId        Optional: Filters the results for only those with a matching user_id. Maximum: 100.
     * @param after         Optional: Cursor for forward pagination.
     * @param before        Optional: Cursor for backward pagination.
     * @param limit         Maximum number of objects to return. Maximum: 100. Default: 20.
     * @return BannedUserList
     */
    @RequestLine("GET /moderation/banned?broadcaster_id={broadcaster_id}&user_id={user_id}&after={after}&before={before}&first={first}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<BannedUserList> getBannedUsers(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("user_id") List<String> userId,
        @Param("after") String after,
        @Param("before") String before,
        @Param("first") Integer limit
    );

    /**
     * Returns all banned and timed-out users in a channel.
     *
     * @param authToken     Auth Token (scope: moderation:read)
     * @param broadcasterId Required: Provided broadcaster_id must match the user_id in the auth token.
     * @param userId        Optional: Filters the results for only those with a matching user_id. Maximum: 100.
     * @param after         Optional: Cursor for forward pagination.
     * @param before        Optional: Cursor for backward pagination.
     * @return BannedUserList
     * @deprecated in favor of getBannedUsers(String, String, List, String, String, Integer) where the last param is the number of objects to retrieve.
     */
    @Deprecated
    default HystrixCommand<BannedUserList> getBannedUsers(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("user_id") List<String> userId,
        @Param("after") String after,
        @Param("before") String before
    ) {
        return this.getBannedUsers(authToken, broadcasterId, userId, after, before, 20);
    }

    /**
     * Returns all user bans and un-bans in a channel.
     *
     * @param authToken     Auth Token (scope: moderation:read)
     * @param broadcasterId Required: Provided broadcaster_id must match the user_id in the auth token.
     * @param userId        Optional: Filters the results and only returns a status object for users who are banned in this channel and have a matching user_id. Maximum: 100.
     * @param after         Optional: Cursor for forward pagination.
     * @param limit         Optional: Maximum number of objects to return. Maximum: 100. Default: 20.
     * @return BannedEventList
     * @see <a href="https://discuss.dev.twitch.tv/t/deprecation-of-twitch-api-event-endpoints-that-supported-websub-based-webhooks/35137">Deprecation announcement</a>
     * @deprecated Will be removed come March 15, 2022, in favor of EventSub and {@link #getBannedUsers(String, String, List, String, String, Integer)}
     */
    @Deprecated
    @RequestLine("GET /moderation/banned/events?broadcaster_id={broadcaster_id}&user_id={user_id}&after={after}&first={first}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<BannedEventList> getBannedEvents(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("user_id") List<String> userId,
        @Param("after") String after,
        @Param("first") Integer limit
    );

    /**
     * Bans a user from participating in a broadcaster’s chat room, or puts them in a timeout.
     * <p>
     * If the user is currently in a timeout, you can call this endpoint to change the duration of the timeout or ban them altogether.
     * If the user is currently banned, you cannot call this method to put them in a timeout instead.
     *
     * @param authToken     Required: User access token (of the broadcaster or a moderator) with scope set to moderator:manage:banned_users, associated with moderatorId.
     * @param broadcasterId Required: The ID of the broadcaster whose chat room the user is being banned from.
     * @param moderatorId   Required: The ID of a user that has permission to moderate the broadcaster’s chat room. Set this to the same value as broadcasterId if a broadcaster token is being used.
     * @param data          Required: Information on the user to ban or put in a timeout.
     * @return BanUsersList
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_BANNED_USERS_MANAGE
     */
    @RequestLine("POST /moderation/bans?broadcaster_id={broadcaster_id}&moderator_id={moderator_id}")
    @Headers({
        "Authorization: Bearer {token}",
        "Content-Type: application/json"
    })
    @Body("%7B\"data\":{data}%7D")
    HystrixCommand<BanUsersList> banUser(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("moderator_id") String moderatorId,
        @Param(value = "data", expander = ObjectToJsonExpander.class) BanUserInput data
    );

    /**
     * Removes the ban or timeout that was placed on the specified user
     *
     * @param authToken     Required: User access token (of the broadcaster or a moderator) with scope set to moderator:manage:banned_users, associated with moderatorId.
     * @param broadcasterId Required: The ID of the broadcaster whose chat room the user is banned from chatting in.
     * @param moderatorId   Required: The ID of a user that has permission to moderate the broadcaster’s chat room. Set this to the same value as broadcasterId if a broadcaster token is being used.
     * @param userId        Required: The ID of the user to remove the ban or timeout from.
     * @return 204 No Content upon a successful unban or untimeout
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_BANNED_USERS_MANAGE
     */
    @RequestLine("DELETE /moderation/bans?broadcaster_id={broadcaster_id}&moderator_id={moderator_id}&user_id={user_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<Void> unbanUser(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("moderator_id") String moderatorId,
        @Param("user_id") String userId
    );

    /**
     * Gets the broadcaster’s list of non-private, blocked words or phrases.
     * These are the terms that the broadcaster or moderator added manually, or that were denied by AutoMod.
     *
     * @param authToken     Required: User access token (of the broadcaster or a moderator) with scope set to moderator:read:blocked_terms, associated with moderatorId.
     * @param broadcasterId Required: The ID of the broadcaster whose blocked terms you’re getting.
     * @param moderatorId   Required: The ID of a user that has permission to moderate the broadcaster’s chat room. Set this to the same value as broadcasterId if a broadcaster token is being used.
     * @param after         Optional: The cursor used to get the next page of results. The Pagination object in the response contains the cursor’s value.
     * @param limit         Optional: The maximum number of blocked terms to return per page in the response. The minimum page size is 1 blocked term per page and the maximum is 100. The default is 20.
     * @return BlockedTermList
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_BLOCKED_TERMS_READ
     */
    @RequestLine("GET /moderation/blocked_terms?broadcaster_id={broadcaster_id}&moderator_id={moderator_id}&after={after}&first={first}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<BlockedTermList> getBlockedTerms(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("moderator_id") String moderatorId,
        @Param("after") String after,
        @Param("first") Integer limit
    );

    /**
     * Adds a word or phrase to the broadcaster’s list of blocked terms.
     * These are the terms that broadcasters don’t want used in their chat room.
     * <p>
     * The term must contain a minimum of 2 characters and may contain up to a maximum of 500 characters.
     * <p>
     * Terms can use a wildcard character ({@literal *}).
     * The wildcard character must appear at the beginning or end of a word, or set of characters.
     * For example, {@literal *foo or foo*}.
     *
     * @param authToken     Required: User access token (of the broadcaster or a moderator) with scope set to moderator:manage:blocked_terms, associated with moderatorId.
     * @param broadcasterId Required: The ID of the broadcaster that owns the list of blocked terms.
     * @param moderatorId   Required: The ID of a user that has permission to moderate the broadcaster’s chat room. Set this to the same value as broadcasterId if a broadcaster token is being used.
     * @param term          Required: The word or phrase to block from being used in the broadcaster’s chat room.
     * @return BlockedTermList
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_BLOCKED_TERMS_MANAGE
     */
    @RequestLine("POST /moderation/blocked_terms?broadcaster_id={broadcaster_id}&moderator_id={moderator_id}")
    @Headers({
        "Authorization: Bearer {token}",
        "Content-Type: application/json"
    })
    @Body("%7B\"text\":\"{text}\"%7D")
    HystrixCommand<BlockedTermList> addBlockedTerm(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("moderator_id") String moderatorId,
        @Param(value = "text", expander = JsonStringExpander.class) String term
    );

    /**
     * Removes the word or phrase that the broadcaster is blocking users from using in their chat room.
     *
     * @param authToken     Required: User access token (of the broadcaster or a moderator) with scope set to moderator:manage:blocked_terms, associated with moderatorId.
     * @param broadcasterId Required: The ID of the broadcaster that owns the list of blocked terms.
     * @param moderatorId   Required: The ID of a user that has permission to moderate the broadcaster’s chat room. Set this to the same value as broadcasterId if a broadcaster token is being used.
     * @param blockedTermId Required: The ID of the blocked term you want to delete.
     * @return 204 No Content upon a successful request
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_BLOCKED_TERMS_MANAGE
     */
    @RequestLine("DELETE /moderation/blocked_terms?broadcaster_id={broadcaster_id}&moderator_id={moderator_id}&id={id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<Void> removeBlockedTerm(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("moderator_id") String moderatorId,
        @Param("id") String blockedTermId
    );

    /**
     * Gets a list of channels that the specified user has moderator privileges in.
     *
     * @param authToken User access token (scope: user:read:moderated_channels)
     * @param userId    The user id associated with the user access token
     * @param limit     The maximum number of items to return per page in the response. Default: 20. Minimum: 1. Maximum: 100.
     * @param after     The cursor used to get the next page of results.
     * @return {@link ModeratedChannelList}
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_USER_MODERATED_READ
     */
    @RequestLine("GET /moderation/channels?user_id={user_id}&after={after}&first={first}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<ModeratedChannelList> getModeratedChannels(
        @Param("token") String authToken,
        @Param("user_id") String userId,
        @Param("first") Integer limit,
        @Param("after") String after
    );

    /**
     * Removes a single chat message or all chat messages from the broadcaster’s chat room.
     * <p>
     * The ID in the moderator_id query parameter must match the user ID in the access token.
     * If the broadcaster wants to remove messages themselves (instead of having the moderator do it), set this parameter to the broadcaster’s ID, too.
     * <p>
     * The id tag in the PRIVMSG contains the message’s ID (see {@code IRCMessageEvent#getMessageId}).
     * Restrictions:
     * <ul>
     *     <li>The message must have been created within the last 6 hours.</li>
     *     <li>The message must not belong to the broadcaster.</li>
     * </ul>
     * If id is not specified, the request removes all messages in the broadcaster’s chat room.
     *
     * @param authToken     User access token (scope: moderator:manage:chat_messages) of the broadcaster or a moderator.
     * @param broadcasterId The ID of the broadcaster that owns the chat room to remove messages from.
     * @param moderatorId   The ID of a user that has permission to moderate the broadcaster’s chat room. This ID must match the user ID in the OAuth token.
     * @param messageId     The ID of the message to remove. If not specified, the request removes all messages in the broadcaster’s chat room.
     * @return 204 No Content upon a successful call
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHAT_MESSAGES_MANAGE
     */
    @RequestLine("DELETE /moderation/chat?broadcaster_id={broadcaster_id}&moderator_id={moderator_id}&message_id={message_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<Void> deleteChatMessages(
        @Param("token") String authToken,
        @NotNull @Param("broadcaster_id") String broadcasterId,
        @NotNull @Param("moderator_id") String moderatorId,
        @Nullable @Param("message_id") String messageId
    );

    /**
     * Determines whether a string message meets the channel’s AutoMod requirements.
     *
     * @param authToken     Auth Token (scope: moderation:read)
     * @param broadcasterId Provided broadcaster_id must match the user_id in the auth token.
     * @param messages      the messages to be checked
     * @return AutomodEnforceStatusList
     */
    @RequestLine("POST /moderation/enforcements/status?broadcaster_id={broadcaster_id}")
    @Headers({
        "Authorization: Bearer {token}",
        "Content-Type: application/json"
    })
    HystrixCommand<AutomodEnforceStatusList> checkAutomodStatus(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        AutomodEnforceCheckList messages
    );

    /**
     * Returns all moderators in a channel.
     *
     * @param authToken     User Token for the broadcaster (scope: moderation:read or channel:manage:moderators),
     * @param broadcasterId Provided broadcaster_id must match the user_id in the auth token.
     * @param userIds       Filters the results and only returns a status object for users who are moderators in this channel and have a matching user_id.
     * @param after         Cursor for forward pagination: tells the server where to start fetching the next set of results, in a multi-page response. The cursor value specified here is from the pagination response field of a prior query.
     * @param limit         Maximum number of objects to return. Maximum: 100. Default: 20.
     * @return ModeratorList
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_MODERATION_READ
     */
    @RequestLine("GET /moderation/moderators?broadcaster_id={broadcaster_id}&user_id={user_id}&after={after}&first={first}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<ModeratorList> getModerators(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("user_id") List<String> userIds,
        @Param("after") String after,
        @Param("first") Integer limit
    );

    /**
     * Adds a moderator to the broadcaster’s chat room.
     *
     * @param authToken     Broadcaster's user access token that includes the channel:manage:moderators scope.
     * @param broadcasterId The ID of the broadcaster that owns the chat room.
     * @param userId        The ID of the user to add as a moderator in the broadcaster’s chat room.
     * @return 204 No Content upon a successful call
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_MODS_MANAGE
     */
    @RequestLine("POST /moderation/moderators?broadcaster_id={broadcaster_id}&user_id={user_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<Void> addChannelModerator(
        @Param("token") String authToken,
        @NotNull @Param("broadcaster_id") String broadcasterId,
        @NotNull @Param("user_id") String userId
    );

    /**
     * Removes a moderator from the broadcaster’s chat room.
     *
     * @param authToken     User access token (scope: channel:manage:moderators) of the broadcaster or target moderator.
     * @param broadcasterId The ID of the broadcaster that owns the chat room.
     * @param userId        The ID of the user to remove as a moderator from the broadcaster’s chat room.
     * @return 204 No Content upon a successful call
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_MODS_MANAGE
     */
    @RequestLine("DELETE /moderation/moderators?broadcaster_id={broadcaster_id}&user_id={user_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<Void> removeChannelModerator(
        @Param("token") String authToken,
        @NotNull @Param("broadcaster_id") String broadcasterId,
        @NotNull @Param("user_id") String userId
    );

    /**
     * Returns all moderators in a channel.
     *
     * @param authToken User Token for the broadcaster
     * @param broadcasterId Provided broadcaster_id must match the user_id in the auth token.
     * @param userIds Filters the results and only returns a status object for users who are moderators in this channel and have a matching user_id.
     * @param after Cursor for forward pagination: tells the server where to start fetching the next set of results, in a multi-page response. The cursor value specified here is from the pagination response field of a prior query.
     * @return ModeratorList
     * @deprecated in favor of getModerators(String, String, List, String, Integer) where the last param is the number of objects to retrieve.
     */
    @Deprecated
    default HystrixCommand<ModeratorList> getModerators(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("user_id") List<String> userIds,
        @Param("after") String after
    ) {
        return this.getModerators(authToken, broadcasterId, userIds, after, 20);
    }

    /**
     * Returns a list of moderators or users added and removed as moderators from a channel.
     *
     * @param authToken     User Token for the broadcaster
     * @param broadcasterId Provided broadcaster_id must match the user_id in the auth token.
     * @param userIds       Filters the results and only returns a status object for users who are moderators in this channel and have a matching user_id.
     * @param after         Cursor for forward pagination: tells the server where to start fetching the next set of results, in a multi-page response. The cursor value specified here is from the pagination response field of a prior query.
     * @param limit         Maximum number of objects to return. Maximum: 100. Default: 20.
     * @return ModeratorList
     * @see <a href="https://discuss.dev.twitch.tv/t/deprecation-of-twitch-api-event-endpoints-that-supported-websub-based-webhooks/35137">Deprecation announcement</a>
     * @deprecated Will be removed come March 15, 2022, in favor of EventSub and {@link #getModerators(String, String, List, String, Integer)}
     */
    @Deprecated
    @RequestLine(value = "GET /moderation/moderators/events?broadcaster_id={broadcaster_id}&user_id={user_id}&after={after}&first={first}", collectionFormat = CollectionFormat.CSV)
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<ModeratorEventList> getModeratorEvents(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("user_id") List<String> userIds,
        @Param("after") String after,
        @Param("first") Integer limit
    );

    /**
     * Returns a list of moderators or users added and removed as moderators from a channel.
     *
     * @param authToken     User Token for the broadcaster
     * @param broadcasterId Provided broadcaster_id must match the user_id in the auth token.
     * @param userIds       Filters the results and only returns a status object for users who are moderators in this channel and have a matching user_id.
     * @param after         Cursor for forward pagination: tells the server where to start fetching the next set of results, in a multi-page response. The cursor value specified here is from the pagination response field of a prior query.
     * @return ModeratorList
     * @see <a href="https://discuss.dev.twitch.tv/t/deprecation-of-twitch-api-event-endpoints-that-supported-websub-based-webhooks/35137">Deprecation announcement</a>
     * @deprecated in favor of getModeratorEvents(String, String, List, String, Integer) where the last param is the number of objects to retrieve.
     */
    @Deprecated
    default HystrixCommand<ModeratorEventList> getModeratorEvents(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("user_id") List<String> userIds,
        @Param("after") String after
    ) {
        return this.getModeratorEvents(authToken, broadcasterId, userIds, after, 20);
    }

    /**
     * Gets the broadcaster’s Shield Mode activation status.
     *
     * @param authToken     User access token that includes the moderator:read:shield_mode or moderator:manage:shield_mode scope.
     * @param broadcasterId The ID of the broadcaster whose Shield Mode activation status you want to get.
     * @param moderatorId   The ID of the broadcaster or a user that is one of the broadcaster’s moderators. This ID must match the user ID in the access token.
     * @return ShieldModeStatusWrapper
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_SHIELD_MODE_READ
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_SHIELD_MODE_MANAGE
     */
    @RequestLine("GET /moderation/shield_mode?broadcaster_id={broadcaster_id}&moderator_id={moderator_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<ShieldModeStatusWrapper> getShieldModeStatus(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("moderator_id") String moderatorId
    );

    /**
     * Activates or deactivates the broadcaster’s Shield Mode.
     *
     * @param authToken     User access token that includes the moderator:manage:shield_mode scope.
     * @param broadcasterId The ID of the broadcaster whose Shield Mode you want to activate or deactivate.
     * @param moderatorId   The ID of the broadcaster or a user that is one of the broadcaster’s moderators. This ID must match the user ID in the access token.
     * @param active        A Boolean value that determines whether to activate Shield Mode.
     * @return ShieldModeStatusWrapper
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_SHIELD_MODE_MANAGE
     */
    @RequestLine("PUT /moderation/shield_mode?broadcaster_id={broadcaster_id}&moderator_id={moderator_id}")
    @Headers({
        "Authorization: Bearer {token}",
        "Content-Type: application/json"
    })
    @Body("%7B\"is_active\":{is_active}%7D")
    HystrixCommand<ShieldModeStatusWrapper> updateShieldModeStatus(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("moderator_id") String moderatorId,
        @Param("is_active") boolean active
    );

    /**
     * Gets a list of unban requests for a broadcaster’s channel.
     *
     * @param authToken     User access token (scope: moderator:read:unban_requests or moderator:manage:unban_requests) of a channel moderator.
     * @param broadcasterId The ID of the broadcaster whose channel is receiving unban requests.
     * @param moderatorId   The ID of the broadcaster or a user that has permission to moderate the broadcaster’s unban requests. This ID must match the user ID in the user access token.
     * @param status        Filter by a status.
     * @param userId        Optional: Filter by a user ID of an unban request submitter (i.e., someone who was banned).
     * @param after         Optional: Cursor used to get next page of results.
     * @param limit         Optional: The maximum number of items to return per page in response.
     * @return UnbanRequestList
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_UNBAN_REQUESTS_READ
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_UNBAN_REQUESTS_MANAGE
     */
    @RequestLine("GET /moderation/unban_requests?broadcaster_id={broadcaster_id}&moderator_id={moderator_id}&status={status}&user_id={user_id}&after={after}&first={first}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<UnbanRequestList> getUnbanRequests(
        @Param("token") String authToken,
        @NotNull @Param("broadcaster_id") String broadcasterId,
        @NotNull @Param("moderator_id") String moderatorId,
        @NotNull @Param("status") UnbanRequestStatus status,
        @Nullable @Param("user_id") String userId,
        @Nullable @Param("after") String after,
        @Nullable @Param("first") Integer limit
    );

    /**
     * Resolve an unban request by approving or denying it.
     *
     * @param authToken      User access token (scope: moderator:manage:unban_requests) from a channel moderator.
     * @param broadcasterId  The ID of the broadcaster whose channel is approving or denying the unban request.
     * @param moderatorId    The ID of the broadcaster or a user that has permission to moderate the broadcaster’s unban requests. This ID must match the user ID in the user access token.
     * @param unbanRequestId The ID of the unban request to resolve; {@link UnbanRequest#getId()}.
     * @param status         Resolution status; should be {@link UnbanRequestStatus#APPROVED} or {@link UnbanRequestStatus#DENIED}.
     * @param resolutionText Optional: Message supplied by the unban request resolver to be shown to the requestor.
     * @return UnbanRequestList
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_UNBAN_REQUESTS_MANAGE
     */
    @RequestLine("PATCH /moderation/unban_requests?broadcaster_id={broadcaster_id}&moderator_id={moderator_id}&unban_request_id={unban_request_id}&status={status}&resolution_text={resolution_text}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<UnbanRequestList> resolveUnbanRequest(
        @Param("token") String authToken,
        @NotNull @Param("broadcaster_id") String broadcasterId,
        @NotNull @Param("moderator_id") String moderatorId,
        @NotNull @Param("unban_request_id") String unbanRequestId,
        @NotNull @Param("status") UnbanRequestStatus status,
        @Nullable @Param("resolution_text") String resolutionText
    );

    /**
     * Warns a user in the specified broadcaster’s chat room,
     * preventing them from chat interaction until the warning is acknowledged.
     * <p>
     * New warnings can be issued to a user when they already have a warning in the channel
     * (new warning will replace old warning).
     *
     * @param authToken     User access token that includes the moderator:manage:warnings scope. Query parameter moderator_id must match the user_id in the user access token.
     * @param broadcasterId The ID of the channel in which the warning will take effect.
     * @param moderatorId   The ID of the twitch user who requested the warning.
     * @param userId        The ID of the twitch user to be warned.
     * @param reason        A custom reason for the warning. Max: 500 chars.
     * @return ChatUserWarningWrapper
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_WARNINGS_MANAGE
     */
    @RequestLine("POST /moderation/warnings?broadcaster_id={broadcaster_id}&moderator_id={moderator_id}")
    @Headers({
        "Authorization: Bearer {token}",
        "Content-Type: application/json"
    })
    @Body("%7B\"data\":%7B\"user_id\":\"{user_id}\",\"reason\":\"{reason}\"%7D%7D")
    HystrixCommand<ValueWrapper<ChatUserWarning>> warnChatUser(
        @Param("token") String authToken,
        @NotNull @Param("broadcaster_id") String broadcasterId,
        @NotNull @Param("moderator_id") String moderatorId,
        @NotNull @Param("user_id") String userId,
        @NotNull @Param(value = "reason", expander = JsonStringExpander.class) String reason
    );

    /**
     * Get information about all polls or specific polls for a Twitch channel.
     * <p>
     * Poll information is available for 90 days.
     *
     * @param authToken     Required: User OAuth token from the broadcaster with the channel:read:polls scope.
     * @param broadcasterId Required: The broadcaster running polls. Provided broadcaster_id must match the user_id in the user OAuth token.
     * @param pollIds       Optional: ID of a poll. Filters results to one or more specific polls. Not providing one or more IDs will return the full list of polls for the authenticated channel. Maximum: 100.
     * @param after         Optional: Cursor for forward pagination: The cursor value specified here is from the pagination response field of a prior query.
     * @param limit         Optional: Maximum number of objects to return. Maximum: 20. Default: 20.
     * @return PollsList
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_POLLS_READ
     */
    @RequestLine("GET /polls?broadcaster_id={broadcaster_id}&id={id}&after={after}&first={first}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<PollsList> getPolls(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("id") List<String> pollIds,
        @Param("after") String after,
        @Param("first") Integer limit
    );

    /**
     * Create a poll for a specific Twitch channel.
     *
     * @param authToken User OAuth token from the broadcaster with the channel:manage:polls scope.
     * @param poll      The new poll to be created. Required: broadcaster_id, title, choices, choice.title, duration. Optional: bits_voting_enabled, bits_per_vote, channel_points_voting_enabled, channel_points_per_vote.
     * @return PollsList
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_POLLS_MANAGE
     */
    @RequestLine("POST /polls")
    @Headers({
        "Authorization: Bearer {token}",
        "Content-Type: application/json"
    })
    HystrixCommand<PollsList> createPoll(
        @Param("token") String authToken,
        Poll poll
    );

    /**
     * End a poll that is currently active.
     *
     * @param authToken User OAuth token from the broadcaster with the channel:manage:polls scope.
     * @param poll      The poll to be ended. Required: broadcaster_id, id, status (must be TERMINATED or ARCHIVED, depending on whether it should be viewed publicly).
     * @return PollsList
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_POLLS_MANAGE
     */
    @RequestLine("PATCH /polls")
    @Headers({
        "Authorization: Bearer {token}",
        "Content-Type: application/json"
    })
    HystrixCommand<PollsList> endPoll(
        @Param("token") String authToken,
        Poll poll
    );

    /**
     * Get information about all Channel Points Predictions or specific Channel Points Predictions for a Twitch channel.
     *
     * @param authToken     Required: User OAuth token from the broadcaster with the channel:read:predictions scope.
     * @param broadcasterId Required: The broadcaster running Predictions. Provided broadcaster_id must match the user_id in the user OAuth token.
     * @param predictionId  Optional: ID of a Prediction. Filters results to one or more specific Predictions. Not providing one or more IDs will return the full list of Predictions for the authenticated channel. Maximum: 100
     * @param after         Optional: The cursor value specified here is from the pagination response field of a prior query.
     * @param limit         Optional: Maximum number of objects to return. Maximum: 20. Default: 20.
     * @return PredictionsList
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_PREDICTIONS_READ
     */
    @RequestLine("GET /predictions?broadcaster_id={broadcaster_id}&id={id}&after={after}&first={first}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<PredictionsList> getPredictions(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("id") List<String> predictionId,
        @Param("after") String after,
        @Param("first") Integer limit
    );

    /**
     * Create a Channel Points Prediction for a specific Twitch channel.
     *
     * @param authToken  User OAuth token from the broadcaster with the channel:manage:predictions scope.
     * @param prediction The prediction to be created. Required: broadcaster_id, title, outcomes, outcome.title, prediction_window.
     * @return PredictionsList
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_PREDICTIONS_MANAGE
     */
    @RequestLine("POST /predictions")
    @Headers({
        "Authorization: Bearer {token}",
        "Content-Type: application/json"
    })
    HystrixCommand<PredictionsList> createPrediction(
        @Param("token") String authToken,
        Prediction prediction
    );

    /**
     * Lock, resolve, or cancel a Channel Points Prediction.
     * <p>
     * Active Predictions can be updated to be "locked," "resolved," or "canceled."
     * Locked Predictions can be updated to be "resolved" or "canceled."
     *
     * @param authToken  User OAuth token from the broadcaster with the channel:manage:predictions scope.
     * @param prediction The prediction to be ended. Required: broadcaster_id, id, status (RESOLVED or CANCELED or LOCKED). Optional: winning_outcome_id (must be present for RESOLVED status).
     * @return PredictionsList
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_PREDICTIONS_MANAGE
     */
    @RequestLine("PATCH /predictions")
    @Headers({
        "Authorization: Bearer {token}",
        "Content-Type: application/json"
    })
    HystrixCommand<PredictionsList> endPrediction(
        @Param("token") String authToken,
        Prediction prediction
    );

    /**
     * Raid another channel by sending the broadcaster’s viewers to the targeted channel.
     * <p>
     * This endpoint triggers a 90-second cooldown before the raid is executed (or the broadcaster can manually press "Raid now").
     *
     * @param authToken         Broadcaster user access token with the channel:manage:raids scope.
     * @param fromBroadcasterId The ID of the broadcaster that’s sending the raiding party.
     * @param toBroadcasterId   The ID of the broadcaster to raid.
     * @return RaidRequestList
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_RAIDS_MANAGE
     */
    @RequestLine("POST /raids?from_broadcaster_id={from_broadcaster_id}&to_broadcaster_id={to_broadcaster_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<RaidRequestList> startRaid(
        @Param("token") String authToken,
        @Param("from_broadcaster_id") String fromBroadcasterId,
        @Param("to_broadcaster_id") String toBroadcasterId
    );

    /**
     * Cancel a pending raid.
     * <p>
     * You can cancel a raid at any point up until the broadcaster clicks Raid Now in the Twitch UX or the 90 seconds countdown expires.
     *
     * @param authToken     Broadcaster user access token with the channel:manage:raids scope.
     * @param broadcasterId The ID of the broadcaster that sent the raiding party.
     * @return 204 No Content upon a successful raid cancel call
     */
    @RequestLine("DELETE /raids?broadcaster_id={broadcaster_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<Void> cancelRaid(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId
    );

    /**
     * Gets games sorted by number of current viewers on Twitch, most popular first.
     * Using user-token or app-token to increase rate limits.
     *
     * @param authToken User or App auth Token, for increased rate-limits
     * @param after Cursor for forward pagination: tells the server where to start fetching the next set of results, in a multi-page response. The cursor value specified here is from the pagination response field of a prior query.
     * @param before Cursor for backward pagination: tells the server where to start fetching the next set of results, in a multi-page response. The cursor value specified here is from the pagination response field of a prior query.
     * @param first Maximum number of objects to return. Maximum: 100. Default: 20.
     * @return GameList
     */
    @RequestLine("GET /games/top?after={after}&before={before}&first={first}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<GameTopList> getTopGames(
        @Param("token") String authToken,
        @Param("after") String after,
        @Param("before") String before,
        @Param("first") String first
    );

    /**
     * Gets all scheduled broadcasts or specific scheduled broadcasts from a channel’s stream schedule.
     *
     * @param authToken     User OAuth Token or App Access Token.
     * @param broadcasterId User ID of the broadcaster who owns the channel streaming schedule.
     * @param ids           The ID of the stream segment to return. Maximum: 100.
     * @param startTime     A timestamp in RFC3339 format to start returning stream segments from. If not specified, the current date and time is used.
     * @param utcOffset     A timezone offset for the requester specified in minutes. For example, a timezone that is +4 hours from GMT would be “240.” If not specified, “0” is used for GMT.
     * @param after         Cursor for forward pagination: tells the server where to start fetching the next set of results in a multi-page response. The cursor value specified here is from the pagination response field of a prior query.
     * @param limit         Maximum number of stream segments to return. Maximum: 25. Default: 20.
     * @return StreamScheduleResponse
     */
    @RequestLine("GET /schedule?broadcaster_id={broadcaster_id}&id={id}&start_time={start_time}&utc_offset={utc_offset}&first={first}&after={after}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<StreamScheduleResponse> getChannelStreamSchedule(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("id") Collection<String> ids,
        @Param("start_time") Instant startTime,
        @Param("utc_offset") String utcOffset,
        @Param("after") String after,
        @Param("first") Integer limit
    );

    @Deprecated
    @SuppressWarnings("DeprecatedIsStillUsed")
    @RequestLine("GET /schedule/icalendar?broadcaster_id={broadcaster_id}")
    HystrixCommand<Response> getChannelInternetCalendarResponse(
        @Param("broadcaster_id") String broadcasterId
    );

    /**
     * Gets all scheduled broadcasts from a channel’s stream schedule as an iCalendar.
     *
     * @param broadcasterId User ID of the broadcaster who owns the channel streaming schedule.
     * @return iCalendar data is returned according to RFC5545, in a multi-line String.
     */
    default String getChannelInternetCalendar(String broadcasterId) throws IOException {
        Response response = getChannelInternetCalendarResponse(broadcasterId).execute();
        try (InputStream is = response.body().asInputStream()) {
            return IOUtils.toString(is, response.charset());
        }
    }

    /**
     * Update the settings for a channel’s stream schedule.
     *
     * @param authToken       User OAuth Token of the broadcaster (scope: "channel:manage:schedule").
     * @param broadcasterId   User ID of the broadcaster who owns the channel streaming schedule. Provided broadcaster_id must match the user_id in the user OAuth token.
     * @param vacationEnabled Indicates whether Vacation Mode is enabled.
     * @param vacationStart   Start time for vacation specified in RFC3339 format. Required if is_vacation_enabled is set to true.
     * @param vacationEnd     End time for vacation specified in RFC3339 format. Required if is_vacation_enabled is set to true.
     * @param timezone        The timezone for when the vacation is being scheduled using the IANA time zone database format. Required if is_vacation_enabled is set to true.
     * @return 204 No Content upon a successful call.
     */
    @RequestLine("PATCH /schedule/settings?broadcaster_id={broadcaster_id}&is_vacation_enabled={is_vacation_enabled}&vacation_start_time={vacation_start_time}&vacation_end_time={vacation_end_time}&timezone={timezone}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<Void> updateChannelStreamSchedule(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("is_vacation_enabled") Boolean vacationEnabled,
        @Param("vacation_start_time") Instant vacationStart,
        @Param("vacation_end_time") Instant vacationEnd,
        @Param("timezone") String timezone
    );

    /**
     * Create a single scheduled broadcast or a recurring scheduled broadcast for a channel’s stream schedule.
     *
     * @param authToken     User OAuth Token of the broadcaster (scope: "channel:manage:schedule").
     * @param broadcasterId User ID of the broadcaster who owns the channel streaming schedule. Provided broadcaster_id must match the user_id in the user OAuth token.
     * @param segment       Properties of the scheduled broadcast (required: start_time, timezone, duration).
     * @return StreamScheduleResponse
     */
    @RequestLine("POST /schedule/segment?broadcaster_id={broadcaster_id}")
    @Headers({
        "Authorization: Bearer {token}",
        "Content-Type: application/json"
    })
    HystrixCommand<StreamScheduleResponse> createStreamScheduleSegment(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        ScheduleSegmentInput segment
    );

    /**
     * Update a single scheduled broadcast or a recurring scheduled broadcast for a channel’s stream schedule.
     *
     * @param authToken     User OAuth Token of the broadcaster (scope: "channel:manage:schedule").
     * @param broadcasterId User ID of the broadcaster who owns the channel streaming schedule. Provided broadcaster_id must match the user_id in the user OAuth token.
     * @param id            The ID of the streaming segment to update.
     * @param segment       Updated properties of the scheduled broadcast.
     * @return StreamScheduleResponse
     */
    @RequestLine("PATCH /schedule/segment?broadcaster_id={broadcaster_id}&id={id}")
    @Headers({
        "Authorization: Bearer {token}",
        "Content-Type: application/json"
    })
    HystrixCommand<StreamScheduleResponse> updateStreamScheduleSegment(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("id") String id,
        ScheduleSegmentInput segment
    );

    /**
     * Delete a single scheduled broadcast or a recurring scheduled broadcast for a channel’s stream schedule.
     *
     * @param authToken     User OAuth Token of the broadcaster (scope: "channel:manage:schedule").
     * @param broadcasterId User ID of the broadcaster who owns the channel streaming schedule. Provided broadcaster_id must match the user_id in the user OAuth token.
     * @param id            The ID of the streaming segment to delete.
     * @return 204 No Content upon a successful call.
     */
    @RequestLine("DELETE /schedule/segment?broadcaster_id={broadcaster_id}&id={id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<Void> deleteStreamScheduleSegment(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("id") String id
    );

    /**
     * Gets information about active streams.
     * <p>
     * Streams are returned sorted by number of current viewers, in descending order.
     * Across multiple pages of results, there may be duplicate or missing streams, as viewers join and leave streams.
     *
     * @param authToken  User or App Access Token
     * @param after      Cursor for forward pagination: tells the server where to start fetching the next set of results, in a multi-page response. The cursor value specified here is from the pagination response field of a prior query.
     * @param before     Cursor for backward pagination: tells the server where to start fetching the next set of results, in a multi-page response. The cursor value specified here is from the pagination response field of a prior query.
     * @param limit      Maximum number of objects to return. Maximum: 100. Default: 20.
     * @param gameIds    Returns streams broadcasting a specified game ID. You can specify up to 100 IDs.
     * @param language   Stream language. You can specify up to 100 languages. A language value must be either the ISO 639-1 two-letter code for a supported stream language or "other".
     * @param userIds    Returns streams broadcast by one or more specified user IDs. You can specify up to 100 IDs.
     * @param userLogins Returns streams broadcast by one or more specified user login names. You can specify up to 100 names.
     * @return StreamList
     */
    @RequestLine("GET /streams?after={after}&before={before}&first={first}&game_id={game_id}&language={language}&user_id={user_id}&user_login={user_login}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<StreamList> getStreams(
        @Param("token") String authToken,
        @Param("after") String after,
        @Param("before") String before,
        @Param("first") Integer limit,
        @Param("game_id") List<String> gameIds,
        @Param("language") List<String> language,
        @Param("user_id") List<String> userIds,
        @Param("user_login") List<String> userLogins
    );

    /**
     * Gets information about active streams.
     *
     * @param authToken   User or App Access Token
     * @param after       Cursor for forward pagination: tells the server where to start fetching the next set of results, in a multi-page response. The cursor value specified here is from the pagination response field of a prior query.
     * @param before      Cursor for backward pagination: tells the server where to start fetching the next set of results, in a multi-page response. The cursor value specified here is from the pagination response field of a prior query.
     * @param limit       Maximum number of objects to return. Maximum: 100. Default: 20.
     * @param communityId Returns streams in a specified community ID. You can specify up to 100 IDs. No longer supported by twitch.
     * @param gameIds     Returns streams broadcasting a specified game ID. You can specify up to 100 IDs.
     * @param language    Stream language. You can specify up to 100 languages.
     * @param userIds     Returns streams broadcast by one or more specified user IDs. You can specify up to 100 IDs.
     * @param userLogins  Returns streams broadcast by one or more specified user login names. You can specify up to 100 names.
     * @return StreamList
     * @deprecated in favor of getStreams(String, String, String, Integer, List, List, List, List); simply remove the argument for communityId to migrate
     */
    @Deprecated
    default HystrixCommand<StreamList> getStreams(
        @Param("token") String authToken,
        @Param("after") String after,
        @Param("before") String before,
        @Param("first") Integer limit,
        @Param("community_id") List<UUID> communityId, // Now unsupported by twitch
        @Param("game_id") List<String> gameIds,
        @Param("language") String language,
        @Param("user_id") List<String> userIds,
        @Param("user_login") List<String> userLogins
    ) {
        return getStreams(authToken, after, before, limit, gameIds, language != null ? Collections.singletonList(language) : null, userIds, userLogins);
    }

    /**
     * Gets information about active streams belonging to channels that the authenticated user follows.
     * <p>
     * Streams are returned sorted by number of current viewers, in descending order.
     * Across multiple pages of results, there may be duplicate or missing streams, as viewers join and leave streams.
     *
     * @param authToken Required: OAuth user token with the user:read:follows scope.
     * @param userId    Required: Results will only include active streams from the channels that this Twitch user follows. This must match the User ID in the bearer token.
     * @param after     Optional: Cursor for forward pagination.
     * @param limit     Optional: Maximum number of objects to return. Maximum: 100. Default: 100.
     * @return StreamList
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_USER_FOLLOWS_READ
     */
    @RequestLine("GET /streams/followed?after={after}&first={first}&user_id={user_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<StreamList> getFollowedStreams(
        @Param("token") String authToken,
        @Param("user_id") String userId,
        @Param("after") String after,
        @Param("first") Integer limit
    );

    /**
     * Gets the channel stream key for a user
     *
     * @param authToken Auth Token (scope: channel:read:stream_key)
     * @param broadcasterId User ID of the broadcaster
     * @return StreamKeyList
     */
    @RequestLine("GET /streams/key?broadcaster_id={broadcaster_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<StreamKeyList> getStreamKey(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId
    );

    /**
     * Gets available Twitch stream tags.
     *
     * @param authToken User Token or App auth Token, for increased rate-limits
     * @param after     Cursor for forward pagination: tells the server where to start fetching the next set of results, in a multi-page response. The cursor value specified here is from the pagination response field of a prior query.
     * @param limit     Maximum number of objects to return. Maximum: 100. Default: 20.
     * @param tagIds    Returns tags by one or more specified tag IDs. You can specify up to 100 IDs. If you search by tagIds, no pagination is used.
     * @return StreamTagList
     * @see <a href="https://discuss.dev.twitch.tv/t/adding-customizable-tags-to-the-twitch-api/42921">Deprecation Information</a>
     * @deprecated Twitch will decommission this endpoint on 2023-07-13 due to the latest custom tags system.
     */
    @Deprecated
    @RequestLine("GET /tags/streams?after={after}&first={first}&tag_id={tag_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<StreamTagList> getAllStreamTags(
        @Param("token") String authToken,
        @Param("after") String after,
        @Param("first") Integer limit,
        @Param("tag_id") List<UUID> tagIds
    );

    /**
     * Gets stream tags which are active on the specified stream.
     *
     * @param authToken     User Token or App auth Token, for increased rate-limits
     * @param broadcasterId ID of the stream to fetch current tags from
     * @return StreamTagList
     * @see <a href="https://discuss.dev.twitch.tv/t/adding-customizable-tags-to-the-twitch-api/42921">Deprecation Information</a>
     * @deprecated Twitch will decommission this endpoint on 2023-07-13 in favor of {@link #getChannelInformation(String, List)} due to the latest custom tags system.
     */
    @Deprecated
    @RequestLine("GET /streams/tags?broadcaster_id={broadcaster_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<StreamTagList> getStreamTags(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId
    );

    /**
     * Replaces the active stream tags on the specified stream with the specified tags (or clears all tags, if no new tags are specified).
     * Requires scope: channel:manage:broadcast or user:edit:broadcast
     *
     * @param authToken     Auth Token
     * @param broadcasterId ID of the stream to replace tags for
     * @param tagIds        Tag ids to replace the current stream tags with. Maximum: 100. If empty, all tags are cleared from the stream. Tags currently expire 72 hours after they are applied, unless the stream is live within that time period.
     * @return Object       nothing
     * @see <a href="https://discuss.dev.twitch.tv/t/adding-customizable-tags-to-the-twitch-api/42921">Deprecation Information</a>
     * @deprecated Twitch has decommissioned this endpoint in favor of {@link #updateChannelInformation(String, String, ChannelInformation)} due to the latest custom tags system.
     */
    @Deprecated
    @RequestLine("PUT /streams/tags?broadcaster_id={broadcaster_id}")
    @Headers({
        "Authorization: Bearer {token}",
        "Content-Type: application/json"
    })
    @Body("%7B\"tag_ids\": [{tag_ids}]%7D")
    HystrixCommand<Void> replaceStreamTags(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param(value = "tag_ids", expander = ObjectToJsonExpander.class) List<UUID> tagIds
    );

    /**
     * Creates a marker at the current time during a live stream. A marker is a temporal indicator that appears on the Twitch web UI for highlight creation and can also be retrieved with
     * {@link #getStreamMarkers(java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.String, java.lang.String) getStreamMarkers}. Markers are meant to remind streamers and their video editors of important moments during a stream.
     * Markers can be created only if the broadcast identified by the specified {@code userId} is live and has enabled VOD (past broadcast) storage. Marker creation will fail if the broadcaster is airing a premiere or a rerun.
     * Requires scope: channel:manage:broadcast or user:edit:broadcast
     *
     * @param authToken     Auth Token
     * @param highlight     User id and optional description for the marker
     * @return StreamMarker
     */
    @RequestLine("POST /streams/markers")
    @Headers({
        "Authorization: Bearer {token}",
        "Content-Type: application/json"
    })
    HystrixCommand<StreamMarker> createStreamMarker(
        @Param("token") String authToken,
        Highlight highlight
    );

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
     * Get all subscribers of a channel
     *
     * @param authToken User Auth Token
     * @param broadcasterId User ID of the broadcaster. Must match the User ID in the Bearer token.
     * @param after     Cursor for forward pagination: tells the server where to start fetching the next set of results, in a multi-page response. The cursor value specified here is from the pagination response field of a prior query.
     * @param before    Cursor for backward pagination: tells the server where to start fetching the next set of results, in a multi-page response. The cursor value specified here is from the pagination response field of a prior query.
     * @param limit     Maximum number of objects to return. Maximum: 100. Default: 20.
     * @return SubscriptionList
     */
    @RequestLine("GET /subscriptions?broadcaster_id={broadcaster_id}&after={after}&before={before}&first={first}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<SubscriptionList> getSubscriptions(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("after") String after,
        @Param("before") String before,
        @Param("first") Integer limit
    );

    /**
     * Check subscription status of provided user IDs (one or more) for a given channel
     *
     * @param authToken User Auth Token
     * @param broadcasterId User ID of the broadcaster. Must match the User ID in the Bearer token.
     * @param userIds Unique identifier of account to get subscription status of. Accepts up to 100 values.
     * @return SubscriptionList
     */
    @RequestLine("GET /subscriptions?broadcaster_id={broadcaster_id}&user_id={user_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<SubscriptionList> getSubscriptionsByUser(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("user_id") List<String> userIds
    );

    /**
     * Returns all subscription events for the past five days.
     *
     * @param authToken     Required: User Auth Token (scope: channel:read:subscriptions).
     * @param broadcasterId Required: User ID of the broadcaster. Must match the User ID in the Bearer token.
     * @param id            Optional: Retrieve a single event by the event ID.
     * @param userId        Optional: ID of the subscribed user.
     * @param after         Optional: Cursor for forward pagination; where to start fetching the next set of results in a multi-page response. This applies only to queries without user_id.
     * @param limit         Optional: Limit the number of items in the response payload. Maximum: 100.
     * @return SubscriptionEventList
     * @see <a href="https://discuss.dev.twitch.tv/t/deprecation-of-twitch-api-event-endpoints-that-supported-websub-based-webhooks/35137">Deprecation announcement</a>
     * @deprecated Will be removed come March 15, 2022, in favor of EventSub and {@link #getSubscriptions(String, String, String, String, Integer)}
     */
    @Deprecated
    @RequestLine("GET /subscriptions/events?broadcaster_id={broadcaster_id}&id={id}&user_id={user_id}&after={after}&first={first}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<SubscriptionEventList> getSubscriptionEvents(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("id") String id,
        @Param("user_id") String userId,
        @Param("after") String after,
        @Param("first") Integer limit
    );

    /**
     * Checks if a specific user (user_id) is subscribed to a specific channel (broadcaster_id).
     *
     * @param authToken     Token with the user:read:subscriptions scope. App access works if the user has authorized your application.
     * @param broadcasterId User ID of an Affiliate or Partner broadcaster.
     * @param userId        User ID of a Twitch viewer.
     * @return SubscriptionList on success, error 404 if not subscribed
     */
    @RequestLine("GET /subscriptions/user?broadcaster_id={broadcaster_id}&user_id={user_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<SubscriptionList> checkUserSubscription(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId,
        @Param("user_id") String userId
    );

    /**
     * Gets information for a specific Twitch Team.
     * <p>
     * One of the two optional query parameters must be specified to return Team information.
     *
     * @param authToken User OAuth Token or App Access Token.
     * @param name      Team name.
     * @param id        Team ID.
     * @return TeamList
     */
    @RequestLine("GET /teams?name={name}&id={id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<TeamList> getTeams(
        @Param("token") String authToken,
        @Param("name") String name,
        @Param("id") String id
    );

    /**
     * Retrieves a list of Twitch Teams of which the specified channel/broadcaster is a member.
     *
     * @param authToken     User OAuth Token or App Access Token
     * @param broadcasterId User ID for a Twitch user.
     * @return TeamMembershipList
     */
    @RequestLine("GET /teams/channel?broadcaster_id={broadcaster_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<TeamMembershipList> getChannelTeams(
        @Param("token") String authToken,
        @Param("broadcaster_id") String broadcasterId
    );

    /**
     * Get Users
     * <p>
     * Gets information about one or more specified Twitch users. Users are identified by optional user IDs and/or login name. If neither a user ID nor a login name is specified, the user is looked up by Bearer token.
     *
     * @param authToken Auth Token (optionally with the user:read:email scope)
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
     * Gets a specified user’s block list.
     * <p>
     * The list is sorted by when the block occurred in descending order (i.e. most recent block first).
     *
     * @param authToken Required: User Access Token with the user:read:blocked_users scope.
     * @param userId    Required: User ID for a Twitch user.
     * @param after     Optional: The cursor value specified here is from the pagination response field of a prior query.
     * @param limit     Optional: Maximum number of objects to return. Maximum: 100. Default: 20.
     * @return BlockedUserList
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_USER_BLOCKS_READ
     */
    @RequestLine("GET /users/blocks?broadcaster_id={broadcaster_id}&after={after}&first={first}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<BlockedUserList> getUserBlockList(
        @Param("token") String authToken,
        @Param("broadcaster_id") String userId,
        @Param("after") String after,
        @Param("first") Integer limit
    );

    /**
     * Blocks the specified user on behalf of the authenticated user.
     *
     * @param authToken     Required: User Access Token with the user:manage:blocked_users scope.
     * @param targetUserId  Required: User ID of the user to be blocked.
     * @param sourceContext Optional: Source context for blocking the user. Valid values: "chat", "whisper".
     * @param reason        Optional: Reason for blocking the user. Valid values: "spam", "harassment", or "other".
     * @return 204 No Content upon a successful call.
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_USER_BLOCKS_MANAGE
     */
    @RequestLine("PUT /users/blocks?target_user_id={target_user_id}&source_context={source_context}&reason={reason}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<Void> blockUser(
        @Param("token") String authToken,
        @Param("target_user_id") String targetUserId,
        @Param("source_context") String sourceContext,
        @Param("reason") String reason
    );

    /**
     * Unblocks the specified user on behalf of the authenticated user.
     *
     * @param authToken    Required: User Access Token with the user:manage:blocked_users scope.
     * @param targetUserId Required: User ID of the user to be unblocked.
     * @return 204 No Content upon a successful call.
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_USER_BLOCKS_MANAGE
     */
    @RequestLine("DELETE /users/blocks?target_user_id={target_user_id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<Void> unblockUser(
        @Param("token") String authToken,
        @Param("target_user_id") String targetUserId
    );

    /**
     * Get Followers
     * <p>
     * Gets information on follow relationships between two Twitch users.
     * Information returned is sorted in order, most recent follow first.
     * <p>
     * WARNING: This method will soon be deprecated as Twitch is imposing new auth requirements.
     * When to_id is specified, a (scoped) broadcaster or moderator oauth token will be required.
     * When from_id is specified, a (scoped) user access token associated with from_id will be required.
     *
     * @param authToken User or App access Token
     * @param fromId    User ID. The request returns information about users who are being followed by the from_id user.
     * @param toId      User ID. The request returns information about users who are following the to_id user.
     * @param after     Cursor for forward pagination: tells the server where to start fetching the next set of results, in a multi-page response.
     * @param limit     Maximum number of objects to return. Maximum: 100. Default: 20.
     * @return FollowList
     * @see <a href="https://discuss.dev.twitch.tv/t/follows-endpoints-and-eventsub-subscription-type-are-now-available-in-open-beta/43322">Deprecation Announcement</a>
     * @deprecated Without prior notice, Twitch has restricted this endpoint to client_id's that were using it before 2023-02-18.
     * Further, Twitch will shutdown this endpoint on <a href="https://discuss.dev.twitch.tv/t/legacy-follows-api-and-eventsub-shutdown-timeline-updated">2023-09-12</a>
     * in favor of {@link #getChannelFollowers(String, String, String, Integer, String)} and {@link #getFollowedChannels(String, String, String, Integer, String)}.
     */
    @Deprecated
    @RequestLine("GET /users/follows?from_id={from_id}&to_id={to_id}&after={after}&first={first}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<FollowList> getFollowers(
        @Param("token") String authToken,
        @Param("from_id") String fromId,
        @Param("to_id") String toId,
        @Param("after") String after,
        @Param("first") Integer limit
    );

    /**
     * Adds a specified user to the followers of a specified channel
     *
     * @param authToken Auth Token (scope: user:edit:follows)
     * @param fromId User ID of the follower
     * @param toId ID of the channel to be followed by the user
     * @param allowNotifications Whether the user gets email or push notifications (depending on the user’s notification settings) when the channel goes live. Default value is false
     * @return 204 No Content upon a successfully created follow
     * @deprecated <a href="https://discuss.dev.twitch.tv/t/deprecation-of-create-and-delete-follows-api-endpoints/32351">Decommissioned by Twitch.</a>
     */
    @RequestLine("POST /users/follows")
    @Headers({
        "Authorization: Bearer {token}",
        "Content-Type: application/json"
    })
    @Body("%7B\"from_id\":\"{from_id}\",\"to_id\":\"{to_id}\",\"allow_notifications\":{allow_notifications}%7D")
    @Deprecated
    HystrixCommand<Void> createFollow(
        @Param("token") String authToken,
        @Param("from_id") String fromId,
        @Param("to_id") String toId,
        @Param("allow_notifications") boolean allowNotifications
    );

    /**
     * Deletes a specified user from the followers of a specified channel
     *
     * @param authToken Auth Token (scope: user:edit:follows)
     * @param fromId User ID of the follower
     * @param toId Channel to be unfollowed by the user
     * @return 204 No Content upon a successful deletion from the list of channel followers
     * @deprecated <a href="https://discuss.dev.twitch.tv/t/deprecation-of-create-and-delete-follows-api-endpoints/32351">Decommissioned by Twitch.</a>
     */
    @RequestLine("DELETE /users/follows?from_id={from_id}&to_id={to_id}")
    @Headers("Authorization: Bearer {token}")
    @Deprecated
    HystrixCommand<Void> deleteFollow(
        @Param("token") String authToken,
        @Param("from_id") String fromId,
        @Param("to_id") String toId
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
     * <p>
     * Note: inactive extensions are only returned if the token has the user:edit:broadcast scope - https://github.com/twitchdev/issues/issues/477
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
     * Updates the activation state, extension ID, and/or version number of installed extensions for a specified user, identified by a Bearer token.
     * If you try to activate a given extension under multiple extension types, the last write wins (and there is no guarantee of write order).
     *
     * @param authToken Auth Token (scope: user:edit:broadcast)
     * @param extensions {@link ExtensionActiveList}
     * @return ExtensionActiveList
     */
    @RequestLine("PUT /users/extensions")
    @Headers({
        "Authorization: Bearer {token}",
        "Content-Type: application/json"
    })
    HystrixCommand<ExtensionActiveList> updateUserExtensions(
        @Param("token") String authToken,
        ExtensionActiveList extensions
    );

    /**
     * Get Videos
     * <p>
     * Gets video information by video ID (one or more), user ID (one only), or game ID (one only).
     * The response has a JSON payload with a data field containing an array of video elements.
     * For lookup by user or game, pagination is available, along with several filters that can be specified as query string parameters.
     * <p>
     * The id, user_id, and game_id parameters are mutually exclusive (but exactly one must be specified).
     * <p>
     * You may apply several filters to get a subset of the videos.
     * The filters are applied as an AND operation to each video.
     * The filters apply only if you get videos by user ID or game ID.
     *
     * @param authToken User or App Access Token
     * @param id        Required: A list of IDs that identify the videos you want to get. Limit: 100. If this is specified, you cannot use any of the optional query string parameters below.
     * @param userId    Required: The ID of the user whose list of videos you want to get.
     * @param gameId    Required: A category or game ID. The response contains a maximum of 500 videos that show this content.
     * @param language  Optional: Language of the video being queried (ISO 639-1 two-letter code, or "other" if the language is not supported by Twitch).
     * @param period    Optional: Period during which the video was created. Valid values: "all", "day", "week", "month". Default: "all".
     * @param sort      Optional: Sort order of the videos. Valid values: "time", "trending", "views". Default: "time".
     * @param type      Optional: Type of video. Valid values: "all", "upload", "archive", "highlight". Default: "all".
     * @param limit     Optional: Number of values to be returned when getting videos by user or game ID. Limit: 100. Default: 20.
     * @param after     Optional: The cursor used to get the next page of results.
     * @param before    Optional: The cursor used to get the previous page of results.
     * @return {@link VideoList}
     */
    @RequestLine("GET /videos?id={id}&user_id={user_id}&game_id={game_id}&language={language}&period={period}&sort={sort}&type={type}&after={after}&before={before}&first={first}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<VideoList> getVideos(
        @Param("token") String authToken,
        @Param("id") List<String> id,
        @Param("user_id") String userId,
        @Param("game_id") String gameId,
        @Param("language") String language,
        @Param("period") Video.SearchPeriod period,
        @Param("sort") Video.SearchOrder sort,
        @Param("type") Video.Type type,
        @Param("first") Integer limit,
        @Param("after") String after,
        @Param("before") String before
    );

    /**
     * Get Videos
     * <p>
     * Gets video information by video ID (one or more), user ID (one only), or game ID (one only).
     * The response has a JSON payload with a data field containing an array of video elements. For lookup by user or game, pagination is available, along with several filters that can be specified as query string parameters.
     *
     * @param authToken User or App Access Token
     * @param id        Required: ID of the video being queried. Limit: 100. If this is specified, you cannot use any of the optional query string parameters below.
     * @param userId    Required: ID of the user who owns the video. Limit 1.
     * @param gameId    Required: ID of the game the video is of. Limit 1.
     * @param language  Optional: Language of the video being queried. Limit: 1.
     * @param period    Optional: Period during which the video was created. Valid values: "all", "day", "week", "month". Default: "all".
     * @param sort      Optional: Sort order of the videos. Valid values: "time", "trending", "views". Default: "time".
     * @param type      Optional: Type of video. Valid values: "all", "upload", "archive", "highlight". Default: "all".
     * @param after     Optional: Cursor for forward pagination: tells the server where to start fetching the next set of results, in a multi-page response.
     * @param before    Optional: Cursor for backward pagination: tells the server where to start fetching the next set of results, in a multi-page response.
     * @param limit     Optional: Number of values to be returned when getting videos by user or game ID. Limit: 100. Default: 20.
     * @return VideoList
     * @deprecated in favor of {@link #getVideos(String, List, String, String, String, Video.SearchPeriod, Video.SearchOrder, Video.Type, Integer, String, String)}, which supports multiple video IDs
     */
    @Deprecated
    default HystrixCommand<VideoList> getVideos(
        @Param("token") String authToken,
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
    ) {
        Video.SearchPeriod time = Video.SearchPeriod.MAPPINGS.get(period);
        Video.SearchOrder order = Video.SearchOrder.MAPPINGS.get(sort);
        Video.Type vidType = Video.Type.MAPPINGS.get(type);
        return getVideos(authToken, Collections.singletonList(id), userId, gameId, language, time, order, vidType, limit, after, before);
    }

    /**
     * Deletes one or more videos. Videos are past broadcasts, Highlights, or uploads.
     * <p>
     * Invalid Video IDs will be ignored (i.e. IDs provided that do not have a video associated with it).
     * If the OAuth user token does not have permission to delete even one of the valid Video IDs, no videos will be deleted and the response will return a 401.
     *
     * @param authToken Required: User OAuth token with the channel:manage:videos scope.
     * @param ids       Required: ID of the video(s) to be deleted. Limit: 5.
     * @return DeletedVideoList
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_VIDEOS_MANAGE
     */
    @RequestLine("DELETE /videos?id={id}")
    @Headers("Authorization: Bearer {token}")
    HystrixCommand<DeletedVideoList> deleteVideos(
        @Param("token") String authToken,
        @Param("id") List<String> ids
    );

    /**
     * Get Webhook Subscriptions
     * <p>
     * Gets the Webhook subscriptions of a user identified by a Bearer token, in order of expiration.
     *
     * The response has a JSON payload with a data field containing an array of subscription elements and a pagination field containing information required to query for more subscriptions.
     *
     * @param authToken User or App auth Token, for increased rate-limits
     * @param after    Cursor for forward pagination: tells the server where to start fetching the next set of results, in a multi-page response.
     * @param limit    Number of values to be returned when getting videos by user or game ID. Limit: 100. Default: 20.
     * @return WebhookSubscriptionList
     * @deprecated <a href="https://discuss.dev.twitch.tv/t/deprecation-of-websub-based-webhooks/32152">Will be decommissioned after 2021-09-16 in favor of EventSub</a>
     */
    @RequestLine("GET /webhooks/subscriptions?after={after}&first={first}")
    @Headers("Authorization: Bearer {token}")
    @Deprecated
    HystrixCommand<WebhookSubscriptionList> getWebhookSubscriptions(
        @Param("token") String authToken,
        @Param("after") String after,
        @Param("first") Integer limit
    );

    /**
     * Subscribe to or unsubscribe from events for a specified topic.
     *
     * <p>
     * Web hook response payloads mimic their respective New Twitch API endpoint responses (with minor omissions for unnecessary fields).
     * That is, a call to a web hook returns in its payload the same data as a call to the corresponding endpoint in the new Twitch API.
     * </p>
     *
     * <p>
     * When you submit a request to subscribe to an event (with the Subscribe To/Unsubscribe From Events endpoint), your request is asynchronously validated to confirm you are allowed to create the subscription.<br>
     * Depending on the results of this validation, Twitch responds by sending you one of two GET requests:
     * <p> * Subscription verify</p>
     * <p> * Subscription denied</p>
     *
     * @param request WebhookRequest to be converted to the Json body of the API call.
     * @param authToken   Auth Token
     *
     * @see <a href="https://dev.twitch.tv/docs/api/webhooks-guid/">Twitch Webhooks Guide</a>
     * @return The response from the server
     * @deprecated <a href="https://discuss.dev.twitch.tv/t/deprecation-of-websub-based-webhooks/32152">Will be decommissioned after 2021-09-16 in favor of EventSub</a>
     */
    @RequestLine("POST /webhooks/hub")
    @Headers({"Authorization: Bearer {token}", "Content-Type: application/json"})
    @Deprecated
    HystrixCommand<Response> requestWebhookSubscription(
        WebhookRequest request, // POJO as first arg is assumed by feign to be body if no @Body annotation
        @Param("token") String authToken
    );

    /**
     * Sends a whisper message to the specified user.
     * <p>
     * Note: The user sending the whisper must have a verified phone number.
     * <p>
     * Note: The API may silently drop whispers that it suspects of violating Twitch policies.
     * (The API does not indicate that it dropped the whisper; it returns a 204 status code as if it succeeded).
     * <p>
     * Rate Limits: You may whisper to a maximum of 40 unique recipients per day.
     * Within the per day limit, you may whisper a maximum of 3 whispers per second and a maximum of 100 whispers per minute.
     * <p>
     * The ID in the from_user_id query parameter must match the user ID in the access token.
     * <p>
     * The maximum message lengths are:
     * <ul>
     *     <li>500 characters if the user you're sending the message to hasn't whispered you before.</li>
     *     <li>10,000 characters if the user you're sending the message to has whispered you before.</li>
     * </ul>
     * <p>
     * Error 400 (Bad Request) can occur if the user that you're sending the whisper to doesn't allow whisper messages from strangers,
     * and has not followed the sender's twitch account.
     *
     * @param authToken  User access token for the whisper sender that includes the user:manage:whispers scope
     * @param fromUserId The ID of the user sending the whisper. This user must have a verified phone number.
     * @param toUserId   The ID of the user to receive the whisper.
     * @param message    The whisper message to send. The message must not be empty. Messages that exceed the maximum length are truncated.
     * @return 204 No Content upon a successful call, even if the message was silently dropped
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_USER_WHISPERS_MANAGE
     */
    @RequestLine("POST /whispers?from_user_id={from_user_id}&to_user_id={to_user_id}")
    @Headers({
        "Authorization: Bearer {token}",
        "Content-Type: application/json"
    })
    @Body("%7B\"message\":\"{message}\"%7D")
    HystrixCommand<Void> sendWhisper(
        @Param("token") String authToken,
        @NotNull @Param("from_user_id") String fromUserId,
        @NotNull @Param("to_user_id") String toUserId,
        @NotNull @Param(value = "message", expander = JsonStringExpander.class) String message
    );

}
