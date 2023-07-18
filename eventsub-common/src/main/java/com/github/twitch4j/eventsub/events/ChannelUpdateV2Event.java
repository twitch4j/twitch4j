package com.github.twitch4j.eventsub.events;

import com.github.twitch4j.eventsub.domain.ContentClassification;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ChannelUpdateV2Event extends EventSubChannelEvent {

    /**
     * The channel’s stream title.
     */
    private String title;

    /**
     * The channel’s broadcast language.
     */
    private String language;

    /**
     * The channel’s category ID.
     */
    private String categoryId;

    /**
     * The category name.
     */
    private String categoryName;

    /**
     * Content classification label IDs currently applied on the channel.
     */
    private Set<ContentClassification> contentClassificationLabels;

}
