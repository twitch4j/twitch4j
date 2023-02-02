package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
public class ShieldModeStatus {

    /**
     * Whether Shield Mode is active.
     */
    @Accessors(fluent = true)
    @JsonProperty("is_active")
    private Boolean isActive;

    /**
     * An ID that identifies the moderator that last activated Shield Mode.
     * <p>
     * Is an empty string if Shield Mode hasn't been previously activated.
     */
    private String moderatorId;

    /**
     * The moderator’s display name.
     * <p>
     * Is an empty string if Shield Mode hasn't been previously activated.
     */
    private String moderatorName;

    /**
     * The moderator’s login name.
     * <p>
     * Is an empty string if Shield Mode hasn't been previously activated.
     */
    private String moderatorLogin;

    /**
     * The UTC timestamp of when Shield Mode was last activated.
     * <p>
     * Is an empty string if Shield Mode hasn't been previously activated.
     */
    private Instant lastActivatedAt;

}
