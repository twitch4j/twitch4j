package com.github.twitch4j.helix.domain;

import lombok.*;

/**
 * Overwatch Metadata
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class OverwatchMetadata {

    /** The Broadcaster */
    @NonNull
    private OverwatchPlayer broadcaster;

    /**
     * Overwatch Player
     */
    @Data
    @Setter(AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class OverwatchPlayer {

        /** Hero Type */
        private OverwatchHero hero;

    }

    /**
     * Overwatch Hero
     */
    @Data
    @Setter(AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class OverwatchHero {

        /** Name of the Overwatch hero. */
        @NonNull
        private String name;

        /** Role of the Overwatch hero. */
        @NonNull
        private String role;

        /** Ability being used by the broadcaster. */
        @NonNull
        private String ability;

    }
}
