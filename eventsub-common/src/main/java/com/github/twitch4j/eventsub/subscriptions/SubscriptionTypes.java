package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.common.annotation.Unofficial;
import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class SubscriptionTypes {
    private final Map<String, SubscriptionType<?, ?, ?>> SUBSCRIPTION_TYPES;
    @Unofficial public final BetaPollBeginType BETA_POLL_BEGIN;
    @Unofficial public final BetaPollProgressType BETA_POLL_PROGRESS;
    @Unofficial public final BetaPollEndType BETA_POLL_END;
    @Unofficial public final BetaPredictionBeginType BETA_PREDICTION_BEGIN;
    @Unofficial public final BetaPredictionProgressType BETA_PREDICTION_PROGRESS;
    @Unofficial public final BetaPredictionLockType BETA_PREDICTION_LOCK;
    @Unofficial public final BetaPredictionEndType BETA_PREDICTION_END;
    public final ChannelBanType CHANNEL_BAN;
    public final ChannelCheerType CHANNEL_CHEER;
    public final ChannelFollowType CHANNEL_FOLLOW;
    public final ChannelModeratorAddType CHANNEL_MODERATOR_ADD;
    public final ChannelModeratorRemoveType CHANNEL_MODERATOR_REMOVE;
    public final ChannelPointsCustomRewardAddType CHANNEL_POINTS_CUSTOM_REWARD_ADD;
    public final ChannelPointsCustomRewardRedemptionAddType CHANNEL_POINTS_CUSTOM_REWARD_REDEMPTION_ADD;
    public final ChannelPointsCustomRewardRedemptionUpdateType CHANNEL_POINTS_CUSTOM_REWARD_REDEMPTION_UPDATE;
    public final ChannelPointsCustomRewardRemoveType CHANNEL_POINTS_CUSTOM_REWARD_REMOVE;
    public final ChannelPointsCustomRewardUpdateType CHANNEL_POINTS_CUSTOM_REWARD_UPDATE;
    public final ChannelRaidType CHANNEL_RAID;
    public final ChannelSubscribeType CHANNEL_SUBSCRIBE;
    @Unofficial public final BetaChannelUnsubscribeType BETA_CHANNEL_UNSUBSCRIBE;
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
                BETA_POLL_BEGIN = new BetaPollBeginType(),
                BETA_POLL_PROGRESS = new BetaPollProgressType(),
                BETA_POLL_END = new BetaPollEndType(),
                BETA_PREDICTION_BEGIN = new BetaPredictionBeginType(),
                BETA_PREDICTION_PROGRESS = new BetaPredictionProgressType(),
                BETA_PREDICTION_LOCK = new BetaPredictionLockType(),
                BETA_PREDICTION_END = new BetaPredictionEndType(),
                CHANNEL_BAN = new ChannelBanType(),
                CHANNEL_CHEER = new ChannelCheerType(),
                CHANNEL_FOLLOW = new ChannelFollowType(),
                CHANNEL_MODERATOR_ADD = new ChannelModeratorAddType(),
                CHANNEL_MODERATOR_REMOVE = new ChannelModeratorRemoveType(),
                CHANNEL_POINTS_CUSTOM_REWARD_ADD = new ChannelPointsCustomRewardAddType(),
                CHANNEL_POINTS_CUSTOM_REWARD_REDEMPTION_ADD = new ChannelPointsCustomRewardRedemptionAddType(),
                CHANNEL_POINTS_CUSTOM_REWARD_REDEMPTION_UPDATE = new ChannelPointsCustomRewardRedemptionUpdateType(),
                CHANNEL_POINTS_CUSTOM_REWARD_REMOVE = new ChannelPointsCustomRewardRemoveType(),
                CHANNEL_POINTS_CUSTOM_REWARD_UPDATE = new ChannelPointsCustomRewardUpdateType(),
                CHANNEL_RAID = new ChannelRaidType(),
                CHANNEL_SUBSCRIBE = new ChannelSubscribeType(),
                BETA_CHANNEL_UNSUBSCRIBE = new BetaChannelUnsubscribeType(),
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
