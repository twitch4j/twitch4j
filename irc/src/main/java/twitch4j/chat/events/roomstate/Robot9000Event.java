package twitch4j.chat.events.roomstate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import twitch4j.chat.domain.Channel;

/**
 * R9K State Event
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class Robot9000Event extends ChannelStatesEvent{

    /**
     * Constructor
     *
     * @param channel Channel
     * @param active State active?
     */
	public Robot9000Event(Channel channel, boolean active) {
		super(channel, active);
	}
}
