package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class GuestStarInviteList {

    /**
     * The invite objects describing the invited user as well as their ready status.
     */
    @JsonProperty("data")
    private List<GuestStarInvite> invites;

}
