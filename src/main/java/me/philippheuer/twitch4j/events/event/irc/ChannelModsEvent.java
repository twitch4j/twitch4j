package me.philippheuer.twitch4j.events.event.irc;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.events.event.AbstractChannelEvent;
import me.philippheuer.twitch4j.model.Channel;

import java.util.List;

/**
 * This event gets called when twitch sends a list of moderators.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class ChannelModsEvent extends AbstractChannelEvent {

	/**
	 * Mods
	 */
	private List<String> mods;

	/**
	 * Event Constructor
	 *
	 * @param channel     The channel that this event originates from.
	 * @param mods 		  The list of currently active moderators.
	 */
	public ChannelModsEvent(Channel channel, List<String> mods) {
		super(channel);
		this.mods = mods;
	}
}
