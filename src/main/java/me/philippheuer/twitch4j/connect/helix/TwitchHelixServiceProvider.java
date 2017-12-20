package me.philippheuer.twitch4j.connect.helix;

import lombok.Getter;
import me.philippheuer.twitch4j.api.helix.HelixApi;
import me.philippheuer.twitch4j.impl.api.helix.HelixTemplate;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Template;

public class TwitchHelixServiceProvider extends AbstractOAuth2ServiceProvider<HelixApi> {

	@Getter
	private final String clientId;

	public TwitchHelixServiceProvider(String clientId, String clientSecret) {
		super(new OAuth2Template(clientId, clientSecret,
				"https://api.twitch.tv/kraken/oauth2/authorize",
				"https://api.twitch.tv/kraken/oauth2/token"));
		this.clientId = clientId;
	}

    @Override
    public HelixApi getApi(String accessToken) {
        return new HelixTemplate(getClientId(), accessToken);
    }
}
