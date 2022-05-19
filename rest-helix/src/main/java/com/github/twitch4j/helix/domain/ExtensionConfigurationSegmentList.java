package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class ExtensionConfigurationSegmentList {

    /**
     * The segment objects.
     */
    private List<ExtensionConfigurationSegment> data;

}
