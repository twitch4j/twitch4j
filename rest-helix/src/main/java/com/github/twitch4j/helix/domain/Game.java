package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.regex.Pattern;

/**
 * Game
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Game {

    /** Game ID. */
    private String id;

    /** Game name. */
    private String name;

    /** Template URL for the game’s box art. */
    private String boxArtUrl;
    
    /**
     * Gets the game's box art url for specific dimensions
     *
     * @param width  game's box art width
     * @param height game's box art height
     * @return String
     */
    public String getBoxArtUrl(Integer width, Integer height) {
        return boxArtUrl.replaceAll(Pattern.quote("{width}"), width.toString()).replaceAll(Pattern.quote("{height}"), height.toString());
    }

}
