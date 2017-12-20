package me.philippheuer.twitch4j;

import lombok.*;
import lombok.experimental.Wither;
import me.philippheuer.twitch4j.connect.TwitchServiceProvider;
import me.philippheuer.twitch4j.connect.helix.TwitchHelixServiceProvider;
import me.philippheuer.twitch4j.impl.Application;
import me.philippheuer.twitch4j.impl.Authorization;
import me.philippheuer.twitch4j.impl.TwitchClient;
import me.philippheuer.twitch4j.security.TwitchAuthenticationService;
import me.philippheuer.twitch4j.security.TwitchHelixAuthenticationService;
import me.philippheuer.twitch4j.web.DefaultServletAppContext;
import me.philippheuer.twitch4j.web.ServletAppContext;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Version;
import org.springframework.social.security.provider.OAuth2AuthenticationService;
import org.springframework.util.Assert;

public class Builder {
	@Wither
	@ToString
	@EqualsAndHashCode
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Client {
		private String clientId;
		private String clientSecret;
		private IAuthorization botAuthorization;
		private Provider serviceProvider = Provider.KRAKEN;
		private ServletAppContext appContext = new DefaultServletAppContext(8080);

		public static Client init() {
			return new Client();
		}

		public IClient build() {
			Assert.isNull(clientId, "You need to provide a client id!");
			Assert.isNull(clientSecret, "You need to provide a client secret!");
			Application application = new Application(clientId, clientSecret);
			if (botAuthorization != null) {
				application.setBotAuthorization(botAuthorization);
			}

			final IClient client = new TwitchClient(application);

			return client;
		}

		public IClient connect() {
			final IClient client = build();
			Assert.isNull(botAuthorization, "You need to provide a bot authorization key!");
			client.connect();

			return client;
		}
	}

	@Wither
	@ToString
	@EqualsAndHashCode
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Credential {

		private String accessToken;
		@Wither(AccessLevel.NONE)
		private OAuth2Version oAuth2Version = OAuth2Version.DRAFT_10;

		public static Credential init() { return new Credential(); }

		public IAuthorization build() {
			return new Authorization(accessToken, oAuth2Version);
		}

		public Credential withOAuth2Version(OAuth2Version oAuth2Version) {
			if (oAuth2Version.equals(OAuth2Version.DRAFT_10) || oAuth2Version.equals(OAuth2Version.BEARER))
				this.oAuth2Version = oAuth2Version;

			return this;
		}
	}

	public enum Provider {
        KRAKEN {
            @Override
            public <T extends OAuth2AuthenticationService> T getProvider(String clientId, String clientSecret) {
                return (T) new TwitchAuthenticationService(clientId, clientSecret);
            }
        },
        HELIX {
            @Override
            public <T extends OAuth2AuthenticationService> T getProvider(String clientId, String clientSecret) {
                return (T) new TwitchHelixAuthenticationService(clientId, clientSecret);
            }
        };

        public abstract <T extends OAuth2AuthenticationService> T getProvider(String clientId, String clientSecret);
    }
}
