package com.github.twitch4j.eventsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class Reward {

    /**
     * The reward identifier.
     */
    private String id;

    /**
     * The reward name.
     */
    private String title;

    /**
     * The reward cost.
     */
    private Integer cost;

    /**
     * The reward description.
     */
    private String prompt;

    @Data
    @Setter(AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class Image {
        @JsonProperty("url_1x")
        private String url1x;
        @JsonProperty("url_2x")
        private String url2x;
        @JsonProperty("url_4x")
        private String url4x;
    }

}
