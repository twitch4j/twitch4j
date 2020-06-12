package com.github.twitch4j.auth.providers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.credentialmanager.identityprovider.OAuth2IdentityProvider;
import com.github.twitch4j.auth.domain.TwitchScopes;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Twitch Identity Provider
 */
@Slf4j
public class TwitchIdentityProvider extends OAuth2IdentityProvider {

    /**
     * Constructor
     *
     * @param clientId     OAuth Client Id
     * @param clientSecret OAuth Client Secret
     * @param redirectUrl  Redirect Url
     */
    public TwitchIdentityProvider(String clientId, String clientSecret, String redirectUrl) {
        super("twitch", "oauth2", clientId, clientSecret, "https://id.twitch.tv/oauth2/authorize", "https://id.twitch.tv/oauth2/token", redirectUrl);

        // configuration
        this.tokenEndpointPostType = "QUERY";
        this.scopeSeperator = "+"; // Prevents a URISyntaxException when creating a URI from the authUrl
    }

    /**
     * Creates an app access token with the desired scopes via the OAuth client credentials flow
     *
     * @param delimitedScopes space-separated string of the scopes the credential should possess
     * @return an OAuth credential in an optional wrapper
     */
    public Optional<OAuth2Credential> getAppAccessToken(String delimitedScopes) {
        HttpUrl url = HttpUrl.parse("https://id.twitch.tv/oauth2/token").newBuilder()
            .addQueryParameter("client_id", this.clientId)
            .addQueryParameter("client_secret", this.clientSecret)
            .addQueryParameter("grant_type", "client_credentials")
            .addQueryParameter("scope", delimitedScopes)
            .build();

        Request request = new Request.Builder()
            .url(url)
            .post(RequestBody.create("", null))
            .build();

        OkHttpClient client = new OkHttpClient();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String json = response.body().string();
                Map<String, Object> map = new ObjectMapper().readValue(json, new TypeReference<HashMap<String, Object>>() {});
                String accessToken = (String) map.get("access_token");
                String refreshToken = (String) map.get("refresh_token");
                Integer expiresIn = (Integer) map.get("expires_in");
                List<String> scopes = (List<String>) map.get("scope");
                return Optional.of(new OAuth2Credential("twitch", accessToken, refreshToken, null, null, expiresIn, scopes));
            }
        } catch (Exception ignored) {
        } finally {
            client.dispatcher().executorService().shutdown();
            client.connectionPool().evictAll();
        }

        return Optional.empty();
    }

    /**
     * Creates an app access token with the desired scopes via the OAuth client credentials flow
     *
     * @param scopes the scopes that the access token should possess
     * @return an OAuth credential in an optional wrapper
     */
    public Optional<OAuth2Credential> getAppAccessToken(Iterable<TwitchScopes> scopes) {
        return this.getAppAccessToken(StringUtils.join(scopes, ' '));
    }

    /**
     * Get Auth Token Information
     *
     * @param credential OAuth2 Credential
     */
    public Optional<OAuth2Credential> getAdditionalCredentialInformation(OAuth2Credential credential) {
        OkHttpClient client = new OkHttpClient();
        try {
            // api call
            Request request = new Request.Builder()
                .url("https://id.twitch.tv/oauth2/validate")
                .header("Authorization", "OAuth " + credential.getAccessToken())
                .build();

            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();

            // parse response
            if (response.isSuccessful()) {
                ObjectMapper objectMapper = new ObjectMapper();
                HashMap<String, Object> tokenInfo = objectMapper.readValue(responseBody, new TypeReference<HashMap<String, Object>>() {});
                String userId = (String) tokenInfo.get("user_id");
                String userName = (String) tokenInfo.get("login");
                List<String> scopes = (List<String>) tokenInfo.get("scopes");
                int expiresIn = (int) tokenInfo.get("expires_in");

                // create credential instance
                OAuth2Credential newCredential = new OAuth2Credential(credential.getIdentityProvider(), credential.getAccessToken(), credential.getRefreshToken(), userId, userName, expiresIn, scopes);

                // inject credential context
                newCredential.getContext().put("client_id", (String) tokenInfo.get("client_id"));

                return Optional.ofNullable(newCredential);
            } else {
                throw new RuntimeException("Request Failed! Code: " + response.code() + " - " + responseBody);
            }

        } catch (Exception ex) {
            // ignore, invalid token
        } finally {
            client.dispatcher().executorService().shutdown();
            client.connectionPool().evictAll();

            if (client.cache() != null && !client.cache().isClosed()) {
                try {
                    client.cache().close();
                } catch (Exception ex) {
                    log.warn("Failed to close OkHttp Client Cache ... [{}]", ex.getMessage());
                }

            }
        }

        return Optional.empty();
    }

}
