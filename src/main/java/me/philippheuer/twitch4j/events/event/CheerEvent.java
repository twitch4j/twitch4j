package me.philippheuer.twitch4j.events.event;

import lombok.*;
import me.philippheuer.twitch4j.events.Event;
import me.philippheuer.twitch4j.model.*;

/**
 * This event gets called when a user receives bits.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class CheerEvent extends Event {

	/**
	 * Event Channel
	 */
	private final Channel channel;

	/**
	 * Event Target User
	 */
	private final User user;

	/**
	 * Cheer
	 */
	private final Cheer cheer;

	/**
	 * Constructor
	 *
	 * @param cheer
	 */
	public CheerEvent(Channel channel, Cheer cheer) {
		this.channel = channel;
		this.user = cheer.getUser();
		this.cheer = cheer;
	}

}
