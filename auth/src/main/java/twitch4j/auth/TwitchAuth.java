package twitch4j.auth;

import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.domain.IdentityProvider;
import twitch4j.auth.providers.TwitchIdentityProvider;

public class TwitchAuth {

    /**
     * Credential Manager
     */
    private final CredentialManager credentialManager;

    public TwitchAuth(CredentialManager credentialManager, String clientId, String clientSecret, String redirectUrl) {
        this.credentialManager = credentialManager;

        // register the twitch identityProvider
        IdentityProvider identityProvider = new TwitchIdentityProvider(clientId, clientSecret, redirectUrl);
        this.credentialManager.registerIdentityProvider(identityProvider);
    }

}
