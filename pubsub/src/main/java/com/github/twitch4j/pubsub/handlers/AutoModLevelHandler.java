package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.AutomodLevelsModified;
import com.github.twitch4j.pubsub.events.AutomodLevelsModifiedEvent;

class AutoModLevelHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "automod-levels-modification";
    }

    @Override
    public boolean handle(Args args) {
        if (args.getTopicParts().length > 1 && "automod_levels_modified".equals(args.getType())) {
            AutomodLevelsModified data = TypeConvert.convertValue(args.getData(), AutomodLevelsModified.class);
            args.getEventManager().publish(new AutomodLevelsModifiedEvent(args.getLastTopicPart(), data));
            return true;
        }
        return false;
    }
}
