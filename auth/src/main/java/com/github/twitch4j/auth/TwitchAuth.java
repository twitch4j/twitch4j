package com.github.twitch4j.auth;

import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.domain.IdentityProvider;
import com.github.twitch4j.auth.providers.TwitchIdentityProvider;

public class TwitchAuth {

    /**
     * Credential Manager
     */
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
        IdentityProvider identityProvider = new TwitchIdentityProvider(clientId, clientSecret, redirectUrl);
        this.credentialManager.registerIdentityProvider(identityProvider);
    }

}
