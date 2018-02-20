package me.philippheuer.twitch4j.impl.api.kraken;

import com.fasterxml.jackson.databind.JsonNode;
import me.philippheuer.twitch4j.IClient;
import me.philippheuer.twitch4j.TwitchAPI;
import me.philippheuer.twitch4j.api.NoScopeInfo;
import me.philippheuer.twitch4j.api.kraken.IKraken;
import me.philippheuer.twitch4j.api.kraken.operations.*;
import me.philippheuer.twitch4j.auth.ICredential;
import me.philippheuer.twitch4j.auth.Scope;
import me.philippheuer.twitch4j.impl.api.Api;
import me.philippheuer.twitch4j.impl.auth.Credential;
import me.philippheuer.twitch4j.utils.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.http.GET;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

public class KrakenApi extends Api implements IKraken {

	private final Logger logger = LoggerFactory.getLogger(LoggerType.API);

	public KrakenApi(IClient client) {
		super(client, TwitchAPI.KRAKEN, IKraken.PREFIX_AUTHORIZATION);
	}

	@Override
	public ChannelKrakenOperation channelOperation() {
		return createService(ChannelKrakenOperation.class);
	}

	@Override
	public ClipsKrakenOperation clipsOperation() {
		return createService(ClipsKrakenOperation.class);
	}

	@Override
	public GamesKrakenOperation gamesOperation() {
		return createService(GamesKrakenOperation.class);
	}

	@Override
	public StreamsKrakenOperation streamsOperation() {
		return createService(StreamsKrakenOperation.class);
	}

	@Override
	public UserKrakenOperation userOperation() {
		return createService(UserKrakenOperation.class);
	}

	@Override
	public VideosKrakenOperation videosOperation() {
		return createService(VideosKrakenOperation.class);
	}

	@Override
	public ICredential fetchUserInfo(ICredential credential) {
		Call<JsonNode> callNode = createService(CoreService.class).fetchUserInfo(credential);
		JsonNode node = null;
		if (!callNode.isExecuted()) {
			try {
				node = callNode.execute().body();
			} catch (IOException e) {
				logger.error("Cannot fetch user information");
				return credential;
			}
		}

		if (node != null) {
			if (!node.get("valid").asBoolean() || credential.expiredAt().getTime().before(new Date())) {
				// Refresh token if session is not valid or expired
				return fetchUserInfo(getClient().getCredentialManager().refreshToken(credential));
			}

			Credential cred = (Credential) credential;
			JsonNode scopes = node.get("authorization").get("scopes");

			if (scopes.isArray()) {
				Set<Scope> scopeSet = new LinkedHashSet<>();
				for (final JsonNode scope : scopes) {
					Arrays.asList(Scope.values()).forEach(s -> {
						if (s.toString().equals(scope.textValue())) {
							scopeSet.add(s);
						}
					});
				}
				cred.setScopes(scopeSet);
			}
			cred.setUser(userOperation().getById(node.get("user_id").asLong()));
			getClient().getCredentialManager().getCredentialStorage().add(cred);
			return cred;
		} else return credential;
	}

	private interface CoreService {
		@GET("/")
		@NoScopeInfo
		Call<JsonNode> fetchUserInfo(ICredential credential);
	}
}
