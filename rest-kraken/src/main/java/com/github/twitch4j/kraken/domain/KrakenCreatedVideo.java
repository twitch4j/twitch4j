package com.github.twitch4j.kraken.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class KrakenCreatedVideo {

    /**
     * Important information for future calls to upload video parts.
     */
    private Upload upload;

    private KrakenVideo video;

    public String getVideoId() {
        // Twitch's documentation had some inconsistencies in their example response, so this includes some fallbacks
        String url = upload.url;
        int i;

        // Fallback 1
        if (url == null || (i = url.lastIndexOf('/')) < 25) {
            url = this.video.getUrl();
            i = url.lastIndexOf('/');
        }

        // Fallback 2
        if (i < 15)
            return video.getIdForApiCalls();

        return url.substring(i + 1);
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Upload {
        /**
         * The URL where you will upload video parts.
         */
        private String url;

        /**
         * The token you will use to upload video parts.
         * <p>
         * Upload tokens expire after 1 day.
         * If you lose the token or it expires, you must start a new upload and get a new token.
         */
        private String token;
    }

}
