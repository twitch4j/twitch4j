package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.common.annotation.Unofficial;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

@Data
@Setter(AccessLevel.PRIVATE)
@Unofficial // not present in official pubsub docs
public class BanSharingSettings {

    /**
     * Broadcaster User ID.
     */
    private String channelId;

    /**
     * Whether ban sharing is enabled.
     */
    @Accessors(fluent = true)
    @JsonProperty("is_enabled")
    private Boolean isEnabled;

    /**
     * The low trust treatment (e.g., restricted, monitored) to be applied
     * when a user is banned in a channel that shares their bans with this broadcaster.
     */
    private LowTrustUserTreatment sharedBansUserTreatment;

    /**
     * The permissions that dictate what types of channels can request this broadcaster's bans.
     */
    private BanSharingPermissions sharingPermissions;

}
