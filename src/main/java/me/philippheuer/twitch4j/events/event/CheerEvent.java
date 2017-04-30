package me.philippheuer.twitch4j.events.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.events.Event;
import me.philippheuer.twitch4j.model.Channel;
import me.philippheuer.twitch4j.model.Cheer;
import me.philippheuer.twitch4j.model.User;

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
public class CheerEvent extends AbstractChannelEvent {

	/**
	 * Event Target User
	 */
	private final User user;

	/**
	 * Cheer
	 */
	private final Cheer cheer;

	/**
	 * Event Constructor
	 *
	 * @param channel The channel that this event originates from.
	 * @param cheer   The cheer, containing all relevant information.
	 * @see Cheer
	 */
	public CheerEvent(Channel channel, Cheer cheer) {
		super(channel);
		this.user = cheer.getUser();
		this.cheer = cheer;
	}

}
