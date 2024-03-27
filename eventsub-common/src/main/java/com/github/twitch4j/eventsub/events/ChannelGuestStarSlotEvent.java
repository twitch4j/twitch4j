package com.github.twitch4j.eventsub.events;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class ChannelGuestStarSlotEvent extends EventSubModerationEvent {

    /**
     * The ID of the slot assignment the guest is assigned to, or null if the guest is <i>not</i> in the
     * {@link com.github.twitch4j.eventsub.domain.GuestStarState#LIVE} or
     * {@link com.github.twitch4j.eventsub.domain.GuestStarState#BACKSTAGE} state.
     *
     * @see GuestStarGuestUpdateEvent#getState()
     */
    @Nullable
    private String slotId;

    /**
     * The user ID of the guest who is assigned to the slot, or null if the slot has no guest.
     */
    @Nullable
    private String guestUserId;

    /**
     * The guest display name, or null if the slot has no guest.
     */
    @Nullable
    private String guestUserName;

    /**
     * The guest login, or null if the slot has no guest.
     */
    @Nullable
    private String guestUserLogin;

}
