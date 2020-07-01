package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BannedEventList {

    @NonNull
    @JsonProperty("data")
    private BannedEvent events;

    @JsonProperty("pagination")
    private HelixPagination pagination;

}
