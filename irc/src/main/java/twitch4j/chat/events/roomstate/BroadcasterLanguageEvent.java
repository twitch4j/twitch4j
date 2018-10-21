package twitch4j.chat.events.roomstate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import twitch4j.chat.domain.Channel;

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
     * @param channel Channel
     * @param language Locale
     */
	public BroadcasterLanguageEvent(Channel channel, Locale language) {
		super(channel, language != null);
		this.language = language;
	}
}
