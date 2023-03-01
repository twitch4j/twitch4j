package com.github.twitch4j.helix.domain;

import lombok.*;

import java.util.Map;
import java.util.UUID;

/**
 * Stream Tags (LiveStream)
 *
 * @deprecated <a href="https://discuss.dev.twitch.tv/t/adding-customizable-tags-to-the-twitch-api/42921">Deprecation Information</a>
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@Deprecated
public class StreamTag {
    /**
     * Tag ID.
     *
     * @see ChannelInformation#getTags()
     * @deprecated Twitch has deprecated UUID-based tags with the latest custom tags system.
     */
    @NonNull
    @Deprecated
    private UUID tagId;

    /** Whether the tag is automatically set by Twitch (meaning that it cannot be set manually) */
    private Boolean isAuto;

    /** Map with key/value pairs for the localized name of tags. Key is the locale ("en-us", "da-dk", etc.) */
    @NonNull
    private Map<String, String> localizationNames;

    /** Map with key/value pairs for the localized description/purpose of tags. Key is the locale ("en-us", "da-dk", etc.) */
    @NonNull
    private Map<String, String> localizationDescriptions;
}
