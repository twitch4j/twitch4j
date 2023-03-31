package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class ChatBadgeSet {

    /**
     * An ID that identifies this set of chat badges.
     * <p>
     * For example, Bits or Subscriber.
     */
    private String setId;

    /**
     * Contains chat badge objects for the set.
     * <p>
     * The list is sorted in ascending order by {@link ChatBadge#getId()}.
     */
    private List<ChatBadge> versions;

}
