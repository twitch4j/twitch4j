package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.twitch4j.eventsub.domain.ContentClassification;
import com.github.twitch4j.helix.interceptor.ContentClassificationStateListSerializer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.With;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@With
@Setter(AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChannelInformation {

    /**
     * Twitch User ID of this channel owner
     */
    private String broadcasterId;

    /**
     * Twitch User Login Name of this channel owner
     */
    private String broadcasterLogin;

    /**
     * Twitch User Display Name of this channel owner
     */
    private String broadcasterName;

    /**
     * Language of the channel
     * <p>
     * A language value is either the ISO 639-1 two-letter code for a supported stream language or “other”
     */
    private String broadcasterLanguage;

    /**
     * Current game ID being played on the channel
     */
    private String gameId;

    /**
     * Current game name being played on the channel
     */
    private String gameName;

    /**
     * Title of the stream
     */
    private String title;

    /**
     * Stream delay in seconds.
     * <p>
     * Stream delay is a Twitch Partner feature; trying to set this value for other account types will return a 400 error.
     * <p>
     * Note: this can only be returned if using the broadcaster's user access toke
     */
    @Nullable
    private Integer delay;

    /**
     * A list of channel-defined tags to apply to the channel.
     * <p>
     * A channel may specify a maximum of 10 tags.
     * Each tag is limited to a maximum of 25 characters and may not be an empty string or contain spaces or special characters.
     * Tags are case-insensitive. For readability, consider using camelCasing or PascalCasing.
     * <p>
     * For {@link com.github.twitch4j.helix.TwitchHelix#updateChannelInformation(String, String, ChannelInformation)},
     * setting this to an empty list <a href="https://github.com/twitchdev/issues/issues/708">should</a> result in all tags being removed from the channel.
     */
    private List<String> tags;

    /**
     * The CCLs applied to the channel.
     */
    @Singular
    @JsonSerialize(using = ContentClassificationStateListSerializer.class)
    private Collection<ContentClassificationState> contentClassificationLabels;

    /**
     * Whether the channel has branded content.
     */
    @Accessors(fluent = true)
    @JsonProperty("is_branded_content")
    private Boolean isBrandedContent;

    /**
     * Converts the {@code content_classification_labels} list from {@link com.github.twitch4j.helix.TwitchHelix#getChannelInformation(String, List)}
     * into a list of {@link ContentClassificationState}, so that {@link ChannelInformation} can be passed to
     * {@link com.github.twitch4j.helix.TwitchHelix#updateChannelInformation(String, String, ChannelInformation)},
     * since the PATCH endpoint expects an array of objects (with {@code is_enabled} boolean flag)
     * rather than an array of strings (that the GET endpoint yields).
     *
     * @param labels collection of {@link ContentClassification}'s
     */
    @JsonProperty("content_classification_labels")
    private void setContentClassificationLabels(Collection<ContentClassification> labels) {
        if (labels == null) return;
        this.contentClassificationLabels = new ArrayList<>(labels.size());
        labels.forEach(label -> contentClassificationLabels.add(new ContentClassificationState(label, true)));
    }

}
