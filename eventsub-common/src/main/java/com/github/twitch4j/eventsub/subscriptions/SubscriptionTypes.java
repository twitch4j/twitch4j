package com.github.twitch4j.eventsub.subscriptions;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SubscriptionTypes {
    public final ChannelBanType CHANNEL_BAN = new ChannelBanType();
    public final ChannelCheerType CHANNEL_CHEER = new ChannelCheerType();
    public final ChannelFollowType CHANNEL_FOLLOW = new ChannelFollowType();
    public final ChannelPointsCustomRewardAddType CHANNEL_POINTS_CUSTOM_REWARD_ADD = new ChannelPointsCustomRewardAddType();
    public final ChannelPointsCustomRewardRedemptionAddType CHANNEL_POINTS_CUSTOM_REWARD_REDEMPTION_ADD = new ChannelPointsCustomRewardRedemptionAddType();
    public final ChannelPointsCustomRewardRedemptionUpdateType CHANNEL_POINTS_CUSTOM_REWARD_REDEMPTION_UPDATE = new ChannelPointsCustomRewardRedemptionUpdateType();
    public final ChannelPointsCustomRewardRemoveType CHANNEL_POINTS_CUSTOM_REWARD_REMOVE = new ChannelPointsCustomRewardRemoveType();
    public final ChannelPointsCustomRewardUpdateType CHANNEL_POINTS_CUSTOM_REWARD_UPDATE = new ChannelPointsCustomRewardUpdateType();
    public final ChannelSubscribeType CHANNEL_SUBSCRIBE = new ChannelSubscribeType();
    public final ChannelUnbanType CHANNEL_UNBAN = new ChannelUnbanType();
    public final ChannelUpdateType CHANNEL_UPDATE = new ChannelUpdateType();
    public final HypeTrainBeginType HYPE_TRAIN_BEGIN = new HypeTrainBeginType();
    public final HypeTrainEndType HYPE_TRAIN_END = new HypeTrainEndType();
    public final HypeTrainProgressType HYPE_TRAIN_PROGRESS = new HypeTrainProgressType();
    public final StreamOfflineType STREAM_OFFLINE = new StreamOfflineType();
    public final StreamOnlineType STREAM_ONLINE = new StreamOnlineType();
    public final UserAuthorizationRevokeType USER_AUTHORIZATION_REVOKE = new UserAuthorizationRevokeType();
    public final UserUpdateType USER_UPDATE = new UserUpdateType();
}
