package com.github.twitch4j.auth;

import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.CredentialManagerBuilder;
import com.github.philippheuer.credentialmanager.domain.Credential;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.auth.providers.TwitchIdentityProvider;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

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
        credentialManager.registerIdentityProvider(new TwitchIdentityProvider("nzymnj7ao06w2u1smp8tqnmmp0rc5f", "g5puvhnijc9w09m8lnaqc1jy1ao78c", "http://localhost:31921/process_oauth2"));

        // add credential
        Credential credential = new OAuth2Credential("twitch", "*authToken*");
        credentialManager.addCredential("twitch", credential);
    }

    /**
     * Test - Twitch Identity Provider - GUI
     */
    /*
    @Test
    @DisplayName("GUI Authentication Test")
    @Disabled
    @SneakyThrows
    public void testGuiAuthenticationController() {
        // build

        GUIAuthController guiAuthController = new GUIAuthController();
        CredentialManager credentialManager = CredentialManagerBuilder.builder()
            .withAuthenticationController(guiAuthController)
            .build();

        // register idp
        TwitchIdentityProvider twitchIdentityProvider = new TwitchIdentityProvider("nzymnj7ao06w2u1smp8tqnmmp0rc5f", "g5puvhnijc9w09m8lnaqc1jy1ao78c", "http://localhost:31921/process_oauth2");
        credentialManager.registerIdentityProvider(twitchIdentityProvider);

        // start oauth2 flow
        credentialManager.getAuthenticationController().startOAuth2AuthorizationCodeGrantType(twitchIdentityProvider, "http://localhost:31921/process_oauth2", Arrays.asList(
            TwitchScopes.CHAT_READ
        ));

        // wait 60 seconds and print resulting credentials
        Thread.sleep(60 * 1000);
        twitchIdentityProvider.getCredentialManager().getCredentials().forEach(c -> {
            System.out.println(c.toString());
        });
    }
    */

}
