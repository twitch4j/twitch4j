package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

/**
 * @deprecated Twitch decommissioned entitlement code related endpoints on 2023-02-27.
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@Deprecated
public class CodeStatusList {

    @NonNull
    @JsonProperty("data")
    private List<CodeStatus> statuses;

}
