package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

@Data
@Setter(AccessLevel.PRIVATE)
public class GuestStarMediaSettings {

    /**
     * Whether the guest has an appropriate audio/video device available to be transmitted to the session.
     */
    @Accessors(fluent = true)
    @JsonProperty("is_available")
    private Boolean isAvailable;

    /**
     * Whether the host is allowing the guest’s media to be seen or heard within the session.
     */
    @Accessors(fluent = true)
    @JsonProperty("is_host_enabled")
    private Boolean isHostEnabled;

    /**
     * whether the guest is allowing their media to be transmitted to the session.
     */
    @Accessors(fluent = true)
    @JsonProperty("is_guest_enabled")
    private Boolean isGuestEnabled;

}
