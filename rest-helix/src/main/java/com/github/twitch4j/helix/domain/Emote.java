package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.common.enums.SubscriptionPlan;
import com.github.twitch4j.helix.TwitchHelix;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Setter(AccessLevel.PRIVATE)
public class Emote {

    /**
     * ID of the emote.
     */
    private String id;

    /**
     * Name of the emote a viewer types into Twitch chat for the image to appear.
     */
    private String name;

    /**
     * The image URLs for the emote.
     * <p>
     * This is <i>not</i> present for {@link TwitchHelix#getUserEmotes(String, String, String, String)};
     * use {@link EmoteList#getPopulatedTemplateUrl(String, Format, Theme, Scale)}.
     */
    @Nullable
    private Images images;

    /**
     * ID of the emote set the emote belongs to.
     * <p>
     * This is <i>not</i> present for {@link TwitchHelix#getGlobalEmotes(String)}.
     * This <i>is</i> present for {@link TwitchHelix#getChannelEmotes(String, String)},
     * {@link TwitchHelix#getEmoteSets(String, Collection)},
     * and {@link TwitchHelix#getUserEmotes(String, String, String, String)}.
     */
    @Nullable
    private String emoteSetId;

    /**
     * The formats that the emote is available in.
     * <p>
     * For example, if the emote is available only as a static PNG, the collection contains only {@link Format#STATIC}.
     * But if it’s available as a static PNG and an animated GIF, the collection contains both {@link Format#STATIC} and {@link Format#ANIMATED}.
     */
    private List<Format> format;

    /**
     * The sizes that the emote is available in.
     */
    private List<Scale> scale;

    /**
     * The background themes that the emote is available in.
     */
    private List<Theme> themeMode;

    /**
     * User ID of the broadcaster who owns the emote.
     * <p>
     * This is <i>only</i> present for {@link TwitchHelix#getEmoteSets(String, Collection)}
     * and {@link TwitchHelix#getUserEmotes(String, String, String, String)}.
     */
    @Nullable
    private String ownerId;

    /**
     * The type of emote.
     * <p>
     * This is <i>only</i> present for {@link TwitchHelix#getChannelEmotes(String, String)}
     * {@link TwitchHelix#getEmoteSets(String, Collection)},
     * and {@link TwitchHelix#getUserEmotes(String, String, String, String)}.
     */
    @Nullable
    private String emoteType;

    /**
     * The subscriber tier at which the emote is unlocked.
     * <p>
     * This is only present when {@link #getEmoteType()} equals {@link Type#SUBSCRIPTIONS}.
     * Also, this can only be present when using {@link TwitchHelix#getChannelEmotes(String, String)}.
     */
    @Nullable
    private SubscriptionPlan tier;

    /**
     * Attempts to parse this emoteType to a known {@link Type}.
     *
     * @return the parsed {@link Type}.
     * @see #getEmoteType()
     */
    @NonNull
    public Type getParsedEmoteType() {
        return Type.parseEmoteType(emoteType != null ? emoteType : "");
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    public static class Images {

        /**
         * Small emote image URL.
         */
        @JsonProperty("url_1x")
        private String smallImageUrl;

        /**
         * Medium emote image URL.
         */
        @JsonProperty("url_2x")
        private String mediumImageUrl;

        /**
         * Large emote image URL.
         */
        @JsonProperty("url_4x")
        private String largeImageUrl;

    }

    public enum Type {

        /**
         * Indicates a library-archived emote.
         */
        @Unofficial
        ARCHIVE,

        /**
         * Indicates a custom Bits tier emote.
         */
        BITS_TIER,

        /**
         * Indicates a channel points reward emote.
         */
        CHANNEL_POINTS,

        /**
         * Indicates a custom follower emote.
         * Note: subscribers can utilize follower emotes in any channel.
         */
        FOLLOWER,

        /**
         * Indicates a global emote.
         */
        GLOBALS,

        /**
         * Indicates a hype train emote.
         */
        HYPE_TRAIN,

        /**
         * Indicates a limited time emote.
         */
        LIMITED_TIME("limitedtime", "owl2019"),

        /**
         * No emote type was assigned to this emote.
         */
        NONE,

        /**
         * Indicates a prime or turbo emote.
         */
        PRIME("prime", "turbo"),

        /**
         * Indicates a rewards emote.
         */
        REWARDS("rewards", "megacommerce", "megacheer"),

        /**
         * Indicates a smiley emote.
         */
        SMILIES,

        /**
         * Indicates a custom subscriber emote.
         */
        SUBSCRIPTIONS,

        /**
         * Indicates a two-factor emote.
         */
        TWO_FACTOR,

        /**
         * The channel emote type was unknown; please report on our Discord or Github!
         */
        OTHER;

        @NonNull
        private final String[] twitchStrings;

        Type() {
            this.twitchStrings = new String[] { this.name().toLowerCase().replace("_", "") };
        }

        Type(String... names) {
            this.twitchStrings = names;
        }

        private static final Map<String, Type> MAPPINGS;

        @NonNull
        public static Type parseEmoteType(String emoteType) {
            return MAPPINGS.getOrDefault(emoteType, OTHER);
        }

        static {
            final Map<String, Type> map = new HashMap<>();

            for (Type emoteType : values()) {
                for (String twitchString : emoteType.twitchStrings) {
                    map.put(twitchString, emoteType);
                }
            }

            MAPPINGS = Collections.unmodifiableMap(map);
        }

    }

    public enum Format {

        /**
         * Returns an animated GIF if available, otherwise, returns the static PNG.
         */
        @JsonEnumDefaultValue
        DEFAULT,

        /**
         * Indicates a static PNG file is available for this emote.
         */
        STATIC,

        /**
         * Indicates an animated GIF is available for this emote.
         */
        ANIMATED

    }

    @RequiredArgsConstructor
    public enum Scale {

        /**
         * A small version (28px x 28px) of the emote.
         */
        @JsonProperty("1.0")
        SMALL("1.0"),

        /**
         * A medium version (56px x 56px) of the emote.
         */
        @JsonProperty("2.0")
        MEDIUM("2.0"),

        /**
         * A large version (112px x 112px) of the emote.
         */
        @JsonProperty("3.0")
        LARGE("3.0");

        @Getter
        private final String twitchString;

    }

    public enum Theme {
        DARK,
        LIGHT
    }

}
