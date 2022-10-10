package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class DeletePinnedChatData {
    @JsonProperty("id")
    private String pinId;
    private PinnedMessageCreator unpinnedBy;
    private String reason; // e.g., UNPIN
}
