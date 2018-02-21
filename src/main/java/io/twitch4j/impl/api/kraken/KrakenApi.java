/*
 * MIT License
 *
 * Copyright (c) 2018 twitch4j
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.twitch4j.impl.api.kraken;

import com.fasterxml.jackson.databind.JsonNode;
import io.twitch4j.api.NoScopeInfo;
import io.twitch4j.api.kraken.IKraken;
import io.twitch4j.api.kraken.operations.*;
import io.twitch4j.impl.api.Api;
import io.twitch4j.impl.auth.Credential;
import io.twitch4j.utils.LoggerType;
import io.twitch4j.IClient;
import io.twitch4j.TwitchAPI;
import io.twitch4j.api.NoScopeInfo;
import io.twitch4j.api.kraken.IKraken;
import io.twitch4j.api.kraken.operations.*;
import io.twitch4j.auth.ICredential;
import io.twitch4j.auth.Scope;
import io.twitch4j.impl.api.Api;
import io.twitch4j.impl.auth.Credential;
import io.twitch4j.utils.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;

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
		Call<JsonNode> fetchUserInfo(@Header("Authorization") ICredential credential);
	}
}
