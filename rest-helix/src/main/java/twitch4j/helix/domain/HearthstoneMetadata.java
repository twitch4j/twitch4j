package twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

/**
 * Heartstone Metadata
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HearthstoneMetadata {

    // The Broadcaster
    @NonNull
    private HearthstonePlayer broadcaster;

    // The Opponent
    @NonNull
    private HearthstonePlayer opponent;

    /**
     * Heartstone Player
     */
    @Data
    @Setter(AccessLevel.PRIVATE)
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class HearthstonePlayer {

        // Hero Type
        private HearthstoneHero hero;

    }

    /**
     * Heartstone Hero
     */
    @Data
    @Setter(AccessLevel.PRIVATE)
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class HearthstoneHero {

        // Name of the Hearthstone hero.
        @NonNull
        private String name;

        // Class of the Hearthstone hero.
        @NonNull
        @JsonProperty("class")
        private String className;

        // Type of Hearthstone hero.
        @NonNull
        private String type;

    }
}
