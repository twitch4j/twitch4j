package me.philippheuer.twitch4j.events.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.events.Event;
import me.philippheuer.twitch4j.model.Channel;

@Data
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class HostOffEvent extends Event {

	/**
	 * Event Channel
	 */
	private final Channel channel;

	/**
	 * Constructor
	 */
	public HostOffEvent(Channel channel) {
		this.channel = channel;
	}
}
