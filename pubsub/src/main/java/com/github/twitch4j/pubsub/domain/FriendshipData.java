package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FriendshipData {
    private String userId;
    private String targetUserId;
    private Change change;

    public enum Change {
        ACCEPTED,
        REJECTED,
        REMOVED,
        REQUESTED,
        SELF_ACCEPTED,
        SELF_REJECTED,
        SELF_REMOVED,
        SELF_REQUESTED;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }

        private static final Map<String, Change> MAPPINGS = Arrays.stream(values()).collect(Collectors.toMap(Enum::toString, Function.identity()));

        @JsonCreator
        public static Change fromString(String change) {
            return MAPPINGS.get(change);
        }
    }
}
