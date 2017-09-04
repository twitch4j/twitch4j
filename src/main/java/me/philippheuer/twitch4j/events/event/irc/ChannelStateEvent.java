package me.philippheuer.twitch4j.events.event.irc;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.events.event.AbstractChannelEvent;
import me.philippheuer.twitch4j.model.Channel;

/**
 * Event Handler
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class ChannelStateEvent extends AbstractChannelEvent {

	/**
	 * Sub Only Mode
	 */
	private Boolean subMode;

	/**
	 * Emote Only Mode
	 */
	private Boolean emoteOnlyMode;

	/**
	 * R9K Mode
	 */
	private Boolean r9kMode;

	/**
	 * Slow Mode
	 */
	private Integer slowMode = 0;

	/**
	 * Followers Only
	 */
	private Integer followersMode = -1;

	/**
	 * Event Constructor
	 *
	 * @param channel     The channel that this event originates from.
	 * @param subMode 	  The new channel state.
	 * @param emoteOnlyMode	Emote Only Mode.
	 * @param r9kMode		R9K Mode.
	 * @param slowMode		Slow Mode Delay
	 * @param followersMode Followers only
	 */
	public ChannelStateEvent(Channel channel, Boolean subMode, Boolean emoteOnlyMode, Boolean r9kMode, Integer slowMode, Integer followersMode) {
		super(channel);
		this.subMode = subMode;
		this.emoteOnlyMode = emoteOnlyMode;
		this.r9kMode = r9kMode;
		this.slowMode = slowMode;
		this.followersMode = followersMode;
	}
}
