package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class DeletedVideoList {

    /**
     * IDs of the videos that were deleted.
     */
    @JsonProperty("data")
    private List<String> deletedVideoIds;

}
