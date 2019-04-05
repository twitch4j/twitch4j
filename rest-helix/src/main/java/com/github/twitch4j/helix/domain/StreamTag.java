package com.github.twitch4j.helix.domain;

import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

/**
 * Stream Tags (LiveStream)
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StreamTag {
    // Tag ID.
    @NonNull
    private UUID tagId;

    // Whether the tag is automatically set by Twitch (meaning that it cannot be set manually)
    private Boolean isAuto;
    
    // Map with key/value pairs for the localized name of tags. Key is the locale ("en-us", "da-dk", etc.)
    @NonNull
    private Map<String, String> localizationNames;

    // Map with key/value pairs for the localized description/purpose of tags. Key is the locale ("en-us", "da-dk", etc.)
    @NonNull
    private Map<String, String> localizationDescriptions;
}
