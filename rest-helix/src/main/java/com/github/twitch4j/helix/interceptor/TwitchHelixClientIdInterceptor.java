package com.github.twitch4j.helix.interceptor;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * Injects ClientId Header, the User Agent and other common headers into each API Request
 */
@Slf4j
public class TwitchHelixClientIdInterceptor implements RequestInterceptor {

    public static final String AUTH_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    /**
     * User Agent
     */
    private final String userAgent;

    /**
     * Access token cache
     */
    private final TwitchHelixTokenManager tokenManager;

    /**
     * Constructor
     */
    public TwitchHelixClientIdInterceptor(String userAgent, TwitchHelixTokenManager tokenManager) {
        this.userAgent = userAgent;
        this.tokenManager = tokenManager;
    }

    /**
     * Interceptor
     *
     * @param template Feign RequestTemplate
     */
    @Override
    public void apply(RequestTemplate template) {
        String clientId = tokenManager.getDefaultClientId();

        // if a oauth token is passed is has to match that client id, default to global client id otherwise (for ie. token verification)
        if (template.headers().containsKey(AUTH_HEADER)) {
            String oauthToken = template.headers().get(AUTH_HEADER).iterator().next().substring(BEARER_PREFIX.length());

            if (oauthToken.isEmpty()) {
                try {
                    oauthToken = tokenManager.getDefaultAuthToken().getAccessToken();
                    clientId = tokenManager.getDefaultClientId();
                } catch (Exception e) {
                    throw new RuntimeException("Failed to generate an app access token as no oauth token was passed to this Helix call", e);
                }

                template.removeHeader(AUTH_HEADER);
                template.header(AUTH_HEADER, BEARER_PREFIX + oauthToken);
            } else if (!StringUtils.contains(oauthToken, '.')) {
                OAuth2Credential verifiedCredential = tokenManager.getOrPopulateCache(oauthToken);
                clientId = TwitchHelixTokenManager.extractClientId(verifiedCredential);
            }

            log.debug("Setting new client-id {} for token {}", clientId, oauthToken.substring(0, 5));
        }

        // set headers
        if (!template.headers().containsKey("Client-Id"))
            template.header("Client-Id", clientId);
        template.header("User-Agent", userAgent);
        if (template.body() != null && !template.headers().containsKey("Content-Type"))
            template.header("Content-Type", "application/json");
    }

}
