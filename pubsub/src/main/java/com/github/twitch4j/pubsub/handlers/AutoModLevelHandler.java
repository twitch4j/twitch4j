package com.github.twitch4j.pubsub.handlers;

import com.github.philippheuer.events4j.api.IEventManager;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.PubSubResponsePayloadMessage;
import com.github.twitch4j.pubsub.domain.AutomodLevelsModified;
import com.github.twitch4j.pubsub.events.AutomodLevelsModifiedEvent;

import java.util.Collection;

class AutoModLevelHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "automod-levels-modification";
    }

    @Override
    public boolean handle(IEventManager eventManager, String[] topicParts, PubSubResponsePayloadMessage message, Collection<String> botOwnerIds) {
        if (topicParts.length > 1 && "automod_levels_modified".equals(message.getType())) {
            AutomodLevelsModified data = TypeConvert.convertValue(message.getMessageData(), AutomodLevelsModified.class);
            eventManager.publish(new AutomodLevelsModifiedEvent(topicParts[topicParts.length - 1], data));
            return true;
        }
        return false;
    }
}
