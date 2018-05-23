package twitch4j.pubsub.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import twitch4j.pubsub.PubSubTopic;

@Data
@EqualsAndHashCode(callSuper = true)
public class TopicListen extends PubSubEvent {
	private PubSubTopic topic;
}
