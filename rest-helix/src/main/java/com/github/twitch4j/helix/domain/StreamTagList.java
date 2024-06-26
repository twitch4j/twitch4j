package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

/**
 * Stream Tags List
 *
 * @see ChannelInformation#getTags()
 * @deprecated Twitch has deprecated UUID-based tags with the latest custom tags system.
 */
@Data
@Deprecated
@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
public class StreamTagList {
    /**
     * Data
     *
     * @see <a href="https://discuss.dev.twitch.tv/t/adding-customizable-tags-to-the-twitch-api/42921">Deprecation Information</a>
     * @deprecated These tags are no longer populated due to Twitch deprecation.
     */
    @Deprecated
    @JsonProperty("data")
    private List<StreamTag> streamTags;

    private HelixPagination pagination;

}
