package me.philippheuer.twitch4j.events.event.irc.roomstates;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import me.philippheuer.twitch4j.model.Channel;

/**
 * R9K mode event.
 *
 * @author Damian Staszewski [https://github.com/stachu540]
 * @version %I%, %G%
 * @since 1.0
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class SlowModeEvent extends ChannelStatesEvent{
	/**
	 * time in seconds
	 */
	private final long time;

	public SlowModeEvent(Channel channel, long time) {
		super(channel, time > 0);
		this.time = time;
	}
}
