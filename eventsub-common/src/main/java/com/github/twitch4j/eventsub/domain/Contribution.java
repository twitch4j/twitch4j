package com.github.twitch4j.eventsub.domain;

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
     * <p>
     * If type is bits, total represents the amount of Bits used.
     * <p>
     * If type is subscription, total is 500, 1000, or 2500 to represent tier 1, 2, or 3 subscriptions, respectively.
     */
    private Integer total;

    public enum Type {

        /**
         * Bits contributions with Cheering and Power-ups.
         */
        BITS,

        /**
         * Subscription activity like subscribing or gifting subscriptions.
         */
        SUBSCRIPTION,

        /**
         * Covers other contribution methods not listed.
         */
        OTHER;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }

}
