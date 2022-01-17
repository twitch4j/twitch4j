package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.common.events.domain.EventUser;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * Called when a ritual takes place in chat.
 * <p>
 * Many channels have special rituals to celebrate viewer milestones when they are shared.
 * The rituals notice extends the sharing of these messages to other viewer milestones  (initially, a new viewer chatting for the first time).
 *
 * @see <a href="https://twitter.com/TwitchSupport/status/1481008324502073347">Shut down announcement</a>
 * @deprecated no longer sent by twitch.
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Deprecated
public class RitualEvent extends AbstractChannelEvent {
    /**
     * The user involved in the ritual.
     */
    EventUser user;

    /**
     * The name of the ritual this notice is for.
     */
    String ritualName;

    /**
     * Event Constructor
     *
     * @param channel    The channel that this event originates from.
     * @param user       The user involved in the ritual.
     * @param ritualName The name of the ritual this notice is for.
     */
    public RitualEvent(EventChannel channel, EventUser user, String ritualName) {
        super(channel);
        this.user = user;
        this.ritualName = ritualName;
    }

    /**
     * @return whether the ritual corresponds to a new viewer chatting for the first time
     */
    public boolean isNewChatter() {
        return "new_chatter".equals(this.ritualName);
    }
}
