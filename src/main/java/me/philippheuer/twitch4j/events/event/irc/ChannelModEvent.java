package me.philippheuer.twitch4j.events.event.irc;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import me.philippheuer.twitch4j.events.event.AbstractChannelEvent;
import me.philippheuer.twitch4j.model.Channel;
import me.philippheuer.twitch4j.model.User;

/**
 * This event gets called when a client gains/loses mod status.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class ChannelModEvent extends AbstractChannelEvent {

	/**
	 * User
	 */
	private User user;

	/**
	 * Is Moderator?
	 */
	private boolean isMod;

	/**
	 * Event Constructor
	 *
	 * @param channel     The channel that this event originates from.
	 * @param user 		  The user that gained/lost mod status.
	 * @param isMod		  Did the use gain or lose mod status?
	 */
	public ChannelModEvent(Channel channel, User user, boolean isMod) {
		super(channel);
		this.user = user;
		this.isMod = isMod;
	}
}
