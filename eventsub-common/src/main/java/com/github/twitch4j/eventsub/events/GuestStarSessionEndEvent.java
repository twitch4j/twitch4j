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
public class GuestStarSessionEndEvent extends ChannelGuestStarSessionEvent {

    /**
     * Unique identifier of the event.
     */
    @Unofficial
    private String id;

    /**
     * RFC3339 timestamp indicating the time the session began.
     */
    private Instant startedAt;

    /**
     * RFC3339 timestamp indicating the time the session ended.
     */
    private Instant endedAt;

}
