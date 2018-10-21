package twitch4j.chat.events.roomstate;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import twitch4j.chat.domain.Channel;
import twitch4j.chat.events.AbstractChannelEvent;

/**
 * Abstract Channel State Event
 */
@Data
@Getter
@EqualsAndHashCode(callSuper = false)
public abstract class ChannelStatesEvent extends AbstractChannelEvent {

	private final boolean active;

	public ChannelStatesEvent(Channel channel, boolean active) {
		super(channel);
		this.active = active;
	}
}
