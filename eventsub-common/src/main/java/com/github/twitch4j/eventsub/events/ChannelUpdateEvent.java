package com.github.twitch4j.eventsub.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChannelUpdateEvent extends EventSubChannelEvent {

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
     * Whether  the channel is flagged as mature.
     */
    private Boolean isMature;

}
