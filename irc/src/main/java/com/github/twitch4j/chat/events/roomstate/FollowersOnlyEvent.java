package com.github.twitch4j.chat.events.roomstate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import com.github.twitch4j.chat.domain.ChatChannel;

import java.util.concurrent.TimeUnit;

/**
 * Followers Only State Event
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class FollowersOnlyEvent extends ChannelStatesEvent{

	private final long time;

	public FollowersOnlyEvent(ChatChannel channel, long time, TimeUnit timeUnit) {
		super(channel, time > -1);
		this.time = timeUnit.toSeconds(time);
	}

	public FollowersOnlyEvent(ChatChannel channel, long time) {
		super(channel, time > -1);
		this.time = TimeUnit.MINUTES.toSeconds(time);
	}

	public long getTime(TimeUnit timeUnit) {
		return timeUnit.convert(time, TimeUnit.SECONDS);
	}
}
