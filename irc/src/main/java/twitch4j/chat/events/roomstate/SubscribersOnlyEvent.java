package twitch4j.chat.events.roomstate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import twitch4j.chat.domain.Channel;

/**
 * Subscribers Only State Event
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class SubscribersOnlyEvent extends ChannelStatesEvent{

    /**
     * Constructor
     *
     * @param channel Channel
     * @param active State active?
     */
	public SubscribersOnlyEvent(Channel channel, boolean active) {
		super(channel, active);
	}
}
