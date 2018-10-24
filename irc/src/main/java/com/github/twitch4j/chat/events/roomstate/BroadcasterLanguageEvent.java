package com.github.twitch4j.chat.events.roomstate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import com.github.twitch4j.chat.domain.ChatChannel;

import java.util.Locale;

/**
 * Broadcaster Language Event
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class BroadcasterLanguageEvent extends ChannelStatesEvent {

    /**
     * Language
     */
	private final Locale language;

    /**
     * Constructor
     *
     * @param channel ChatChannel
     * @param language Locale
     */
	public BroadcasterLanguageEvent(ChatChannel channel, Locale language) {
		super(channel, language != null);
		this.language = language;
	}
}
