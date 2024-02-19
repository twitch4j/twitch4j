package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.BroadcastSettings;
import com.github.twitch4j.pubsub.events.BroadcastSettingsUpdateEvent;

class BroadcastSettingsHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "broadcast-settings-update";
    }

    @Override
    public TwitchEvent apply(Args args) {
        if ("broadcast_settings_update".equals(args.getType())) {
            BroadcastSettings data = TypeConvert.jsonToObject(args.getRawMessage(), BroadcastSettings.class);
            return new BroadcastSettingsUpdateEvent(data);
        }
        return null;
    }
}
