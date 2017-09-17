package me.philippheuer.twitch4j.events.event.irc.roomstates;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import me.philippheuer.twitch4j.model.Channel;

import java.util.concurrent.TimeUnit;

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
public class FollowersOnlyEvent extends ChannelStatesEvent{

	private final long time;

	public FollowersOnlyEvent(Channel channel, long time, TimeUnit timeUnit) {
		super(channel, time > -1);
		this.time = timeUnit.toSeconds(time);
	}

	public FollowersOnlyEvent(Channel channel, long time) {
		super(channel, time > -1);
		this.time = TimeUnit.MINUTES.toSeconds(time);
	}

	public long getTime(TimeUnit timeUnit) {
		return timeUnit.convert(time, TimeUnit.SECONDS);
	}
}
