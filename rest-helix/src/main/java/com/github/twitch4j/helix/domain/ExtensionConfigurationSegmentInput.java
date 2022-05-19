package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;
import lombok.extern.jackson.Jacksonized;
import org.jetbrains.annotations.Nullable;

@With
@Data
@Setter(AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExtensionConfigurationSegmentInput {

    /**
     * Required: ID for the Extension which the configuration is for.
     */
    private String extensionId;

    /**
     * Required: Configuration type.
     */
    private ExtensionSegment segment;

    /**
     * User ID of the broadcaster.
     * Required if the segment type is "developer" or "broadcaster".
     */
    @Nullable
    private String broadcasterId;

    /**
     * Configuration in a string-encoded format.
     */
    @Nullable
    private String content;

    /**
     * Configuration version with the segment type.
     */
    @Nullable
    private String version;

}
