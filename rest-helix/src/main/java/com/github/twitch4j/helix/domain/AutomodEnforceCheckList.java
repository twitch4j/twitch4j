package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class AutomodEnforceCheckList {

    @NonNull
    @Singular
    @JsonProperty("data")
    List<AutomodEnforceCheck> messages;

}
