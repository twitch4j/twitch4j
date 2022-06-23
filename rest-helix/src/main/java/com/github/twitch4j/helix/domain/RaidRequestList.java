package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class RaidRequestList {

    /**
     * A list of raids. The list will contain the single raid that this request created.
     */
    private List<RaidRequest> data;

}
