package com.github.twitch4j.helix.interceptor;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.auth.providers.TwitchIdentityProvider;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

@Slf4j
public final class TwitchHelixTokenManager {

    private static final String CLIENT_ID_CONTEXT = "client_id";

    /**
     * Access token cache
     */
    private final Cache<String, OAuth2Credential> accessTokenCache = Caffeine.newBuilder()
        .expireAfterAccess(15, TimeUnit.MINUTES)
        .maximumSize(10_000)
        .build();

    /**
     * Reference to the twitch identity provider
     */
    private final TwitchIdentityProvider twitchIdentityProvider;

    /**
     * The client id provided in the helix builder
     */
    private final String clientId;

    /**
     * The client secret provided in the helix builder
     */
    private final String clientSecret;

    /**
     * The client id associated with defaultAuthToken
     */
    @Getter(AccessLevel.PACKAGE)
    private volatile String defaultClientId;

    /**
     * The default token that is used if no access token was directly provided to a helix call
     */
    private volatile OAuth2Credential defaultAuthToken;

    public TwitchHelixTokenManager(String clientId, String clientSecret, OAuth2Credential defaultAuthToken) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.twitchIdentityProvider = new TwitchIdentityProvider(clientId, clientSecret, null);
        this.defaultClientId = clientId;
        this.defaultAuthToken = defaultAuthToken;

        if (defaultAuthToken != null) {
            twitchIdentityProvider.getAdditionalCredentialInformation(defaultAuthToken).ifPresent(oauth -> {
                populateCache(oauth);
                this.defaultClientId = extractClientId(oauth);
                this.defaultAuthToken = oauth;
            });
        }
    }

    OAuth2Credential getDefaultAuthToken() {
        if (defaultAuthToken == null) {
            synchronized (this) {
                if (defaultAuthToken == null) {
                    checkClientCredentialsParameters();
                    OAuth2Credential token = twitchIdentityProvider.getAppAccessToken();
                    populateCache(token);
                    this.defaultClientId = (String) token.getContext().computeIfAbsent(CLIENT_ID_CONTEXT, s -> clientId);
                    return this.defaultAuthToken = token;
                }
            }
        }

        return defaultAuthToken;
    }

    @Nullable
    OAuth2Credential getIfPresent(@NotNull String accessToken) {
        return accessTokenCache.getIfPresent(accessToken);
    }

    @NotNull
    OAuth2Credential getOrPopulateCache(@NotNull String accessToken) {
        OAuth2Credential verifiedCredential = getIfPresent(accessToken);

        if (verifiedCredential == null) {
            log.debug("Getting matching client-id for authorization token {}", accessToken.substring(0, 5));
            verifiedCredential = twitchIdentityProvider
                .getAdditionalCredentialInformation(new OAuth2Credential(twitchIdentityProvider.getProviderName(), accessToken))
                .orElseThrow(() -> new RuntimeException("Failed to get the client_id for the provided authentication token, the authentication token may be invalid!"));
            populateCache(verifiedCredential);
        }

        return verifiedCredential;
    }

    private void populateCache(@NotNull OAuth2Credential enrichedCredential) {
        accessTokenCache.put(enrichedCredential.getAccessToken(), enrichedCredential);
    }

    private void checkClientCredentialsParameters() {
        if (StringUtils.isEmpty(defaultClientId) || StringUtils.isEmpty(clientSecret) || clientSecret.charAt(0) == '*')
            throw new RuntimeException("Necessary OAuth token was missing from Helix call, without the means to generate one!");
    }

    static String extractClientId(@NotNull OAuth2Credential credential) {
        return (String) credential.getContext().get(CLIENT_ID_CONTEXT);
    }

}
