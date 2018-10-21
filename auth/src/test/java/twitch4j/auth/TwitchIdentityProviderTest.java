package twitch4j.auth;

import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.CredentialManagerBuilder;
import com.github.philippheuer.credentialmanager.domain.Credential;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import twitch4j.auth.providers.TwitchIdentityProvider;

@Slf4j
@Tag("unittest")
public class TwitchIdentityProviderTest {

    /**
     * Test - Twitch Identity Provider
     */
    @Test
    @DisplayName("Twitch Identity Provider")
    public void twitchIdentityProvider() {
        // build
        CredentialManager credentialManager = CredentialManagerBuilder.builder().build();

        // register idp
        credentialManager.registerIdentityProvider(new TwitchIdentityProvider("*clientId*", "", "http://localhost"));

        // add credential
        Credential credential = new OAuth2Credential("twitch", "*authToken*");
        credentialManager.addCredential("twitch", credential);
    }

}
