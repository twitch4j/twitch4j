package twitch4j.irc.event;

import com.github.philippheuer.events4j.domain.Event;
import lombok.Data;
import lombok.EqualsAndHashCode;
import twitch4j.irc.TwitchMessageInterface;

@Data
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractTMIEvent extends Event {
	private TwitchMessageInterface messageInterface;
}
