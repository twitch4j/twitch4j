package com.github.twitch4j.pubsub.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.AutomaticRewardRedemption;
import com.github.twitch4j.pubsub.domain.ChannelPointsRedemption;
import com.github.twitch4j.pubsub.domain.ChannelPointsReward;
import com.github.twitch4j.pubsub.domain.CommunityGoalContribution;
import com.github.twitch4j.pubsub.domain.RedemptionProgress;
import com.github.twitch4j.pubsub.events.AutomaticRewardRedeemedEvent;
import com.github.twitch4j.pubsub.events.CommunityGoalContributionEvent;
import com.github.twitch4j.pubsub.events.CustomRewardCreatedEvent;
import com.github.twitch4j.pubsub.events.CustomRewardDeletedEvent;
import com.github.twitch4j.pubsub.events.CustomRewardUpdatedEvent;
import com.github.twitch4j.pubsub.events.RedemptionStatusUpdateEvent;
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;
import com.github.twitch4j.pubsub.events.UpdateRedemptionFinishedEvent;
import com.github.twitch4j.pubsub.events.UpdateRedemptionProgressEvent;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;

class ChannelPointsHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "channel-points-channel-v1";
    }

    @Override
    public Collection<String> topicNames() {
        return Arrays.asList(topicName(), "community-points-channel-v1");
    }

    @Override
    public TwitchEvent apply(Args args) {
        JsonNode msgData = args.getData();
        String timestampText = msgData.path("timestamp").asText();
        Instant instant = Instant.parse(timestampText);

        switch (args.getType()) {
            case "automatic-reward-redeemed":
                AutomaticRewardRedemption autoRedemption = TypeConvert.convertValue(msgData.path("redemption"), AutomaticRewardRedemption.class);
                return new AutomaticRewardRedeemedEvent(instant, autoRedemption);
            case "reward-redeemed":
                ChannelPointsRedemption redemption = TypeConvert.convertValue(msgData.path("redemption"), ChannelPointsRedemption.class);
                return new RewardRedeemedEvent(instant, redemption);
            case "redemption-status-update":
                ChannelPointsRedemption updatedRedemption = TypeConvert.convertValue(msgData.path("redemption"), ChannelPointsRedemption.class);
                return new RedemptionStatusUpdateEvent(instant, updatedRedemption);
            case "custom-reward-created":
                ChannelPointsReward newReward = TypeConvert.convertValue(msgData.path("new_reward"), ChannelPointsReward.class);
                return new CustomRewardCreatedEvent(instant, newReward);
            case "custom-reward-updated":
                ChannelPointsReward updatedReward = TypeConvert.convertValue(msgData.path("updated_reward"), ChannelPointsReward.class);
                return new CustomRewardUpdatedEvent(instant, updatedReward);
            case "custom-reward-deleted":
                ChannelPointsReward deletedReward = TypeConvert.convertValue(msgData.path("deleted_reward"), ChannelPointsReward.class);
                return new CustomRewardDeletedEvent(instant, deletedReward);
            case "update-redemption-statuses-progress":
                RedemptionProgress redemptionProgress = TypeConvert.convertValue(msgData.path("progress"), RedemptionProgress.class);
                return new UpdateRedemptionProgressEvent(instant, redemptionProgress);
            case "update-redemption-statuses-finished":
                RedemptionProgress redemptionFinished = TypeConvert.convertValue(msgData.path("progress"), RedemptionProgress.class);
                return new UpdateRedemptionFinishedEvent(instant, redemptionFinished);
            case "community-goal-contribution":
                CommunityGoalContribution contribution = TypeConvert.convertValue(msgData.path("contribution"), CommunityGoalContribution.class);
                return new CommunityGoalContributionEvent(instant, contribution);
            default:
                return null;
        }
    }
}
