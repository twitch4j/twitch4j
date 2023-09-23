package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.AutomodLevelsModified;
import com.github.twitch4j.pubsub.events.AutomodLevelsModifiedEvent;

class AutoModLevelHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "automod-levels-modification";
    }

    @Override
    public TwitchEvent apply(Args args) {
        if (args.getTopicParts().length > 1 && "automod_levels_modified".equals(args.getType())) {
            AutomodLevelsModified data = TypeConvert.convertValue(args.getData(), AutomodLevelsModified.class);
            return new AutomodLevelsModifiedEvent(args.getLastTopicPart(), data);
        }
        return null;
    }
}
