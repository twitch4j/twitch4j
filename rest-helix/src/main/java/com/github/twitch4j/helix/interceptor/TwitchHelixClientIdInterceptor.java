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
     * Constructor
     *
     * @param twitchHelixBuilder Twitch Client Builder
     */
    public TwitchHelixClientIdInterceptor(TwitchHelixBuilder twitchHelixBuilder) {
        this.twitchAPIBuilder = twitchHelixBuilder;

        // we can use the request to get additional info without client id / secret / etc.
        twitchIdentityProvider = new TwitchIdentityProvider(null, null, null);
    }

    /**
     * Interceptor
     *
     * @param template Feign RequestTemplate
     */
    @Override
    public void apply(RequestTemplate template) {
        String clientId = twitchAPIBuilder.getClientId();

        // if a oauth token is passed is has to match that client id, default to global client id otherwise (for ie. token verification)
        if (template.headers().containsKey("Authorization")) {
            String oauthToken = template.headers().get("Authorization").iterator().next().substring("Bearer ".length());

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
            log.debug("Setting new client-id {} for token {}", clientId, oauthToken.substring(0, 5));
        }

        // set headers
        template.header("Client-Id", clientId);
        template.header("User-Agent", twitchAPIBuilder.getUserAgent());
    }
}
