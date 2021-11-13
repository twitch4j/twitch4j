package com.github.twitch4j.helix.domain;

/**
 * Status code applied to a set of entitlements for the update operation that can be used to indicate partial success.
 */
public enum UpdateEntitlementStatus {

    /**
     * Entitlement was successfully updated.
     */
    SUCCESS,

    /**
     * Invalid format for entitlement ID.
     */
    INVALID_ID,

    /**
     * Entitlement ID does not exist.
     */
    NOT_FOUND,

    /**
     * Entitlement is not owned by the organization or the user when called with a user OAuth token.
     */
    UNAUTHORIZED,

    /**
     * Indicates the entitlement update operation failed.
     * Errors in the this state are expected to be be transient and should be retried later.
     */
    UPDATE_FAILED

}
