package me.philippheuer.twitch4j.connect.helix;

import me.philippheuer.twitch4j.api.helix.HelixApi;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;

public class TwitchHelixConnectionFactory extends OAuth2ConnectionFactory<HelixApi> {
    public TwitchHelixConnectionFactory(String clientId, String clientSecret) {
        super("twitch-helix", new TwitchHelixServiceProvider(clientId, clientSecret), new TwitchHelixAdapter());
    }
}
