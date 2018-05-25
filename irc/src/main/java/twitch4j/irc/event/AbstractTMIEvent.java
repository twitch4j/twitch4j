package twitch4j.irc.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import twitch4j.common.events.Event;
import twitch4j.irc.TwitchMessageInterface;

@Data
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractTMIEvent extends Event {
	private TwitchMessageInterface messageInterface;
}
