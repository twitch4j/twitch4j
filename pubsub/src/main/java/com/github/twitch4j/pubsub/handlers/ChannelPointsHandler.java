package com.github.twitch4j.pubsub.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.philippheuer.events4j.api.IEventManager;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.ChannelPointsRedemption;
import com.github.twitch4j.pubsub.domain.ChannelPointsReward;
import com.github.twitch4j.pubsub.domain.CommunityGoalContribution;
import com.github.twitch4j.pubsub.domain.RedemptionProgress;
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
    public boolean handle(Args args) {
        JsonNode msgData = args.getData();
        String timestampText = msgData.path("timestamp").asText();
        Instant instant = Instant.parse(timestampText);
        IEventManager eventManager = args.getEventManager();

        switch (args.getType()) {
            case "reward-redeemed":
                ChannelPointsRedemption redemption = TypeConvert.convertValue(msgData.path("redemption"), ChannelPointsRedemption.class);
                eventManager.publish(new RewardRedeemedEvent(instant, redemption));
                return true;
            case "redemption-status-update":
                ChannelPointsRedemption updatedRedemption = TypeConvert.convertValue(msgData.path("redemption"), ChannelPointsRedemption.class);
                eventManager.publish(new RedemptionStatusUpdateEvent(instant, updatedRedemption));
                return true;
            case "custom-reward-created":
                ChannelPointsReward newReward = TypeConvert.convertValue(msgData.path("new_reward"), ChannelPointsReward.class);
                eventManager.publish(new CustomRewardCreatedEvent(instant, newReward));
                return true;
            case "custom-reward-updated":
                ChannelPointsReward updatedReward = TypeConvert.convertValue(msgData.path("updated_reward"), ChannelPointsReward.class);
                eventManager.publish(new CustomRewardUpdatedEvent(instant, updatedReward));
                return true;
            case "custom-reward-deleted":
                ChannelPointsReward deletedReward = TypeConvert.convertValue(msgData.path("deleted_reward"), ChannelPointsReward.class);
                eventManager.publish(new CustomRewardDeletedEvent(instant, deletedReward));
                return true;
            case "update-redemption-statuses-progress":
                RedemptionProgress redemptionProgress = TypeConvert.convertValue(msgData.path("progress"), RedemptionProgress.class);
                eventManager.publish(new UpdateRedemptionProgressEvent(instant, redemptionProgress));
                return true;
            case "update-redemption-statuses-finished":
                RedemptionProgress redemptionFinished = TypeConvert.convertValue(msgData.path("progress"), RedemptionProgress.class);
                eventManager.publish(new UpdateRedemptionFinishedEvent(instant, redemptionFinished));
                return true;
            case "community-goal-contribution":
                CommunityGoalContribution contribution = TypeConvert.convertValue(msgData.path("contribution"), CommunityGoalContribution.class);
                eventManager.publish(new CommunityGoalContributionEvent(instant, contribution));
                return true;
            default:
                return false;
        }
    }
}
