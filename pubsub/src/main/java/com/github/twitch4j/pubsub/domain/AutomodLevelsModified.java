package com.github.twitch4j.pubsub.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Data
@Setter(AccessLevel.PRIVATE)
public class AutomodLevelsModified {
    private String createdBy;
    private String createdByUserId;
    private AutomodLevels previousAutomodLevels;
    private AutomodLevels updatedAutomodLevels;

    /**
     * Configuration of the Automod category sensitivity levels, between 0 and 4 (inclusive) each.
     */
    @Data
    @Setter(AccessLevel.PRIVATE)
    public static class AutomodLevels {
        @Nullable
        private Integer overallLevel;
        private Integer ableismLevel;
        private Integer aggressionLevel;
        private Integer homophobiaLevel;
        private Integer misogynyLevel;
        private Integer nameCallingLevel;
        private Integer profanityLevel;
        private Integer racismLevel;
        private Integer sexualityLevel;
    }
}
