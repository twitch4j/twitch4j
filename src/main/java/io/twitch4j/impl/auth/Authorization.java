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

package io.twitch4j.impl.auth;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import io.twitch4j.Builder;
import io.twitch4j.IClient;
import io.twitch4j.TwitchAPI;
import io.twitch4j.api.kraken.models.ErrorResponse;
import io.twitch4j.api.kraken.models.Kraken;
import io.twitch4j.api.kraken.models.User;
import io.twitch4j.auth.AuthorizationException;
import io.twitch4j.auth.IAuthorization;
import io.twitch4j.auth.ICredential;
import io.twitch4j.auth.Scope;
import io.twitch4j.utils.Util;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.eclipse.jetty.util.UrlEncoded;

import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class Authorization implements IAuthorization {

	private final IClient client;

	@Getter
	@Setter
	private boolean forceVerify = false;

	@Getter
	@Setter
	private boolean openIdConnect = false;

	private final OkHttpClient httpClient = new OkHttpClient();

	private final ObjectMapper mapper = new ObjectMapper();

	{
		this.mapper.setPropertyNamingStrategy(new PropertyNamingStrategy.SnakeCaseStrategy());
		this.mapper.enable(JsonGenerator.Feature.IGNORE_UNKNOWN);
	}

	@Override
	public String buildAuthorizationUrl(Set<Scope> scopes, String redirectUri, String state) {
		// do not add openid scope if OIDC is false
		openIdConnectScopeVerify(scopes);

		return new StringBuilder(TwitchAPI.KRAKEN.getUrl())
				.append("oauth2/authorize")
				.append("?response_type=code")
				.append("&client_id=").append(client.getConfiguration().getClientId())
				.append("&redirect_uri=").append(UrlEncoded.encodeString(redirectUri, Charset.forName("UTF-8")))
				.append("&scope=").append(scopes.stream().map(Scope::toString).collect(Collectors.joining("+")))
				.append("&state=").append(state)
				// Force Verify for reauthorizing your application
				.append("&force_verify=").append(Boolean.toString(forceVerify))
				.toString();
	}

	private void openIdConnectScopeVerify(Set<Scope> scopes) {
		if (openIdConnect) {
			if (!scopes.contains(Scope.OPENID)) {
				scopes.add(Scope.OPENID);
			}
		} else {
			if (scopes.contains(Scope.OPENID)) {
				scopes.remove(Scope.OPENID);
			}
		}
	}

	private AccessToken buildAccessToken(Response response) throws Exception {
		AccessToken accessToken = null;
		if (response.isSuccessful()) {
			accessToken = mapper.readValue(response.body().bytes(), AccessToken.class);
		} else if (response.code() >= 400) {
			throw new AuthorizationException(mapper.readValue(response.body().bytes(), ErrorResponse.class));
		}
		return accessToken;
	}

	@Override
	public ICredential buildAccessToken(String code, String redirectUri) throws Exception {
		String url = new StringBuilder((openIdConnect) ? TwitchAPI.DEFAULT.getUrl() : TwitchAPI.KRAKEN.getUrl())
				.append("oauth2/token")
				.append("?grant_type=authorization_code")
				.append("&client_id=").append(client.getConfiguration().getClientId())
				.append("&client_secret=").append(client.getConfiguration().getClientSecret())
				.append("&code=").append(code)
				.append("&redirect_uri=").append(UrlEncoded.encodeString(redirectUri, Charset.forName("UTF-8")))
				.toString();

		try (Response response = postResponse(url)) {
			return buildCredential(buildAccessToken(response));
		}
	}

	@Override
	public ICredential refreshToken(ICredential credential) throws Exception {
		String url = new StringBuilder(TwitchAPI.KRAKEN.getUrl())
				.append("oauth2/authorize")
				.append("?grant_type=refresh_token")
				.append("&refresh_token=").append(credential.getRefreshToken())
				.append("&client_id=").append(client.getConfiguration().getClientId())
				.append("&client_secret=").append(client.getConfiguration().getClientSecret())
				.toString();

		try (Response response = postResponse(url)) {
			return buildCredential(buildAccessToken(response));
		}
	}

	private ICredential buildCredential(AccessToken accessToken) throws Exception {
		Kraken kraken = client.getKrakenApi().fetchUserInfo(accessToken.getAccessToken());
		User user = client.getKrakenApi().usersOperation().getById(kraken.getUserId());
		Map<String, ?> data = new LinkedHashMap<>();
		Calendar expire = Calendar.getInstance();
		expire.add(Calendar.SECOND, (int) accessToken.getExpiresIn());

		Credential preCredential = Credential.newCredential()
				.accessToken(accessToken.getAccessToken())
				.refreshToken(accessToken.getRefreshToken())
				.expiredAt(expire)
				.idToken(JWT.decode(accessToken.getIdToken()))
				.scopes(kraken.getAuthorization().getScopes())
				.build();
		preCredential.setUser(client.getKrakenApi().usersOperation().get(preCredential));

		return preCredential;
	}

	private Response postResponse(String url) throws Exception {
		Request request = new Request.Builder()
				.url(url)
				.post(null)
				.build();
		try (Response response = httpClient.newCall(request).execute()) {
			return response;
		}
	}

	@Override
	public boolean revokeToken(ICredential credential) throws Exception {
		String url = new StringBuilder(TwitchAPI.KRAKEN.getUrl())
				.append("oauth2/revoke")
				.append("?response_type=code")
				.append("?client_id=").append(client.getConfiguration().getClientId())
				.append("&token=").append(credential.getAccessToken())
				.toString();

		try (Response response = postResponse(url)) {
			if (response.code() >= 400) {
				throw new AuthorizationException(mapper.readValue(response.body().bytes(), ErrorResponse.class));
			}
			return response.isSuccessful();
		}
	}

	@Override
	public ICredential rebuildCredentialData(ICredential credential) throws Exception {
		Credential preCredential = (Credential) credential;
		Kraken kraken = client.getKrakenApi().fetchUserInfo(credential.getAccessToken());
		if (!kraken.isValid()) {
			return refreshToken(credential);
		} else {
			if (Objects.isNull(credential.getUser())) {
				preCredential.setUser(client.getKrakenApi().usersOperation().get(credential));
			}
			if (Objects.isNull(credential.getScopes()) || credential.getScopes().size() != kraken.getAuthorization().getScopes().size() || Util.hasCollectionDifference(credential.getScopes(), kraken.getAuthorization().getScopes())) {
				preCredential.setScopes(kraken.getAuthorization().getScopes());
			}
			return preCredential;
		}
	}

	@Override
	public ICredential buildCredentialData(Builder.Credentials credentialBuilder) throws Exception {
		Kraken kraken = client.getKrakenApi().fetchUserInfo(credentialBuilder.getAccessToken());
		Calendar expire = Calendar.getInstance();
		expire.add(Calendar.DATE, 60);

		Credential preCredential = Credential.newCredential()
				.accessToken(credentialBuilder.getAccessToken())
				.refreshToken(credentialBuilder.getRefreshToken())
				.idToken(credentialBuilder.getIdToken())
				.scopes(kraken.getAuthorization().getScopes())
				.expiredAt(expire)
				.build();
		preCredential.setUser(client.getKrakenApi().usersOperation().get(preCredential));

		return preCredential;
	}
}
