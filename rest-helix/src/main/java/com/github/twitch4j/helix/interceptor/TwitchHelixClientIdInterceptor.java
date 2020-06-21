package com.github.twitch4j.helix.interceptor;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.auth.providers.TwitchIdentityProvider;
import com.github.twitch4j.helix.TwitchHelixBuilder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Injects ClientId Header, the User Agent and other common headers into each API Request
 */
@Slf4j
public class TwitchHelixClientIdInterceptor implements RequestInterceptor {

    /**
     * Reference to the Client Builder
     */
    private final TwitchHelixBuilder twitchAPIBuilder;

    /**
     * Reference to the twitch identity provider
     */
    @Setter
    private TwitchIdentityProvider twitchIdentityProvider;

    /**
     * Access token cache
     */
    private final Cache<String, OAuth2Credential> accessTokenCache = Caffeine.newBuilder()
        .expireAfterWrite(15, TimeUnit.MINUTES)
        .maximumSize(10_000)
        .build();

    /**
     * The default app access token that is used if no oauth was passed by the user
     */
    private volatile OAuth2Credential defaultAuthToken;

    /**
     * The default client id, typically associated with {@link TwitchHelixClientIdInterceptor#defaultAuthToken}
     */
    private String defaultClientId;

    /**
     * Constructor
     *
     * @param twitchHelixBuilder Twitch Client Builder
     */
    public TwitchHelixClientIdInterceptor(TwitchHelixBuilder twitchHelixBuilder) {
        this.twitchAPIBuilder = twitchHelixBuilder;
        twitchIdentityProvider = new TwitchIdentityProvider(twitchHelixBuilder.getClientId(), twitchHelixBuilder.getClientSecret(), null);
        this.defaultClientId = twitchAPIBuilder.getClientId();
        this.defaultAuthToken = twitchHelixBuilder.getDefaultAuthToken();
        if (defaultAuthToken != null)
            twitchIdentityProvider.getAdditionalCredentialInformation(defaultAuthToken)
                .ifPresent(oauth -> this.defaultClientId = (String) oauth.getContext().get("client_id"));
    }

    /**
     * Interceptor
     *
     * @param template Feign RequestTemplate
     */
    @Override
    public void apply(RequestTemplate template) {
        String clientId = this.defaultClientId;

        // if a oauth token is passed is has to match that client id, default to global client id otherwise (for ie. token verification)
        if (template.headers().containsKey("Authorization")) {
            String oauthToken = template.headers().get("Authorization").iterator().next().substring("Bearer ".length());

            if (oauthToken.isEmpty()) {
                String clientSecret = twitchAPIBuilder.getClientSecret();
                if (defaultAuthToken == null && (StringUtils.isEmpty(clientId) || StringUtils.isEmpty(clientSecret) || clientSecret.charAt(0) == '*'))
                    throw new RuntimeException("Necessary OAuth token was missing from Helix call, without the means to generate one!");

                try {
                    oauthToken = getOrCreateAuthToken().getAccessToken();
                } catch (Exception e) {
                    throw new RuntimeException("Failed to generate an app access token as no oauth token was passed to this Helix call", e);
                }

                template.removeHeader("Authorization");
                template.header("Authorization", "Bearer " + oauthToken);
            } else {
                OAuth2Credential verifiedCredential = accessTokenCache.getIfPresent(oauthToken);
                if (verifiedCredential == null) {
                    log.debug("Getting matching client-id for authorization token {}", oauthToken.substring(0, 5));

                    Optional<OAuth2Credential> requestedCredential = twitchIdentityProvider.getAdditionalCredentialInformation(new OAuth2Credential("twitch", oauthToken));
                    if (!requestedCredential.isPresent()) {
                        throw new RuntimeException("Failed to get the client_id for the provided authentication token, the authentication token may be invalid!");
                    }

                    verifiedCredential = requestedCredential.get();
                    accessTokenCache.put(oauthToken, verifiedCredential);
                }

                clientId = (String) verifiedCredential.getContext().get("client_id");
            }

            log.debug("Setting new client-id {} for token {}", clientId, oauthToken.substring(0, 5));
        }

        // set headers
        template.header("Client-Id", clientId);
        template.header("User-Agent", twitchAPIBuilder.getUserAgent());
    }

    private OAuth2Credential getOrCreateAuthToken() {
        if (defaultAuthToken == null)
            synchronized (this) {
                if (defaultAuthToken == null)
                    return (this.defaultAuthToken = twitchIdentityProvider.getAppAccessToken());
            }

        return this.defaultAuthToken;
    }
}
