package me.philippheuer.twitch4j.events.event.irc;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.events.Event;
import me.philippheuer.twitch4j.message.irc.IRCParser;

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
@Deprecated
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

	/**
	 * Check if the irc message contains a tag
	 *
	 * @param name tag name
	 * @return If the tag is existing
	 */
	public boolean hasTag(String name) {
		return getIrcParser().getTags() != null && getIrcParser().getTags().hasTag(name);
	}

	/**
	 * Get the tag value
	 *
	 * @param name tag name
	 * @param <T> class that extends Object
	 * @return getting tag data returned classes extends Object
	 */
	@SuppressWarnings("unchecked")
	public <T extends Object> T getTag(String name) {
		return (T) getIrcParser().getTags().getTag(name);
	}
}
