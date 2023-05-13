package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class GuestStarSession {

    /**
     * ID uniquely representing the Guest Star session.
     */
    private String id;

    /**
     * The guests currently interacting with the Guest Star session.
     */
    private List<GuestStarGuest> guests;

}
