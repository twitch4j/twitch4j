package com.github.twitch4j.pubsub;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.common.util.CryptoUtils;
import com.github.twitch4j.pubsub.domain.PubSubRequest;
import com.github.twitch4j.pubsub.enums.PubSubType;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public interface ITwitchPubSub extends AutoCloseable {

    EventManager getEventManager();

    /**
     * Send WS Message to subscribe to a topic
     *
     * @param request Topic
     * @return PubSubSubscription to be listened to
     */
    PubSubSubscription listenOnTopic(PubSubRequest request);

    /**
     * Unsubscribe from a topic.
     * Usage example:
     * <pre>
     *      PubSubSubscription subscription = twitchPubSub.listenForCheerEvents(...);
     *      // ...
     *      twitchPubSub.unsubscribeFromTopic(subscription);
     * </pre>
     *
     * @param subscription PubSubSubscription to be unlistened from
     * @return whether the subscription was previously subscribed to
     */
    boolean unsubscribeFromTopic(PubSubSubscription subscription);

    @Override
    void close();

    default PubSubSubscription listenOnTopic(PubSubType type, OAuth2Credential credential, Collection<String> topics) {
        PubSubRequest request = new PubSubRequest();
        request.setType(type);
        request.setNonce(CryptoUtils.generateNonce(30));
        request.getData().put("auth_token", credential != null ? credential.getAccessToken() : "");
        request.getData().put("topics", topics);

        return listenOnTopic(request);
    }

    default PubSubSubscription listenOnTopic(PubSubType type, OAuth2Credential credential, String topic) {
        return listenOnTopic(type, credential, Collections.singletonList(topic));
    }

    default PubSubSubscription listenOnTopic(PubSubType type, OAuth2Credential credential, String... topics) {
        return listenOnTopic(type, credential, Arrays.asList(topics));
    }

    /**
     * Event Listener: AutoMod flags a message as potentially inappropriate, and when a moderator takes action on a message.
     *
     * @param credential Credential (for userId, scope: channel:moderate)
     * @param userId     The moderator's user id associated with the credential
     * @param channelId  The user id associated with the target channel
     * @return PubSubSubscription
     */
    default PubSubSubscription listenForAutomodQueueEvents(OAuth2Credential credential, String userId, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "automod-queue." + userId + "." + channelId);
    }

    /**
     * Event Listener: User earned a new Bits badge and shared the notification with chat
     *
     * @param credential Credential (for target channel id, scope: bits:read)
     * @param channelId  Target Channel Id
     * @return PubSubSubscription
     */
    default PubSubSubscription listenForBitsBadgeEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "channel-bits-badge-unlocks." + channelId);
    }

    /**
     * Event Listener: Anyone cheers on a specified channel.
     *
     * @param credential Credential (for target channel id, scope: bits:read)
     * @param channelId  Target Channel Id
     * @return PubSubSubscription
     */
    default PubSubSubscription listenForCheerEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "channel-bits-events-v2." + channelId);
    }

    /**
     * Event Listener: Anyone subscribes (first month), resubscribes (subsequent months), or gifts a subscription to a channel.
     *
     * @param credential Credential (for targetChannelId, scope: channel_subscriptions)
     * @param channelId  Target Channel Id
     * @return PubSubSubscription
     */
    default PubSubSubscription listenForSubscriptionEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "channel-subscribe-events-v1." + channelId);
    }

    /**
     * Event Listener: Anyone makes a purchase on a channel.
     *
     * @param credential Credential (any)
     * @param channelId  Target Channel Id
     * @return PubSubSubscription
     */
    @Deprecated
    default PubSubSubscription listenForCommerceEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "channel-commerce-events-v1." + channelId);
    }

    /**
     * Event Listener: Anyone whispers the specified user.
     *
     * @param credential Credential (for targetUserId, scope: whispers:read)
     * @param userId     Target User Id
     * @return PubSubSubscription
     */
    default PubSubSubscription listenForWhisperEvents(OAuth2Credential credential, String userId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "whispers." + userId);
    }

    /**
     * Event Listener: A moderator performs an action in the channel
     *
     * @param credential Credential (for channelId, scope: channel:moderate)
     * @param channelId  Target Channel Id
     * @return PubSubSubscription
     * @deprecated in favor of listenForModerationEvents(OAuth2Credential, String, String)
     */
    @Deprecated
    @Unofficial
    default PubSubSubscription listenForModerationEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "chat_moderator_actions." + channelId);
    }

    /**
     * Event Listener: A moderator performs an action in the channel
     *
     * @param credential Credential (for userId, scope: channel:moderate)
     * @param userId     The user id associated with the credential
     * @param channelId  The user id associated with the target channel
     * @return PubSubSubscription
     */
    @SuppressWarnings("deprecated")
    default PubSubSubscription listenForModerationEvents(OAuth2Credential credential, String userId, String channelId) {
        return listenForModerationEvents(credential, userId + "." + channelId);
    }

    /**
     * Event Listener: A user’s message held by AutoMod has been approved or denied.
     *
     * @param credential Credential (for userId, scope: chat:read)
     * @param userId     The user id associated with the credential
     * @param channelId  The user id associated with the target channel
     * @return PubSubSubscription
     */
    default PubSubSubscription listenForUserModerationNotificationEvents(OAuth2Credential credential, String userId, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "user-moderation-notifications." + userId + "." + channelId);
    }

    /**
     * Event Listener: Anyone makes a channel points redemption on a channel.
     *
     * @param credential Credential (with the channel:read:redemptions scope for maximum information)
     * @param channelId  Target Channel Id
     * @return PubSubSubscription
     */
    default PubSubSubscription listenForChannelPointsRedemptionEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "community-points-channel-v1." + channelId);
    }

    /*
     * Undocumented topics - Use at your own risk
     */

    @Unofficial
    @Deprecated
    default PubSubSubscription listenForAdsEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "ads." + channelId);
    }

    @Unofficial
    @Deprecated
    default PubSubSubscription listenForAdPropertyRefreshEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "ad-property-refresh." + channelId);
    }

    @Unofficial
    default PubSubSubscription listenForAutomodLevelsModificationEvents(OAuth2Credential credential, String userId, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "automod-levels-modification." + userId + "." + channelId);
    }

    @Unofficial
    @Deprecated
    default PubSubSubscription listenForBountyBoardEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "channel-bounty-board-events.cta." + channelId);
    }

    @Unofficial
    @Deprecated
    default PubSubSubscription listenForDashboardActivityFeedEvents(OAuth2Credential credential, String userId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "dashboard-activity-feed." + userId);
    }

    @Unofficial
    default PubSubSubscription listenForCommunityBoostEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "community-boost-events-v1." + channelId);
    }

    @Unofficial
    default PubSubSubscription listenForCreatorGoalsEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "creator-goals-events-v1." + channelId);
    }

    @Unofficial
    default PubSubSubscription listenForCrowdChantEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "crowd-chant-channel-v1." + channelId);
    }

    @Unofficial
    default PubSubSubscription listenForUserChannelPointsEvents(OAuth2Credential credential, String userId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "community-points-user-v1." + userId);
    }

    @Unofficial
    @Deprecated
    default PubSubSubscription listenForChannelDropEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "channel-drop-events." + channelId);
    }

    @Unofficial
    default PubSubSubscription listenForChannelBitsLeaderboardEvents(OAuth2Credential credential, String channelId) {
        return listenForChannelBitsLeaderboardEvents(credential, channelId, "WEEK");
    }

    @Unofficial
    default PubSubSubscription listenForChannelBitsLeaderboardMonthlyEvents(OAuth2Credential credential, String channelId) {
        return listenForChannelBitsLeaderboardEvents(credential, channelId, "MONTH");
    }

    @Unofficial
    default PubSubSubscription listenForChannelBitsLeaderboardAllTimeEvents(OAuth2Credential credential, String channelId) {
        return listenForChannelBitsLeaderboardEvents(credential, channelId, "ALLTIME");
    }

    @Unofficial
    default PubSubSubscription listenForChannelBitsLeaderboardEvents(OAuth2Credential credential, String channelId, String timeAggregationUnit) {
        return listenOnTopic(PubSubType.LISTEN, credential, "leaderboard-events-v1.bits-usage-by-channel-v1-" + channelId + "-" + timeAggregationUnit);
    }

    @Unofficial
    @Deprecated
    default PubSubSubscription listenForChannelPrimeGiftStatusEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "channel-prime-gifting-status." + channelId);
    }

    @Unofficial
    default PubSubSubscription listenForChannelSubLeaderboardEvents(OAuth2Credential credential, String channelId) {
        return listenForChannelSubLeaderboardEvents(credential, channelId, "WEEK");
    }

    @Unofficial
    default PubSubSubscription listenForChannelSubLeaderboardMonthlyEvents(OAuth2Credential credential, String channelId) {
        return listenForChannelSubLeaderboardEvents(credential, channelId, "MONTH");
    }

    @Unofficial
    default PubSubSubscription listenForChannelSubLeaderboardAllTimeEvents(OAuth2Credential credential, String channelId) {
        return listenForChannelSubLeaderboardEvents(credential, channelId, "ALLTIME");
    }

    @Unofficial
    default PubSubSubscription listenForChannelSubLeaderboardEvents(OAuth2Credential credential, String channelId, String timeAggregationUnit) {
        return listenOnTopic(PubSubType.LISTEN, credential, "leaderboard-events-v1.sub-gifts-sent-" + channelId + "-" + timeAggregationUnit);
    }

    @Unofficial
    default PubSubSubscription listenForLeaderboardEvents(OAuth2Credential credential, String channelId) {
        return listenForLeaderboardEvents(credential, channelId, "WEEK");
    }

    @Unofficial
    default PubSubSubscription listenForLeaderboardMonthlyEvents(OAuth2Credential credential, String channelId) {
        return listenForLeaderboardEvents(credential, channelId, "MONTH");
    }

    @Unofficial
    default PubSubSubscription listenForLeaderboardAllTimeEvents(OAuth2Credential credential, String channelId) {
        return listenForLeaderboardEvents(credential,  channelId, "ALLTIME");
    }

    @Unofficial
    default PubSubSubscription listenForLeaderboardEvents(OAuth2Credential credential, String channelId, String timeAggregationUnit) {
        return listenOnTopic(
            PubSubType.LISTEN,
            credential,
            "leaderboard-events-v1.bits-usage-by-channel-v1-" + channelId + "-" + timeAggregationUnit,
            "leaderboard-events-v1.sub-gifts-sent-" + channelId + "-" + timeAggregationUnit
        );
    }

    @Unofficial
    default PubSubSubscription listenForChannelPredictionsEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "predictions-channel-v1." + channelId);
    }

    @Unofficial
    default PubSubSubscription listenForUserPredictionsEvents(OAuth2Credential credential, String userId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "predictions-user-v1." + userId);
    }

    @Unofficial
    default PubSubSubscription listenForChannelSubGiftsEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "channel-sub-gifts-v1." + channelId);
    }

    @Unofficial
    @Deprecated
    default PubSubSubscription listenForChannelSquadEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "channel-squad-updates." + channelId);
    }

    @Unofficial
    default PubSubSubscription listenForRaidEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "raid." + channelId);
    }

    @Unofficial
    default PubSubSubscription listenForChannelUnbanRequestEvents(OAuth2Credential credential, String userId, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "channel-unban-requests." + userId + '.' + channelId);
    }

    @Unofficial
    default PubSubSubscription listenForUserUnbanRequestEvents(OAuth2Credential credential, String userId, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "user-unban-requests." + userId + '.' + channelId);
    }

    @Unofficial
    @Deprecated
    default PubSubSubscription listenForChannelExtensionEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "channel-ext-v1." + channelId);
    }

    @Unofficial
    @Deprecated
    default PubSubSubscription listenForExtensionControlEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "extension-control." + channelId);
    }

    @Unofficial
    default PubSubSubscription listenForHypeTrainEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "hype-train-events-v1." + channelId);
    }

    @Unofficial
    default PubSubSubscription listenForHypeTrainRewardEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "hype-train-events-v1.rewards." + channelId);
    }

    @Unofficial
    @Deprecated
    default PubSubSubscription listenForBroadcastSettingUpdateEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "broadcast-settings-update." + channelId);
    }

    @Unofficial
    @Deprecated
    default PubSubSubscription listenForCelebrationEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "celebration-events-v1." + channelId);
    }

    @Unofficial
    @Deprecated
    default PubSubSubscription listenForPublicBitEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "channel-bit-events-public." + channelId);
    }

    @Unofficial
    default PubSubSubscription listenForPublicCheerEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "channel-cheer-events-public-v1." + channelId);
    }

    @Unofficial
    @Deprecated
    default PubSubSubscription listenForStreamChangeEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "stream-change-by-channel." + channelId);
    }

    @Unofficial
    @Deprecated
    default PubSubSubscription listenForStreamChatRoomEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "stream-chat-room-v1." + channelId);
    }

    @Unofficial
    @Deprecated
    default PubSubSubscription listenForChannelChatroomEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "chatrooms-channel-v1." + channelId);
    }

    @Unofficial
    default PubSubSubscription listenForUserChatroomEvents(OAuth2Credential credential, String userId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "chatrooms-user-v1." + userId);
    }

    @Unofficial
    @Deprecated
    default PubSubSubscription listenForUserBitsUpdateEvents(OAuth2Credential credential, String userId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "user-bits-updates-v1." + userId);
    }

    @Unofficial
    @Deprecated
    default PubSubSubscription listenForUserCampaignEvents(OAuth2Credential credential, String userId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "user-campaign-events." + userId);
    }

    @Unofficial
    @Deprecated
    default PubSubSubscription listenForUserDropEvents(OAuth2Credential credential, String userId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "user-drop-events." + userId);
    }

    @Unofficial
    @Deprecated
    default PubSubSubscription listenForUserPropertiesUpdateEvents(OAuth2Credential credential, String userId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "user-properties-update." + userId);
    }

    @Unofficial
    @Deprecated
    default PubSubSubscription listenForUserSubscribeEvents(OAuth2Credential credential, String userId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "user-subscribe-events-v1." + userId);
    }

    @Unofficial
    @Deprecated
    default PubSubSubscription listenForUserImageUpdateEvents(OAuth2Credential credential, String userId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "user-image-update." + userId);
    }

    /**
     * Event Listener: Anyone follows the specified channel.
     *
     * @param credential {@link OAuth2Credential}
     * @param channelId  the id for the channel
     * @return PubSubSubscription
     */
    @Unofficial
    default PubSubSubscription listenForFollowingEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "following." + channelId);
    }

    @Unofficial
    default PubSubSubscription listenForFriendshipEvents(OAuth2Credential credential, String userId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "friendship." + userId);
    }

    @Unofficial
    default PubSubSubscription listenForLowTrustUsersEvents(OAuth2Credential credential, String userId, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "low-trust-users." + userId + "." + channelId);
    }

    @Unofficial
    default PubSubSubscription listenForOnsiteNotificationEvents(OAuth2Credential credential, String userId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "onsite-notifications." + userId);
    }

    @Unofficial
    default PubSubSubscription listenForPollEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "polls." + channelId);
    }

    @Unofficial
    default PubSubSubscription listenForPresenceEvents(OAuth2Credential credential, String userId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "presence." + userId);
    }

    @Unofficial
    default PubSubSubscription listenForRadioEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "radio-events-v1." + channelId);
    }

    @Unofficial
    default PubSubSubscription listenForVideoPlaybackEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "video-playback-by-id." + channelId);
    }

    @Unofficial
    default PubSubSubscription listenForVideoPlaybackByNameEvents(OAuth2Credential credential, String channelName) {
        return listenOnTopic(PubSubType.LISTEN, credential, "video-playback." + channelName.toLowerCase());
    }

    @Unofficial
    @Deprecated
    default PubSubSubscription listenForWatchPartyEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "pv-watch-party-events." + channelId);
    }

}
