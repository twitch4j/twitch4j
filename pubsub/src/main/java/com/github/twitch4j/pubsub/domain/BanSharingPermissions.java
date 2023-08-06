package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Determines whether various types of users can request the broadcaster to share their bans.
 */
@Data
@Accessors(fluent = true)
@Setter(AccessLevel.PRIVATE)
public class BanSharingPermissions {

    @JsonProperty("ALL")
    private Boolean canEveryoneRequestAccess;

    @JsonProperty("AFFILIATES")
    private Boolean canAffiliatesRequestAccess;

    @JsonProperty("PARTNERS")
    private Boolean canPartnersRequestAccess;

    @JsonProperty("MUTUAL_FOLLOWERS")
    private Boolean canMutualFollowersRequestAccess;

}
