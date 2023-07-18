package com.github.twitch4j.eventsub.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @deprecated in favor of {@link ChannelUpdateV2Event}
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Deprecated
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
    @Accessors(fluent = true)
    @JsonProperty("is_mature")
    private Boolean isMature;

}
