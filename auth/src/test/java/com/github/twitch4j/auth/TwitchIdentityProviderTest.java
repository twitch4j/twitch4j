package com.github.twitch4j.auth;

import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.CredentialManagerBuilder;
import com.github.twitch4j.auth.domain.TwitchScopes;
import com.github.twitch4j.auth.providers.TwitchIdentityProvider;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

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
        TwitchIdentityProvider identityProvider = new TwitchIdentityProvider("nzymnj7ao06w2u1smp8tqnmmp0rc5f", "***", "http://localhost:31921/process_oauth2");
        credentialManager.registerIdentityProvider(identityProvider);

        // add credential
        identityProvider.getAppAccessToken(EnumSet.range(TwitchScopes.HELIX_ANALYTICS_READ_EXTENSIONS, TwitchScopes.KRAKEN_VIEWING_ACTIVITY_READ)).ifPresent(oauth -> {
            credentialManager.addCredential("twitch", oauth);
        });

        // Assertions.assertFalse(credentialManager.getCredentials().isEmpty());
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
        TwitchIdentityProvider twitchIdentityProvider = new TwitchIdentityProvider("nzymnj7ao06w2u1smp8tqnmmp0rc5f", "***", "http://localhost:31921/process_oauth2");
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
