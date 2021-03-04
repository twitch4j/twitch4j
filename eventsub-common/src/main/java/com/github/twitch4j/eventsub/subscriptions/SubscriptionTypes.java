package com.github.twitch4j.eventsub.subscriptions;

import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class SubscriptionTypes {
    private final Map<String, SubscriptionType<?, ?, ?>> SUBSCRIPTION_TYPES;
    public final ChannelBanType CHANNEL_BAN;
    public final ChannelCheerType CHANNEL_CHEER;
    public final ChannelFollowType CHANNEL_FOLLOW;
    public final ChannelPointsCustomRewardAddType CHANNEL_POINTS_CUSTOM_REWARD_ADD;
    public final ChannelPointsCustomRewardRedemptionAddType CHANNEL_POINTS_CUSTOM_REWARD_REDEMPTION_ADD;
    public final ChannelPointsCustomRewardRedemptionUpdateType CHANNEL_POINTS_CUSTOM_REWARD_REDEMPTION_UPDATE;
    public final ChannelPointsCustomRewardRemoveType CHANNEL_POINTS_CUSTOM_REWARD_REMOVE;
    public final ChannelPointsCustomRewardUpdateType CHANNEL_POINTS_CUSTOM_REWARD_UPDATE;
    public final ChannelRaidType CHANNEL_RAID;
    public final ChannelSubscribeType CHANNEL_SUBSCRIBE;
    public final ChannelUnbanType CHANNEL_UNBAN;
    public final ChannelUpdateType CHANNEL_UPDATE;
    public final HypeTrainBeginType HYPE_TRAIN_BEGIN;
    public final HypeTrainEndType HYPE_TRAIN_END;
    public final HypeTrainProgressType HYPE_TRAIN_PROGRESS;
    public final StreamOfflineType STREAM_OFFLINE;
    public final StreamOnlineType STREAM_ONLINE;
    public final UserAuthorizationRevokeType USER_AUTHORIZATION_REVOKE;
    public final UserUpdateType USER_UPDATE;

    public SubscriptionType<?, ?, ?> getSubscriptionType(String subTypeName, String subTypeVersion) {
        return SUBSCRIPTION_TYPES.get(subTypeName + ':' + subTypeVersion);
    }

    static {
        SUBSCRIPTION_TYPES = Collections.unmodifiableMap(
            Stream.of(
                CHANNEL_BAN = new ChannelBanType(),
                CHANNEL_CHEER = new ChannelCheerType(),
                CHANNEL_FOLLOW = new ChannelFollowType(),
                CHANNEL_POINTS_CUSTOM_REWARD_ADD = new ChannelPointsCustomRewardAddType(),
                CHANNEL_POINTS_CUSTOM_REWARD_REDEMPTION_ADD = new ChannelPointsCustomRewardRedemptionAddType(),
                CHANNEL_POINTS_CUSTOM_REWARD_REDEMPTION_UPDATE = new ChannelPointsCustomRewardRedemptionUpdateType(),
                CHANNEL_POINTS_CUSTOM_REWARD_REMOVE = new ChannelPointsCustomRewardRemoveType(),
                CHANNEL_POINTS_CUSTOM_REWARD_UPDATE = new ChannelPointsCustomRewardUpdateType(),
                CHANNEL_RAID = new ChannelRaidType(),
                CHANNEL_SUBSCRIBE = new ChannelSubscribeType(),
                CHANNEL_UNBAN = new ChannelUnbanType(),
                CHANNEL_UPDATE = new ChannelUpdateType(),
                HYPE_TRAIN_BEGIN = new HypeTrainBeginType(),
                HYPE_TRAIN_END = new HypeTrainEndType(),
                HYPE_TRAIN_PROGRESS = new HypeTrainProgressType(),
                STREAM_OFFLINE = new StreamOfflineType(),
                STREAM_ONLINE = new StreamOnlineType(),
                USER_AUTHORIZATION_REVOKE = new UserAuthorizationRevokeType(),
                USER_UPDATE = new UserUpdateType()
            ).collect(Collectors.toMap(type -> type.getName() + ':' + type.getVersion(), Function.identity()))
        );
    }
}
