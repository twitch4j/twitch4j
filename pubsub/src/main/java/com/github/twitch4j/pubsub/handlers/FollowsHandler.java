package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.OutboundFollow;
import com.github.twitch4j.pubsub.domain.OutboundUnfollow;
import com.github.twitch4j.pubsub.events.OutboundFollowPubSubEvent;
import com.github.twitch4j.pubsub.events.OutboundUnfollowPubSubEvent;

class FollowsHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "follows";
    }

    @Override
    public TwitchEvent apply(Args args) {
        String[] topicParts = args.getTopicParts();
        if (topicParts.length <= 1) return null;
        String selfId = topicParts[1];
        switch (args.getType()) {
            case "user-followed":
                OutboundFollow follow = TypeConvert.jsonToObject(args.getRawMessage(), OutboundFollow.class);
                return new OutboundFollowPubSubEvent(selfId, follow);

            case "user-unfollowed":
                OutboundUnfollow unfollow = TypeConvert.jsonToObject(args.getRawMessage(), OutboundUnfollow.class);
                return new OutboundUnfollowPubSubEvent(selfId, unfollow);

            default:
                return null;
        }
    }
}
