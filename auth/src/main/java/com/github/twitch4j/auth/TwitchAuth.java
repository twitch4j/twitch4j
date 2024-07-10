package com.github.twitch4j.auth;

import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.domain.IdentityProvider;
import com.github.twitch4j.auth.providers.TwitchIdentityProvider;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class TwitchAuth {

    /**
     * Credential Manager
     */
    @Getter
    private final CredentialManager credentialManager;

    /**
     * Twitch Identity Provider
     *
     * @param credentialManager Credential Manager
     * @param clientId          OAuth2 Client Id
     * @param clientSecret      OAuth2 Client Secret
     * @param redirectUrl       OAuth2 Redirect Url
     */
    public TwitchAuth(CredentialManager credentialManager, String clientId, String clientSecret, String redirectUrl) {
        this.credentialManager = credentialManager;
        registerIdentityProvider(credentialManager, clientId, clientSecret, redirectUrl, false);
    }

    public static void registerIdentityProvider(CredentialManager credentialManager, String clientId, String clientSecret, String redirectUrl, boolean useMock) {
        // register the twitch identityProvider
        Optional<TwitchIdentityProvider> ip = credentialManager.getIdentityProviderByName(TwitchIdentityProvider.PROVIDER_NAME, TwitchIdentityProvider.class);
        if (!ip.isPresent()) {
            String baseUrl = useMock ? TwitchIdentityProvider.CLI_MOCK_BASE_URL : TwitchIdentityProvider.OFFICIAL_BASE_URL;
            IdentityProvider identityProvider = new TwitchIdentityProvider(clientId, clientSecret, redirectUrl, baseUrl);
            try {
                credentialManager.registerIdentityProvider(identityProvider);
            } catch (Exception e) {
                log.error("TwitchAuth: Encountered conflicting identity provider!", e);
            }
        } else {
            log.debug("TwitchIdentityProvider was already registered, ignoring call to TwitchAuth.registerIdentityProvider!");
        }
    }
}
