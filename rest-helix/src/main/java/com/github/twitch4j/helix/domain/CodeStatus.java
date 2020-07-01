package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CodeStatus {
    /**
     * The code to check the status of or to redeem.
     */
    private String code;

    /**
     * Indicates the current status of each key when checking key status or redeeming.
     */
    private Status status;

    public enum Status {
        /**
         * Request successfully redeemed this code to the authenticated user’s account.
         */
        SUCCESSFULLY_REDEEMED,
        /**
         * Code has already been claimed by a Twitch user.
         */
        ALREADY_CLAIMED,
        /**
         * Code has expired and can no longer be claimed.
         */
        EXPIRED,
        /**
         * User is not eligible to redeem this code.
         */
        USER_NOT_ELIGIBLE,
        /**
         * Code is not valid and/or does not exist in our database.
         */
        NOT_FOUND,
        /**
         * Code is not currently active.
         */
        INACTIVE,
        /**
         * Code has not been claimed.
         */
        UNUSED,
        /**
         * Code was not properly formatted.
         */
        INCORRECT_FORMAT,
        /**
         * Indicates some internal and/or unknown failure handling this code.
         */
        INTERNAL_ERROR
    }
}
