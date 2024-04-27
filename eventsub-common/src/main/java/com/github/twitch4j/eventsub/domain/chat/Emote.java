package com.github.twitch4j.eventsub.domain.chat;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

@Data
@Setter(AccessLevel.PRIVATE)
public class Emote {

    /**
     * An ID that uniquely identifies this emote.
     */
    private String id;

    /**
     * An ID that identifies the emote set that the emote belongs to.
     */
    private String emoteSetId;

    /**
     * The ID of the broadcaster who owns the emote.
     */
    @Nullable
    private String ownerId;

    /**
     * The formats that the emote is available in.
     * <p>
     * For example, if the emote is available only as a static PNG, the array contains only static.
     * But if the emote is available as a static PNG and an animated GIF, the array contains static and animated.
     */
    @Nullable
    private EnumSet<Format> format;

    public enum Format {
        /**
         * An animated GIF is available for this emote.
         */
        ANIMATED,

        /**
         * A static PNG file is available for this emote.
         */
        STATIC
    }

}
