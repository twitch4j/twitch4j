package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
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
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Extension {

    /** ID of the extension. */
    private String id;

    /** Name of the extension. */
    private String name;

    /** Version of the extension. */
    private String version;

    /** Indicates whether the extension is configured such that it can be activated. */
    private String canActivate;

    /** Types for which the extension can be activated. Valid values: "component", "mobile", "panel", "overlay". */
    private List<String> type;

}
