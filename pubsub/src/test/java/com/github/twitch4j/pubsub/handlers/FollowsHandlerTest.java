package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.OutboundFollow;
import com.github.twitch4j.pubsub.domain.OutboundUnfollow;
import com.github.twitch4j.pubsub.domain.PubSubResponse;
import com.github.twitch4j.pubsub.events.OutboundFollowPubSubEvent;
import com.github.twitch4j.pubsub.events.OutboundUnfollowPubSubEvent;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unittest")
class FollowsHandlerTest {

    @Test
    void follow() {
        TwitchEvent event = parse("{\"type\":\"MESSAGE\",\"data\":{\"topic\":\"follows.149223493\",\"message\":\"{\\\"type\\\":\\\"user-followed\\\",\\\"timestamp\\\":\\\"2024-05-11T13:37:05.678901234Z\\\",\\\"target_display_name\\\":\\\"OGprodigy\\\",\\\"target_username\\\":\\\"ogprodigy\\\",\\\"target_user_id\\\":\\\"53888434\\\"}\"}}");
        OutboundFollow data = new OutboundFollow("53888434", "ogprodigy", "OGprodigy", Instant.parse("2024-05-11T13:37:05.678901234Z"));
        assertEquals(event, new OutboundFollowPubSubEvent("149223493", data));
    }

    @Test
    void unfollow() {
        TwitchEvent event = parse("{\"type\":\"MESSAGE\",\"data\":{\"topic\":\"follows.149223493\",\"message\":\"{\\\"type\\\":\\\"user-unfollowed\\\",\\\"timestamp\\\":\\\"2024-05-12T13:37:05.678901234Z\\\",\\\"target_user_id\\\":\\\"53888434\\\"}\"}}");
        OutboundUnfollow data = new OutboundUnfollow("53888434", Instant.parse("2024-05-12T13:37:05.678901234Z"));
        assertEquals(event, new OutboundUnfollowPubSubEvent("149223493", data));
    }

    private TwitchEvent parse(String json) {
        PubSubResponse message = TypeConvert.jsonToObject(json, PubSubResponse.class);
        String[] topicParts = StringUtils.split(message.getData().getTopic(), '.');
        TopicHandler handler = HandlerRegistry.INSTANCE.getHandlers().get(topicParts[0]);
        TopicHandler.Args args = new TopicHandler.Args(topicParts, message.getData().getMessage(), Collections.emptySet());
        return handler.apply(args);
    }

}
