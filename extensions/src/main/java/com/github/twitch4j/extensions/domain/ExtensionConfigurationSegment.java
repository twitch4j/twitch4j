package com.github.twitch4j.extensions.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.With;

@Data
@Setter(AccessLevel.PRIVATE)
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExtensionConfigurationSegment {

    @NonNull
    String channelId;

    @NonNull
    ConfigurationSegmentType segment;

    String content;

    String version;

}
