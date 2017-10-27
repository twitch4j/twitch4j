package me.philippheuer.twitch4j.events.event.pubsub;

import me.philippheuer.twitch4j.message.pubsub.topics.PubSubTopics;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import me.philippheuer.twitch4j.events.Event;

@Value
@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MessageData extends Event {
    private String rawMessage;
    private PubSubTopics topic;
}
