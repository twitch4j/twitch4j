package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class ChatBadgeSet {

    /**
     * ID for the chat badge set.
     */
    private String setId;

    /**
     * Contains chat badge objects for the set.
     */
    private List<ChatBadge> versions;

}
