package me.philippheuer.twitch4j.impl;

import lombok.Data;
import me.philippheuer.twitch4j.IAuthorization;
import org.springframework.social.oauth2.OAuth2Version;

@Data
public class Authorization implements IAuthorization {
    private final String token;
    /**
     * The {@link OAuth2Version}.
     * <p>
     * Supported only for twitch: {@link OAuth2Version#BEARER} and {@link OAuth2Version#DRAFT_10}
     */
    private final OAuth2Version oAuth2Version;

    public Authorization(String token, OAuth2Version oAuth2Version) throws UnsupportedOperationException {
        if (oAuth2Version.equals(OAuth2Version.BEARER_DRAFT_2) || oAuth2Version.equals(OAuth2Version.DRAFT_8)) {
            throw new UnsupportedOperationException("The OAuth2Version is unsupported for Twitch.");
        }
        this.token = token;
        this.oAuth2Version = oAuth2Version;
    }
}
