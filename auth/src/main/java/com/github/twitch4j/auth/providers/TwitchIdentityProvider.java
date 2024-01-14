package com.github.twitch4j.auth.providers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.credentialmanager.identityprovider.OAuth2IdentityProvider;
import com.github.philippheuer.credentialmanager.util.ProxyHelper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Twitch Identity Provider
 */
@Slf4j
public class TwitchIdentityProvider extends OAuth2IdentityProvider {

    public static final String PROVIDER_NAME = "twitch";

    /**
     * Constructor
     *
     * @param clientId     OAuth Client Id
     * @param clientSecret OAuth Client Secret
     * @param redirectUrl  Redirect Url
     */
    public TwitchIdentityProvider(String clientId, String clientSecret, String redirectUrl) {
        super(PROVIDER_NAME, "oauth2", clientId, clientSecret, "https://id.twitch.tv/oauth2/authorize", "https://id.twitch.tv/oauth2/token", "https://id.twitch.tv/oauth2/device", redirectUrl, ProxyHelper.selectProxy());

        // configuration
        this.tokenEndpointPostType = "QUERY";
        this.scopeSeperator = "+"; // Prevents a URISyntaxException when creating a URI from the authUrl
    }

    /**
     * Checks whether an {@link OAuth2Credential} is valid.
     * <p>
     * Expired tokens will yield false (assuming the network request succeeds).
     *
     * @param credential the OAuth credential to check
     * @return whether the token is valid, or empty if the network request did not succeed
     */
    public Optional<Boolean> isCredentialValid(OAuth2Credential credential) {
        if (credential == null || credential.getAccessToken() == null || credential.getAccessToken().isEmpty())
            return Optional.of(false);

        // build request
        Request request = new Request.Builder()
            .url("https://id.twitch.tv/oauth2/validate")
            .header("Authorization", "OAuth " + credential.getAccessToken())
            .build();

        // perform call
        try (Response response = httpClient.newCall(request).execute()) {
            // return token status
            if (response.isSuccessful())
                return Optional.of(true);

            if (response.code() >= 400 && response.code() < 500)
                return Optional.of(false);
        } catch (Exception ignored) {
            // fall through to return empty
        }

        return Optional.empty();
    }

    /**
     * Get Auth Token Information
     *
     * @param credential OAuth2 Credential
     */
    public Optional<OAuth2Credential> getAdditionalCredentialInformation(OAuth2Credential credential) {
        try {
            // api call
            Request request = new Request.Builder()
                .url("https://id.twitch.tv/oauth2/validate")
                .header("Authorization", "OAuth " + credential.getAccessToken())
                .build();

            Response response = httpClient.newCall(request).execute();
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
                newCredential.getContext().put("client_id", tokenInfo.get("client_id"));

                return Optional.of(newCredential);
            } else {
                throw new RuntimeException("Request Failed! Code: " + response.code() + " - " + responseBody);
            }

        } catch (Exception ex) {
            // ignore, invalid token
        }

        return Optional.empty();
    }

    /**
     * Revokes an access token.
     * <p>
     * The clientId passed to {@link TwitchIdentityProvider} must match that used to create the credential
     *
     * @param credential the {@link OAuth2Credential} to be revoked
     * @return whether the credential was successfully revoked
     */
    public boolean revokeCredential(OAuth2Credential credential) {
        HttpUrl url = HttpUrl.parse("https://id.twitch.tv/oauth2/revoke").newBuilder()
            .addQueryParameter("client_id", (String) credential.getContext().getOrDefault("client_id", clientId))
            .addQueryParameter("token", credential.getAccessToken())
            .build();

        Request request = new Request.Builder()
            .url(url)
            .post(RequestBody.create("", null))
            .build();

        try {
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return true;
            } else {
                log.warn("Unable to revoke access token! Code: " + response.code() + " - " + response.body().string());
            }
        } catch (Exception ignored) {
        }

        return false;
    }

}
