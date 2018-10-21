package twitch4j.chat.events.roomstate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import twitch4j.chat.domain.Channel;

/**
 * Slow Mode State Event
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class SlowModeEvent extends ChannelStatesEvent {
	/**
	 * time in seconds
	 */
	private final long time;

    /**
     * Constructor
     *
     * @param channel Channel
     * @param time seconds
     */
	public SlowModeEvent(Channel channel, long time) {
		super(channel, time > 0);
		this.time = time;
	}
}
