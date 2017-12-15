package me.philippheuer.twitch4j.connect;

import me.philippheuer.twitch4j.api.TwitchApi;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;

public class TwitchConnectionFactory extends OAuth2ConnectionFactory<TwitchApi> {
    public TwitchConnectionFactory(String clientId, String clientSecret) {
        super("twitch", new TwitchServiceProvider(clientId, clientSecret), new TwitchAdapter());
    }
}
