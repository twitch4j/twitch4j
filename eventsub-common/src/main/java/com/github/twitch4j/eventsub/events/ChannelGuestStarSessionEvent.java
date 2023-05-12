package com.github.twitch4j.eventsub.events;

import com.github.twitch4j.common.annotation.Unofficial;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class ChannelGuestStarSessionEvent extends EventSubChannelEvent {

    /**
     * Unique identifier of the event.
     */
    @Unofficial
    private String id;

    /**
     * ID representing the unique session that was started.
     */
    private String sessionId;

    /**
     * RFC3339 timestamp indicating the time the session began.
     */
    private Instant startedAt;

}
