package me.philippheuer.twitch4j.security;

import me.philippheuer.twitch4j.api.TwitchApi;
import me.philippheuer.twitch4j.connect.TwitchConnectionFactory;
import org.springframework.social.security.provider.OAuth2AuthenticationService;

public class TwitchAuthenticationService extends OAuth2AuthenticationService<TwitchApi> {
    public TwitchAuthenticationService(String clientId, String clientSecret) {
        super(new TwitchConnectionFactory(clientId, clientSecret));
    }
}
