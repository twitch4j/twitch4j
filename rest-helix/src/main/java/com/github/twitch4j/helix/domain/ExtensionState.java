package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ExtensionState {

    @JsonAlias({ "Testing", "HostedTest" }) // undocumented, but have observed "Testing"
    IN_TEST("InTest"),

    @JsonAlias({ "Reviewing", "ReadyForReview" }) // undocumented, unobserved
    IN_REVIEW("InReview"),

    REJECTED("Rejected"),

    APPROVED("Approved"),

    RELEASED("Released"),

    DEPRECATED("Deprecated"),

    @JsonAlias("Pending") // undocumented, unobserved
    PENDING_ACTION("PendingAction"),

    @JsonAlias("Uploading") // undocumented, unobserved
    ASSETS_UPLOADED("AssetsUploaded"),

    DELETED("Deleted");

    private final String twitchString;

    @Override
    public String toString() {
        return this.twitchString;
    }
}
