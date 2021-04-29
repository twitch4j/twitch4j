package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Extension
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Extension {

    /** ID of the extension. */
    private String id;

    /** Name of the extension. */
    private String name;

    /** Version of the extension. */
    private String version;

    /** Indicates whether the extension is configured such that it can be activated. */
    private Boolean canActivate;

    /** Types for which the extension can be activated. Valid values: "component", "mobile", "panel", "overlay". */
    private List<String> type;

}
