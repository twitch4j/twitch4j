package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class ChannelEditorList {

    /**
     * Users who have editor permissions for a specific channel.
     */
    @JsonProperty("data")
    private List<ChannelEditor> editors;

}
