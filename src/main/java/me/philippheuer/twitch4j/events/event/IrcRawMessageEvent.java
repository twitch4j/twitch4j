package me.philippheuer.twitch4j.events.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.events.Event;
import me.philippheuer.twitch4j.message.irc.IRCParser;
import me.philippheuer.twitch4j.model.Channel;
import me.philippheuer.twitch4j.model.User;

import java.util.Currency;

/**
 * This event gets called when twitch4j recieves a raw irc message.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class IrcRawMessageEvent extends Event {

	/**
	 * User
	 */
	private final IRCParser ircParser;

	/**
	 * Event Constructor
	 *
	 * @param ircParser the IrcParser Instance
	 */
	public IrcRawMessageEvent(IRCParser ircParser) {
		super();
		this.ircParser = ircParser;
	}
}
