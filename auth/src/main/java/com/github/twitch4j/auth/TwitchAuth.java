package com.github.twitch4j.auth;

import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.domain.IdentityProvider;
import com.github.philippheuer.credentialmanager.identityprovider.OAuth2IdentityProvider;
import com.github.twitch4j.auth.providers.TwitchIdentityProvider;
import lombok.Getter;

import java.util.Optional;

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
     * @param clientId OAuth2 Client Id
     * @param clientSecret OAuth2 Client Secret
     * @param redirectUrl OAuth2 Redirect Url
     */
    public TwitchAuth(CredentialManager credentialManager, String clientId, String clientSecret, String redirectUrl) {
        this.credentialManager = credentialManager;

        // register the twitch identityProvider
        Optional<OAuth2IdentityProvider> ip = this.credentialManager.getOAuth2IdentityProviderByName("twitch");
        if (ip.isPresent()) {
            // already registered
        } else {
            // register
            IdentityProvider identityProvider = new TwitchIdentityProvider(clientId, clientSecret, redirectUrl);
            this.credentialManager.registerIdentityProvider(identityProvider);
        }
    }

}
