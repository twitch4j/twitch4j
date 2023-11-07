package com.github.twitch4j.eventsub.subscriptions;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
@SuppressWarnings({ "deprecation", "DeprecatedIsStillUsed" })
public class SubscriptionTypes {
    private final Map<String, SubscriptionType<?, ?, ?>> SUBSCRIPTION_TYPES;

    public final @ApiStatus.Experimental BetaChannelAdBreakBeginType BETA_CHANNEL_AD_BREAK_BEGIN;
    public final ChannelBanType CHANNEL_BAN;
    public final @ApiStatus.Experimental BetaChannelChatClearType BETA_CHANNEL_CHAT_CLEAR;
    public final @ApiStatus.Experimental BetaChannelClearUserMessagesType BETA_CHANNEL_CLEAR_USER_MESSAGES;
    public final @ApiStatus.Experimental BetaChannelMessageDeleteType BETA_CHANNEL_CHAT_MESSAGE_DELETE;
    public final @ApiStatus.Experimental BetaChannelChatNotificationType BETA_CHANNEL_CHAT_NOTIFICATION;
    public final ChannelCharityDonateType CHANNEL_CHARITY_DONATE;
    public final CharityCampaignStartType CHANNEL_CHARITY_START;
    public final CharityCampaignProgressType CHANNEL_CHARITY_PROGRESS;
    public final CharityCampaignStopType CHANNEL_CHARITY_STOP;
    public final ChannelCheerType CHANNEL_CHEER;
    public final @Deprecated ChannelFollowType CHANNEL_FOLLOW;
    public final ChannelFollowTypeV2 CHANNEL_FOLLOW_V2;
    public final ChannelGoalBeginType CHANNEL_GOAL_BEGIN;
    public final ChannelGoalProgressType CHANNEL_GOAL_PROGRESS;
    public final ChannelGoalEndType CHANNEL_GOAL_END;
    public final ChannelModeratorAddType CHANNEL_MODERATOR_ADD;
    public final ChannelModeratorRemoveType CHANNEL_MODERATOR_REMOVE;
    public final ChannelPointsCustomRewardAddType CHANNEL_POINTS_CUSTOM_REWARD_ADD;
    public final ChannelPointsCustomRewardRedemptionAddType CHANNEL_POINTS_CUSTOM_REWARD_REDEMPTION_ADD;
    public final ChannelPointsCustomRewardRedemptionUpdateType CHANNEL_POINTS_CUSTOM_REWARD_REDEMPTION_UPDATE;
    public final ChannelPointsCustomRewardRemoveType CHANNEL_POINTS_CUSTOM_REWARD_REMOVE;
    public final ChannelPointsCustomRewardUpdateType CHANNEL_POINTS_CUSTOM_REWARD_UPDATE;
    public final ChannelRaidType CHANNEL_RAID;
    public final ChannelSubscribeType CHANNEL_SUBSCRIBE;
    public final ChannelSubscriptionEndType CHANNEL_SUBSCRIPTION_END;
    public final ChannelSubscriptionGiftType CHANNEL_SUBSCRIPTION_GIFT;
    public final ChannelSubscriptionMessageType CHANNEL_SUBSCRIPTION_MESSAGE;
    public final ChannelUnbanType CHANNEL_UNBAN;
    public final @Deprecated ChannelUpdateType CHANNEL_UPDATE;
    public final ChannelUpdateV2Type CHANNEL_UPDATE_V2;
    public final DropEntitlementGrantType DROP_ENTITLEMENT_GRANT;
    public final ExtensionBitsTransactionCreateType EXTENSION_BITS_TRANSACTION_CREATE;
    public final HypeTrainBeginType HYPE_TRAIN_BEGIN;
    public final HypeTrainEndType HYPE_TRAIN_END;
    public final HypeTrainProgressType HYPE_TRAIN_PROGRESS;
    public final PollBeginType POLL_BEGIN;
    public final PollProgressType POLL_PROGRESS;
    public final PollEndType POLL_END;
    public final PredictionBeginType PREDICTION_BEGIN;
    public final PredictionProgressType PREDICTION_PROGRESS;
    public final PredictionLockType PREDICTION_LOCK;
    public final PredictionEndType PREDICTION_END;
    public final ShieldModeBeginType SHIELD_MODE_BEGIN_TYPE;
    public final ShieldModeEndType SHIELD_MODE_END_TYPE;
    public final ShoutoutCreateType SHOUTOUT_CREATE_TYPE;
    public final ShoutoutReceiveType SHOUTOUT_RECEIVE_TYPE;
    public final StreamOfflineType STREAM_OFFLINE;
    public final StreamOnlineType STREAM_ONLINE;
    public final UserAuthorizationGrantType USER_AUTHORIZATION_GRANT;
    public final UserAuthorizationRevokeType USER_AUTHORIZATION_REVOKE;
    public final UserUpdateType USER_UPDATE;

