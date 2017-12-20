package me.philippheuer.twitch4j.connect;

import lombok.Getter;
import me.philippheuer.twitch4j.api.TwitchApi;
import me.philippheuer.twitch4j.impl.api.TwitchTemplate;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Template;

public class TwitchServiceProvider extends AbstractOAuth2ServiceProvider<TwitchApi> {

	@Getter
	private final String clientId;

	public TwitchServiceProvider(String clientId, String clientSecret) {
		super(new OAuth2Template(clientId, clientSecret,
				"https://api.twitch.tv/kraken/oauth2/authorize",
				"https://api.twitch.tv/kraken/oauth2/token"));
		this.clientId = clientId;
	}

	@Override
	public TwitchApi getApi(String accessToken) {
		return new TwitchTemplate(clientId, accessToken);
	}
}
