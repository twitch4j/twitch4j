package twitch4j.chat.events.roomstate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import twitch4j.chat.domain.Channel;

/**
 * Emote Only State Event
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class EmoteOnlyEvent extends ChannelStatesEvent{

    /**
     * Constructor
     *
     * @param channel Channel
     * @param active State active?
     */
	public EmoteOnlyEvent(Channel channel, boolean active) {
		super(channel, active);
	}
}
