package com.github.twitch4j.chat.events.roomstate;

import com.github.twitch4j.common.events.domain.EventChannel;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.util.Locale;

/**
 * Broadcaster Language Event
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Deprecated
public class BroadcasterLanguageEvent extends ChannelStatesEvent {

    /**
     * Language
     */
    Locale language;

    /**
     * Constructor
     *
     * @param channel ChatChannel
     * @param language Locale
     */
	public BroadcasterLanguageEvent(EventChannel channel, Locale language) {
		super(channel, language != null);
		this.language = language;
	}
}
