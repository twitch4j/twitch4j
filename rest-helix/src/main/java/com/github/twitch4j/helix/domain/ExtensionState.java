package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum ExtensionState {

    @JsonAlias({ "Testing", "HostedTest" }) // undocumented, but have observed "Testing"
    @JsonProperty("InTest")
    IN_TEST,

    @JsonAlias({ "Reviewing", "ReadyForReview" }) // undocumented, unobserved
    @JsonProperty("InReview")
    IN_REVIEW,

    @JsonProperty("Rejected")
    REJECTED,

    @JsonProperty("Approved")
    APPROVED,

    @JsonProperty("Released")
    RELEASED,

    @JsonProperty("Deprecated")
    DEPRECATED,

    @JsonAlias("Pending") // undocumented, unobserved
    @JsonProperty("PendingAction")
    PENDING_ACTION,

    @JsonAlias("Uploading") // undocumented, unobserved
    @JsonProperty("AssetsUploaded")
    ASSETS_UPLOADED,

    @JsonProperty("Deleted")
    DELETED

}
