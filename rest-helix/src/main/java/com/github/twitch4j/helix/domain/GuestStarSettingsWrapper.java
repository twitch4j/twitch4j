package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class GuestStarSettingsWrapper {

    private List<GuestStarSettings> data;

    public GuestStarSettings get() {
        return data == null || data.isEmpty() ? null : data.get(0);
    }

}
