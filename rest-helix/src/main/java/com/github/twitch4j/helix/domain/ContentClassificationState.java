package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.eventsub.domain.ContentClassification;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;
import org.jetbrains.annotations.NotNull;

@Data
@With
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ContentClassificationState {

    /**
     * ID of the Content Classification Labels that must be added/removed from the channel.
     */
    @NotNull
    private ContentClassification id;

    /**
     * Whether the label should be enabled (true) or disabled for the channel.
     */
    @JsonProperty("is_enabled")
    private boolean isEnabled;

}
