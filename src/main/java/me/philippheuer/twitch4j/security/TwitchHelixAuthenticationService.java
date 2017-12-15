package me.philippheuer.twitch4j.security;

import me.philippheuer.twitch4j.api.helix.HelixApi;
import me.philippheuer.twitch4j.connect.helix.TwitchHelixConnectionFactory;
import org.springframework.social.security.provider.OAuth2AuthenticationService;

public class TwitchHelixAuthenticationService extends OAuth2AuthenticationService<HelixApi> {
    public TwitchHelixAuthenticationService(String clientId, String clientSecret) {
        super(new TwitchHelixConnectionFactory(clientId, clientSecret));
    }
}
