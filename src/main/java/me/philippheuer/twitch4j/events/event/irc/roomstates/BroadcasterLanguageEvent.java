package me.philippheuer.twitch4j.events.event.irc.roomstates;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import me.philippheuer.twitch4j.model.Channel;

import java.util.Locale;

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
public class BroadcasterLanguageEvent extends ChannelStatesEvent{

	private final Locale language;

	public BroadcasterLanguageEvent(Channel channel, Locale language) {
		super(channel, language != null);
		this.language = language;
	}
}
