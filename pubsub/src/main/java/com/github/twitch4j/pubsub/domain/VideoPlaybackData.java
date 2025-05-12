package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.twitch4j.common.enums.TwitchEnum;
import com.github.twitch4j.common.util.TwitchEnumDeserializer;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Data
@Setter(onMethod_ = { @Deprecated })
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
    @Nullable
    private Integer playDelay;

    /**
     * Sent when {@link #getType()} is {@link Type#VIEW_COUNT}.
     */
    @Nullable
    private Integer viewers;

    /**
     * Sent when {@link #getType()} is {@link Type#VIEW_COUNT}.
     * <p>
     * Only positive if the channel is in a Stream Together session.
     */
    @Nullable
    private Integer collaborationViewers;

    /**
     * Sent when {@link #getType()} is {@link Type#VIEW_COUNT}.
     */
    @JsonDeserialize(using = TwitchEnumDeserializer.class)
    private TwitchEnum<CollaborationStatus> collaborationStatus;

    /**
     * Sent when {@link #getType()} is {@link Type#COMMERCIAL}.
     */
    @Nullable
    private Integer length;

    /**
     * Sent when {@link #getType()} is {@link Type#COMMERCIAL}.
     */
    @Nullable
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

    public enum CollaborationStatus {
        /**
         * Channel is not in a Stream Together session.
         */
        NONE,

        /**
         * Channel is engaged in a Stream Together session.
         */
        IN_COLLABORATION,

        /**
         * Unknown collaboration status; please report to our issue tracker!
         */
        @JsonEnumDefaultValue
        UNKNOWN
    }
}
