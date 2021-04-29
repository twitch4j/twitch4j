package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

/**
 * Available Cheermotes
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class CheermoteList {

    @NonNull
    @JsonProperty("data")
    private List<Cheermote> cheermotes;

}
