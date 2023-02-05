package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.common.util.TypeConvert;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Data
@Setter(AccessLevel.PRIVATE)
public class AutomodCaughtMessage {

    /**
     * Object containing details about the message.
     */
    private Content content;

    /**
     * Identifier of the message.
     */
    private String id;

    /**
     * Object representing the sender of the message.
     */
    private Sender sender;

    /**
     * Timestamp the message was sent in RFC3339 format.
     */
    private Instant sentAt;

    @Data
    @Setter(AccessLevel.PRIVATE)
    public static class Content {

        /**
         * The text of the message.
         */
        private String text;

        /**
         * Object defining the potentially inappropriate content of the message.
         */
        private List<Fragment> fragments;

    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    public static class Fragment {

        /**
         * Plain text fragment of the message sent.
         */
        private String text;

        @Nullable
        private FragmentFlags automod;

        /**
         * Information about an emote if present in the message.
         */
        @Nullable
        private FragmentEmote emoticon;

        @Nullable
        @Unofficial
        private FragmentLink link;

        @Nullable
        @Unofficial
        private FragmentMention userMention;

        /**
         * @return whether this fragment of the message was flagged by AutoMod.
         */
        public boolean isFragmentFlagged() {
            return getAutomod() != null && getAutomod().getTopics() != null && !getAutomod().getTopics().isEmpty();
        }

        /**
         * @return whether this fragment of the message is a link.
         */
        @Unofficial
        public boolean isFragmentLink() {
            return getLink() != null && getLink().getHost() != null;
        }

        /**
         * @return whether this fragment of the message is simply a user mention.
         */
        @Unofficial
        public boolean isFragmentMention() {
            return getUserMention() != null && getUserMention().getUserId() != null;
        }

    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    public static class FragmentEmote {
        /**
         * An ID that identifies this emote.
         */
        @JsonAlias("emoticonID")
        private String emoticonId;

        /**
         * An ID that identifies the emote set that the emote belongs to.
         */
        @JsonAlias("emoticonSetID")
        private String emoticonSetId;
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    public static class FragmentFlags {
        private Map<String, Integer> topics;

        @Unofficial
        public Map<AutomodContentClassification.Category, Integer> getParsedTopics() {
            return TypeConvert.convertValue(topics, new TypeReference<EnumMap<AutomodContentClassification.Category, Integer>>() {});
        }
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    public static class FragmentLink {
        private String host;
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    public static class FragmentMention {
        @JsonProperty("userID")
        String userId;
        String login;
        String displayName;
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    public static class Sender {

        /**
         * User ID of the sender.
         */
        private String userId;

        /**
         * User login name of the sender.
         */
        private String login;

        /**
         * User display name of the sender.
         */
        private String displayName;

        /**
         * Chat color of the sender.
         */
        @Unofficial
        private String chatColor;

        @Unofficial
        private List<Badge> badges;

        @Data
        @Setter(AccessLevel.PRIVATE)
        public static class Badge {
            private String id;
            private String version;
        }

    }

}
