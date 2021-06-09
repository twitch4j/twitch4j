package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.common.enums.SubscriptionPlan;
import com.github.twitch4j.helix.TwitchHelix;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

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
     */
    private Images images;

    /**
     * ID of the emote set the emote belongs to.
     * <p>
     * This is <i>not</i> present for {@link TwitchHelix#getGlobalEmotes(String)}.
     * This <i>is</i> present for {@link TwitchHelix#getChannelEmotes(String, String)} and {@link TwitchHelix#getEmoteSets(String, Collection)}.
     * <p>
     * Note: at the time of writing, this is <a href="https://github.com/twitchdev/issues/issues/413">broken</a> on Twitch's end.
     */
    @Nullable
    private String emoteSetId;

    /**
     * User ID of the broadcaster who owns the emote.
     * <p>
     * This is <i>only</i> present for {@link TwitchHelix#getEmoteSets(String, Collection)}.
     */
    @Nullable
    private String ownerId;

    /**
     * The type of emote.
     * <p>
     * This is <i>only</i> present for {@link TwitchHelix#getChannelEmotes(String, String)} and {@link TwitchHelix#getEmoteSets(String, Collection)}.
     */
    @Nullable
    private Type emoteType;

    /**
     * The subscriber tier at which the emote is unlocked.
     * <p>
     * This is only present when {@link #getEmoteType()} equals {@link Type#SUBSCRIPTIONS}.
     * Also, this can only be present when using {@link TwitchHelix#getChannelEmotes(String, String)}.
     */
    @Nullable
    private SubscriptionPlan tier;

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
         * Indicates a custom Bits tier emote.
         */
        @JsonProperty("bitstier")
        BITS_TIER,

        /**
         * Indicates a custom follower emote.
         */
        FOLLOWER,

        /**
         * Indicates a custom subscriber emote.
         */
        SUBSCRIPTIONS,

        /**
         * The channel emote type was unknown; please report on our Discord or Github!
         */
        @JsonEnumDefaultValue
        OTHER

    }

}
