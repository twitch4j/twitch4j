package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class CustomPowerupList {

    /**
     * A list of custom Power-ups.
     * <p>
     * The list is in ascending order by id.
     * If the broadcaster hasn’t created custom Power-ups, the list is empty.
     */
    private List<CustomPowerup> data;

}
