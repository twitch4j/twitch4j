package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.twitch4j.eventsub.domain.ContentClassification;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class ContentClassificationInfo {

    /**
     * Unique identifier for the CCL.
     */
    private String id;

    /**
     * Localized description of the CCL.
     */
    private String description;

    /**
     * Localized name of the CCL.
     */
    private String name;

    /**
     * @return {@link #getId()} parsed as {@link ContentClassification}.
     */
    @JsonIgnore
    public ContentClassification getLabel() {
        return ContentClassification.parse(id);
    }

}
