package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class ShieldModeStatusWrapper {

    /**
     * A list that contains a single object with the broadcaster’s Shield Mode status.
     */
    private List<ShieldModeStatus> data;

    /**
     * @return a single object with the broadcaster’s Shield Mode status.
     */
    public ShieldModeStatus get() {
        return data.get(0);
    }

}
