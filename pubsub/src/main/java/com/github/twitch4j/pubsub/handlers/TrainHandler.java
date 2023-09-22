package com.github.twitch4j.pubsub.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.philippheuer.events4j.api.IEventManager;
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

class TrainHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "hype-train-events-v1";
    }

    @Override
    public boolean handle(Args args) {
        String[] topicParts = args.getTopicParts();
        IEventManager eventManager = args.getEventManager();
        if (topicParts.length > 2 && "rewards".equals(topicParts[1])) {
            eventManager.publish(new HypeTrainRewardsEvent(TypeConvert.convertValue(args.getData(), HypeTrainRewardsData.class)));
            return true;
        }
        JsonNode msgData = args.getData();
        String lastTopicIdentifier = args.getLastTopicPart();
        switch (args.getType()) {
            case "hype-train-approaching":
                final HypeTrainApproaching approachData = TypeConvert.convertValue(msgData, HypeTrainApproaching.class);
                eventManager.publish(new HypeTrainApproachingEvent(approachData));
                return true;
            case "hype-train-start":
                final HypeTrainStart startData = TypeConvert.convertValue(msgData, HypeTrainStart.class);
                eventManager.publish(new HypeTrainStartEvent(startData));
                return true;
            case "hype-train-progression":
                final HypeProgression progressionData = TypeConvert.convertValue(msgData, HypeProgression.class);
                eventManager.publish(new HypeTrainProgressionEvent(lastTopicIdentifier, progressionData));
                return true;
            case "hype-train-level-up":
                final HypeLevelUp levelUpData = TypeConvert.convertValue(msgData, HypeLevelUp.class);
                eventManager.publish(new HypeTrainLevelUpEvent(lastTopicIdentifier, levelUpData));
                return true;
            case "hype-train-end":
                final HypeTrainEnd endData = TypeConvert.convertValue(msgData, HypeTrainEnd.class);
                eventManager.publish(new HypeTrainEndEvent(lastTopicIdentifier, endData));
                return true;
            case "hype-train-conductor-update":
                final HypeTrainConductor conductorData = TypeConvert.convertValue(msgData, HypeTrainConductor.class);
                eventManager.publish(new HypeTrainConductorUpdateEvent(lastTopicIdentifier, conductorData));
                return true;
            case "hype-train-cooldown-expiration":
                eventManager.publish(new HypeTrainCooldownExpirationEvent(lastTopicIdentifier));
                return true;
            case "last-x-experiment-event":
                // note: this isn't a true hype train event (it can be fired with no train active), but twitch hacked together the feature to use the hype pubsub infrastructure
                final SupportActivityFeedData lastData = TypeConvert.convertValue(msgData, SupportActivityFeedData.class);
                eventManager.publish(new SupportActivityFeedEvent(lastTopicIdentifier, lastData));
                return true;
            default:
                return false;
        }
    }
}