    public SubscriptionType<?, ?, ?> getSubscriptionType(String subTypeName, String subTypeVersion) {
        return SUBSCRIPTION_TYPES.get(subTypeName + ':' + subTypeVersion);
    }

    static {
        SUBSCRIPTION_TYPES = Collections.unmodifiableMap(
            Stream.of(
                BETA_CHANNEL_AD_BREAK_BEGIN = new BetaChannelAdBreakBeginType(),
                CHANNEL_BAN = new ChannelBanType(),
                BETA_CHANNEL_CHAT_CLEAR = new BetaChannelChatClearType(),
                BETA_CHANNEL_CLEAR_USER_MESSAGES = new BetaChannelClearUserMessagesType(),
                BETA_CHANNEL_CHAT_MESSAGE_DELETE = new BetaChannelMessageDeleteType(),
                BETA_CHANNEL_CHAT_NOTIFICATION = new BetaChannelChatNotificationType(),
                CHANNEL_CHARITY_DONATE = new ChannelCharityDonateType(),
                CHANNEL_CHARITY_START = new CharityCampaignStartType(),
                CHANNEL_CHARITY_PROGRESS = new CharityCampaignProgressType(),
                CHANNEL_CHARITY_STOP = new CharityCampaignStopType(),
                CHANNEL_CHEER = new ChannelCheerType(),
                CHANNEL_FOLLOW = new ChannelFollowType(),
                CHANNEL_FOLLOW_V2 = new ChannelFollowTypeV2(),
                CHANNEL_GOAL_BEGIN = new ChannelGoalBeginType(),
                CHANNEL_GOAL_PROGRESS = new ChannelGoalProgressType(),
                CHANNEL_GOAL_END = new ChannelGoalEndType(),
                CHANNEL_MODERATOR_ADD = new ChannelModeratorAddType(),
                CHANNEL_MODERATOR_REMOVE = new ChannelModeratorRemoveType(),
                CHANNEL_POINTS_CUSTOM_REWARD_ADD = new ChannelPointsCustomRewardAddType(),
                CHANNEL_POINTS_CUSTOM_REWARD_REDEMPTION_ADD = new ChannelPointsCustomRewardRedemptionAddType(),
                CHANNEL_POINTS_CUSTOM_REWARD_REDEMPTION_UPDATE = new ChannelPointsCustomRewardRedemptionUpdateType(),
                CHANNEL_POINTS_CUSTOM_REWARD_REMOVE = new ChannelPointsCustomRewardRemoveType(),
                CHANNEL_POINTS_CUSTOM_REWARD_UPDATE = new ChannelPointsCustomRewardUpdateType(),
                CHANNEL_RAID = new ChannelRaidType(),
                CHANNEL_SUBSCRIBE = new ChannelSubscribeType(),
                CHANNEL_SUBSCRIPTION_END = new ChannelSubscriptionEndType(),
                CHANNEL_SUBSCRIPTION_GIFT = new ChannelSubscriptionGiftType(),
                CHANNEL_SUBSCRIPTION_MESSAGE = new ChannelSubscriptionMessageType(),
                CHANNEL_UNBAN = new ChannelUnbanType(),
                CHANNEL_UPDATE = new ChannelUpdateType(),
                CHANNEL_UPDATE_V2 = new ChannelUpdateV2Type(),
                DROP_ENTITLEMENT_GRANT = new DropEntitlementGrantType(),
                EXTENSION_BITS_TRANSACTION_CREATE = new ExtensionBitsTransactionCreateType(),
                HYPE_TRAIN_BEGIN = new HypeTrainBeginType(),
                HYPE_TRAIN_END = new HypeTrainEndType(),
                HYPE_TRAIN_PROGRESS = new HypeTrainProgressType(),
                POLL_BEGIN = new PollBeginType(),
                POLL_PROGRESS = new PollProgressType(),
                POLL_END = new PollEndType(),
                PREDICTION_BEGIN = new PredictionBeginType(),
                PREDICTION_PROGRESS = new PredictionProgressType(),
                PREDICTION_LOCK = new PredictionLockType(),
                PREDICTION_END = new PredictionEndType(),
                SHIELD_MODE_BEGIN_TYPE = new ShieldModeBeginType(),
                SHIELD_MODE_END_TYPE = new ShieldModeEndType(),
                SHOUTOUT_CREATE_TYPE = new ShoutoutCreateType(),
                SHOUTOUT_RECEIVE_TYPE = new ShoutoutReceiveType(),
                STREAM_OFFLINE = new StreamOfflineType(),
                STREAM_ONLINE = new StreamOnlineType(),
                USER_AUTHORIZATION_GRANT = new UserAuthorizationGrantType(),
                USER_AUTHORIZATION_REVOKE = new UserAuthorizationRevokeType(),
                USER_UPDATE = new UserUpdateType()
            ).collect(Collectors.toMap(type -> type.getName() + ':' + type.getVersion(), Function.identity()))
        );
    }
}
