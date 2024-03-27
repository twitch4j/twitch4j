package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class GuestStarSessionList {

    /**
     * Summary of the session details.
     */
    private List<GuestStarSession> data;

    public GuestStarSession get() {
        return data == null || data.isEmpty() ? null : data.get(0);
    }

}
