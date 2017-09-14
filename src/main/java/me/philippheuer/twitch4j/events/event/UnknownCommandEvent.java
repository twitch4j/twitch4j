package me.philippheuer.twitch4j.events.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.philippheuer.twitch4j.events.event.irc.ChannelMessageEvent;
import me.philippheuer.twitch4j.model.Channel;
import me.philippheuer.twitch4j.model.User;

/**
 * This event gets called when a command doesn't exists. It can be used to query alternative sources for commands.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
@Getter
@EqualsAndHashCode(callSuper = false)
public class UnknownCommandEvent extends AbstractChannelEvent {

	/**
	 * User
	 */
	private final User user;

	/**
	 * Message
	 */
	private final ChannelMessageEvent messageEvent;

	/**
	 * Event Constructor
	 *
	 * @param channel The channel that this event originates from.
	 * @param user	The user that invoked this event.
	 * @param messageEvent The originating event.
	 */
	public UnknownCommandEvent(Channel channel, User user, ChannelMessageEvent messageEvent) {
		super(channel);
		this.user = user;
		this.messageEvent = messageEvent;
	}
}
