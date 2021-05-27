package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.common.util.TypeConvert;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Data
@Setter(AccessLevel.PRIVATE)
public class AutomodCaughtMessage {

    /**
     * Object containing details about the message
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
        private String text;
        private FragmentFlags automod;
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    public static class FragmentFlags {
        private Map<String, Integer> topics;

        public Map<AutomodContentClassification.Category, Integer> getParsedTopics() {
            return TypeConvert.convertValue(topics, new TypeReference<EnumMap<AutomodContentClassification.Category, Integer>>() {});
        }
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
