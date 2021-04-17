package com.github.twitch4j.eventsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class Contribution {

    /**
     * The ID of the user.
     */
    private String userId;

    /**
     * The display name of the user.
     */
    private String userName;

    /**
     * The login name of the user.
     */
    private String userLogin;

    /**
     * Type of contribution.
     */
    private Type type;

    /**
     * The total contributed.
     */
    private Integer total;

    public enum Type {
        @JsonProperty("bits")
        BITS,
        @JsonProperty("subscription")
        SUBSCRIPTION;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }

}
