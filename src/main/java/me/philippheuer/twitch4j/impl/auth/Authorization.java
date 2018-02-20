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

package me.philippheuer.twitch4j.impl.auth;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.philippheuer.twitch4j.IClient;
import me.philippheuer.twitch4j.TwitchAPI;
import me.philippheuer.twitch4j.auth.IAuthorization;
import me.philippheuer.twitch4j.auth.ICredential;
import me.philippheuer.twitch4j.auth.Scope;
import okhttp3.Response;
import org.eclipse.jetty.util.UrlEncoded;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.nio.charset.Charset;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class Authorization implements IAuthorization {
	@NonNull private final IClient client;

	@Getter
	@Setter
	private boolean forceVerify = false;

	@Getter
	@Setter
	private boolean openIdConnect = false;

	private final Retrofit retrofit = new Retrofit.Builder()
			.baseUrl((openIdConnect) ? TwitchAPI.DEFAULT : TwitchAPI.KRAKEN)
			.addConverterFactory(JacksonConverterFactory.create())
			.build();

	@Override
	public String buildAuthorizationUrl(Set<Scope> scopes, String redirectUri, String state) {
		// do not add openid scope if OIDC is false
		openIdConnectScopeVerify(scopes);

		return new StringBuilder(TwitchAPI.KRAKEN)
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

	@Override
	public ICredential buildAccessToken(String code, String redirectUri) {
		OAuth2 oAuth2 = retrofit.create(OAuth2.class);
		Credential credential = oAuth2.getToken(
				client.getConfiguration().getClientId(),
				client.getConfiguration().getClientSecret(),
				redirectUri,
				code
		);

		credential.setUser(client.getKrakenApi().userOperation().get(credential));

		return credential;
	}

	@Override
	public ICredential refreshToken(ICredential credential) {
		// cast to Credential to change
		Credential oldCredential = (Credential) credential;
		OAuth2 oAuth2 = retrofit.create(OAuth2.class);
		Credential newCredential = oAuth2.refreshToken(
				client.getConfiguration().getClientId(),
				client.getConfiguration().getClientSecret(),
				oldCredential.getRefreshToken()
		);
		newCredential.setUser(oldCredential.getUser());
		return newCredential;
	}

	@Override
	public boolean revokeToken(ICredential credential) {
		return retrofit.create(OAuth2.class).revokeToken(client.getConfiguration().getClientId(), credential.getAccessToken()).isSuccessful();
	}

	public interface OAuth2 {
		@POST("/oauth2/token?grant_type=authorization_code")
		Credential getToken(
				@Query("client_id") String clientId,
				@Query("client_secret") String clientSecret,
				@Query("redirect_uri") String redirectUri,
				@Query("code") String code
		);
		@POST("https://api.twitch.tv/kraken/oauth2/token?grant_type=refresh_token")
		Credential refreshToken(
				@Query("client_id") String clientId,
				@Query("client_secret") String clientSecret,
				@Query("refresh_token") String refreshToken
		);
		@POST("https://api.twitch.tv/kraken/oauth2/revoke")
		Response revokeToken(
				@Query("client_id") String clientId,
				@Query("token") String accessToken
		);
	}
}
