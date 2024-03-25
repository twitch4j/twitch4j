package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class EmoteList {

    /**
     * The emote data.
     */
    @JsonProperty("data")
    private List<Emote> emotes;

    /**
     * A templated URL.
     *
     * @see <a href="https://dev.twitch.tv/docs/irc/emotes#cdn-template">Emote CDN URL format</a>
     */
    private String template;

    /**
     * Contains the information used to page through the list of results.
     * <p>
     * The object is empty if there are no more pages left to page through.
     */
    private HelixPagination pagination;

    /**
     * Uses the values from id, format, scale, and theme_mode to replace the like-named placeholder strings in the templated URL to create a CDN (content delivery network) URL that you use to fetch the emote.
     *
     * @param id     The emote’s ID.
     * @param format The format of the image to get.
     * @param theme  The background theme of the emote.
     * @param size   The size of the emote.
     * @return the populated emote template url.
     * @see #getTemplate()
     */
    public String getPopulatedTemplateUrl(@NonNull String id, @NonNull Emote.Format format, @NonNull Emote.Theme theme, @NonNull Emote.Scale size) {
        return StringUtils.replaceEach(
            getTemplate(),
            new String[] {
                "{{id}}",
                "{{format}}",
                "{{theme_mode}}",
                "{{scale}}"
            },
            new String[] {
                id,
                format.toString().toLowerCase(),
                theme.toString().toLowerCase(),
                size.getTwitchString()
            }
        );
    }

}
