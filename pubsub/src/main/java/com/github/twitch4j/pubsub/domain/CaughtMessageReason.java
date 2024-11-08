package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.common.enums.TwitchEnum;
import com.github.twitch4j.common.util.TwitchEnumDeserializer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
@Unofficial
public class CaughtMessageReason {

    private String reason;
    private AutomodFailure automodFailure;
    private BlockedTermFailure blockedTermFailure;

    public boolean matchesAutomodCategory() {
        return "AutoModCaughtMessageReason".equals(reason);
    }

    public boolean matchesBlockedTerm() {
        return "BlockedTermCaughtMessageReason".equals(reason);
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    public static class AutomodFailure {
        @JsonDeserialize(using = TwitchEnumDeserializer.class)
        private TwitchEnum<AutomodContentClassification.Category> category;

        private int level;

        @Nullable
        private List<Positions> positionsInMessage;
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    public static class BlockedTermFailure {
        @Accessors(fluent = true)
        @JsonProperty("contains_private_term")
        private boolean containsPrivateTerm;

        @Nullable
        private List<BlockedTerm> termsFound;
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    public static class BlockedTerm {
        @JsonProperty("is_private")
        private boolean isPrivate;
        private String termId;
        private String ownerChannelId;
        private String text;
        private Positions positionsInMessage;
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Positions {
        /**
         * Zero-indexed (inclusive) starting position of the blocked term
         */
        private int startPos;

        /**
         * Zero-indexed (inclusive) ending position of the blocked term
         */
        private int endPos;
    }

}
