package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.common.annotation.Unofficial;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
public class GuestStarGuest {

    /**
     * ID representing this guest’s slot assignment.
     * <p>
     * The host is always in slot "0".
     * Guests are assigned the following consecutive IDs (e.g, "1", "2", "3", etc).
     * <p>
     * Screen Share is represented as a special guest with the ID "SCREENSHARE".
     * <p>
     * The identifier here matches the ID referenced in browser source links used in broadcasting software.
     */
    private String slotId;

    /**
     * User ID of the guest assigned to this slot.
     */
    private String userId;

    /**
     * Login name of the guest assigned to this slot.
     */
    @Unofficial // not in example response
    private String userLogin;

    /**
     * Display name of the guest assigned to this slot.
     */
    @Unofficial // not in example response
    @JsonAlias("userName") // for consistency with other endpoints
    private String userDisplayName;

    /**
     * Whether the guest is visible in the browser source in the host’s streaming software.
     */
    @Accessors(fluent = true)
    @JsonProperty("is_live")
    private Boolean isLive;

    /**
     * Value from 0 to 100 representing the host’s volume setting for this guest.
     */
    private Integer volume;

    /**
     * Timestamp when this guest was assigned a slot in the session.
     */
    private Instant assignedAt;

    /**
     * Information about the guest’s audio settings.
     */
    @JsonAlias("audio") // docs and example have naming discrepancy
    private GuestStarMediaSettings audioSettings;

    /**
     * Information about the guest’s video settings.
     */
    @JsonAlias("video") // docs and example have naming discrepancy
    private GuestStarMediaSettings videoSettings;

}
