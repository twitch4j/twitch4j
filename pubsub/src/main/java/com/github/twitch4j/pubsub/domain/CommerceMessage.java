package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CommerceMessage {

    /**
     * The message in this commerce event
     */
    private String message;

    /**
     * A list of emotes that were used in the message
     */
    private List<CommerceEmote> emotes;

    @Data
    public static class CommerceEmote {
        /**
         * The index in the message where the emote starts
         */
        private Integer start;

        /**
         * The number of characters in the emote name
         */
        private Integer end;

        /**
         * The id for the emote being used
         */
        @JsonProperty("id")
        private String emoteId;

        /**
         * @return the emote id as an integer, or null.
         * @deprecated in favor of {@link #getEmoteId()}
         */
        @JsonIgnore
        @Deprecated
        public Integer getId() {
            try {
                return Integer.parseInt(getEmoteId());
            } catch (Exception e) {
                return null;
            }
        }
    }

}
