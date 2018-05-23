package twitch4j.pubsub.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import twitch4j.common.events.Event;
import twitch4j.pubsub.TwitchPubSub;

@Data
@ToString(exclude = {"pubSub"})
@EqualsAndHashCode(callSuper = true)
public abstract class PubSubEvent<E extends PubSubEvent<E>> extends Event<E> {
	private TwitchPubSub pubSub;
}
