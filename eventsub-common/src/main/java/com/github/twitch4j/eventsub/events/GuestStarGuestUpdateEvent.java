package com.github.twitch4j.eventsub.events;

import com.github.twitch4j.eventsub.domain.GuestStarState;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GuestStarGuestUpdateEvent extends ChannelGuestStarSlotEvent {

    /**
     * The current state of the user after the update has taken place.
     */
    private GuestStarState state;

}
