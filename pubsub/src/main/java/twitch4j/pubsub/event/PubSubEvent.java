package twitch4j.pubsub.event;

import com.github.philippheuer.events4j.domain.Event;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import twitch4j.pubsub.TwitchPubSub;

@Data
@ToString(exclude = {"pubSub"})
@EqualsAndHashCode(callSuper = true)
public abstract class PubSubEvent extends Event {
	private TwitchPubSub pubSub;
}
