package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class VideoPlaybackData {
    /**
     * The type of the video playback event.
     */
    private Type type;

    /**
     * Always sent. May have a fractional part.
     */
    private String serverTime;

    /**
     * Sent when {@link #getType()} is {@link Type#STREAM_UP}.
     */
    private Integer playDelay;

    /**
     * Sent when {@link #getType()} is {@link Type#VIEW_COUNT}.
     */
    private Integer viewers;

    /**
     * Sent when {@link #getType()} is {@link Type#COMMERCIAL}.
     */
    private Integer length;

    /**
     * Sent when {@link #getType()} is {@link Type#COMMERCIAL}.
     */
    private Boolean scheduled;

    @RequiredArgsConstructor
    public enum Type {
        COMMERCIAL("commercial"),
        STREAM_DOWN("stream-down"),
        STREAM_UP("stream-up"),
        VIEW_COUNT("viewcount"),
        TOS_STRIKE("tos-strike");

        private final String type;

        @Override
        public String toString() {
            return this.type;
        }

        @JsonCreator
        public static Type fromString(String type) {
            for (Type t : Type.values()) {
                if (t.type.equalsIgnoreCase(type))
                    return t;
            }

            return null;
        }
    }
}
