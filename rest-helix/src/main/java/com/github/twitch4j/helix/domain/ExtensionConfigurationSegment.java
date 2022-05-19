package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Data
@Setter(AccessLevel.PRIVATE)
public class ExtensionConfigurationSegment {

    /**
     * The type of segment.
     */
    private ExtensionSegment segment;

    /**
     * The contents of the segment.
     * <p>
     * This string may be a plain string or a string-encoded JSON object.
     */
    private String content;

    /**
     * The version that identifies the segment’s definition.
     */
    private String version;

    /**
     * The ID of the broadcaster that owns the extension.
     * The object includes this field only if the segment query parameter is set to developer or broadcaster.
     */
    @Nullable
    private String broadcasterId;

}
