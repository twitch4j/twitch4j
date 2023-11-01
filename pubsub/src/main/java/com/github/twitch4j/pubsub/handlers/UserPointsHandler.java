package com.github.twitch4j.pubsub.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.ChannelPointsEarned;
import com.github.twitch4j.pubsub.domain.ChannelPointsRedemption;
import com.github.twitch4j.pubsub.domain.ClaimData;
import com.github.twitch4j.pubsub.domain.CommunityGoalContribution;
import com.github.twitch4j.pubsub.domain.PointsSpent;
import com.github.twitch4j.pubsub.events.ClaimAvailableEvent;
import com.github.twitch4j.pubsub.events.ClaimClaimedEvent;
import com.github.twitch4j.pubsub.events.PointsEarnedEvent;
import com.github.twitch4j.pubsub.events.PointsSpentEvent;
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;
import com.github.twitch4j.pubsub.events.UserCommunityGoalContributionEvent;

import java.time.Instant;

class UserPointsHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "community-points-user-v1";
    }

    @Override
    public TwitchEvent apply(Args args) {
        JsonNode msgData = args.getData();
        switch (args.getType()) {
            case "points-earned":
                final ChannelPointsEarned pointsEarned = TypeConvert.convertValue(msgData, ChannelPointsEarned.class);
                return new PointsEarnedEvent(pointsEarned);
            case "claim-available":
                final ClaimData claimAvailable = TypeConvert.convertValue(msgData, ClaimData.class);
                return new ClaimAvailableEvent(claimAvailable);
            case "claim-claimed":
                final ClaimData claimClaimed = TypeConvert.convertValue(msgData, ClaimData.class);
                return new ClaimClaimedEvent(claimClaimed);
            case "points-spent":
                final PointsSpent pointsSpent = TypeConvert.convertValue(msgData, PointsSpent.class);
                return new PointsSpentEvent(pointsSpent);
            case "reward-redeemed":
                final ChannelPointsRedemption redemption = TypeConvert.convertValue(msgData.path("redemption"), ChannelPointsRedemption.class);
                return new RewardRedeemedEvent(Instant.parse(msgData.path("timestamp").asText()), redemption);
            case "community-goal-contribution":
                CommunityGoalContribution goal = TypeConvert.convertValue(msgData.path("contribution"), CommunityGoalContribution.class);
                Instant instant = Instant.parse(msgData.path("timestamp").textValue());
                return new UserCommunityGoalContributionEvent(args.getLastTopicPart(), instant, goal);
            case "global-last-viewed-content-updated":
            case "channel-last-viewed-content-updated":
                // unimportant
                return new TwitchEvent() {};
            default:
                return null;
        }
    }
}
