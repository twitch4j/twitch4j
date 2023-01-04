package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
@AllArgsConstructor
public class AutomodEnforceCheckList {

    @NonNull
    @Singular
    @JsonProperty("data")
    List<AutomodEnforceCheck> messages;

}
