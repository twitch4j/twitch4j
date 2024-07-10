package com.github.twitch4j.pubsub.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.HypeLevelUp;
import com.github.twitch4j.pubsub.domain.HypeProgression;
import com.github.twitch4j.pubsub.domain.HypeTrainApproaching;
import com.github.twitch4j.pubsub.domain.HypeTrainConductor;
import com.github.twitch4j.pubsub.domain.HypeTrainEnd;
import com.github.twitch4j.pubsub.domain.HypeTrainRewardsData;
import com.github.twitch4j.pubsub.domain.HypeTrainStart;
import com.github.twitch4j.pubsub.domain.SupportActivityFeedData;
import com.github.twitch4j.pubsub.events.HypeTrainApproachingEvent;
import com.github.twitch4j.pubsub.events.HypeTrainConductorUpdateEvent;
import com.github.twitch4j.pubsub.events.HypeTrainCooldownExpirationEvent;
import com.github.twitch4j.pubsub.events.HypeTrainEndEvent;
import com.github.twitch4j.pubsub.events.HypeTrainLevelUpEvent;
import com.github.twitch4j.pubsub.events.HypeTrainProgressionEvent;
import com.github.twitch4j.pubsub.events.HypeTrainRewardsEvent;
import com.github.twitch4j.pubsub.events.HypeTrainStartEvent;
import com.github.twitch4j.pubsub.events.SupportActivityFeedEvent;

import java.util.Arrays;
import java.util.Collection;

class TrainHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "hype-train-events-v2";
    }

    @Override
    public Collection<String> topicNames() {
        return Arrays.asList(topicName(), "hype-train-events-v1");
    }

    @Override
    public TwitchEvent apply(Args args) {
        String[] topicParts = args.getTopicParts();
        if (topicParts.length > 2 && "rewards".equals(topicParts[1])) {
            return new HypeTrainRewardsEvent(TypeConvert.convertValue(args.getData(), HypeTrainRewardsData.class));
        }
        JsonNode msgData = args.getData();
        String lastTopicIdentifier = args.getLastTopicPart();
        switch (args.getType()) {
            case "hype-train-approaching":
                final HypeTrainApproaching approachData = TypeConvert.convertValue(msgData, HypeTrainApproaching.class);
                return new HypeTrainApproachingEvent(approachData);
            case "hype-train-start":
                final HypeTrainStart startData = TypeConvert.convertValue(msgData, HypeTrainStart.class);
                return new HypeTrainStartEvent(lastTopicIdentifier, startData);
            case "hype-train-progression":
                final HypeProgression progressionData = TypeConvert.convertValue(msgData, HypeProgression.class);
                return new HypeTrainProgressionEvent(lastTopicIdentifier, progressionData);
            case "hype-train-level-up":
                final HypeLevelUp levelUpData = TypeConvert.convertValue(msgData, HypeLevelUp.class);
                return new HypeTrainLevelUpEvent(lastTopicIdentifier, levelUpData);
            case "hype-train-end":
                final HypeTrainEnd endData = TypeConvert.convertValue(msgData, HypeTrainEnd.class);
                return new HypeTrainEndEvent(lastTopicIdentifier, endData);
            case "hype-train-conductor-update":
                final HypeTrainConductor conductorData = TypeConvert.convertValue(msgData, HypeTrainConductor.class);
                return new HypeTrainConductorUpdateEvent(lastTopicIdentifier, conductorData);
            case "hype-train-cooldown-expiration":
                //noinspection deprecation
                return new HypeTrainCooldownExpirationEvent(lastTopicIdentifier);
            case "last-x-experiment-event":
                // note: this isn't a true hype train event (it can be fired with no train active), but twitch hacked together the feature to use the hype pubsub infrastructure
                final SupportActivityFeedData lastData = TypeConvert.convertValue(msgData, SupportActivityFeedData.class);
                return new SupportActivityFeedEvent(lastTopicIdentifier, lastData);
            default:
                return null;
        }
    }
}
