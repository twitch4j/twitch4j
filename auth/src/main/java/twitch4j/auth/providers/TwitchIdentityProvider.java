package twitch4j.auth.providers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.credentialmanager.identityprovider.OAuth2IdentityProvider;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Twitch Identity Provider
 */
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
    }

    /**
     * Get Auth Token Information
     *
     * @param authToken Auth Token
     */
    public Optional<OAuth2Credential> getTokenInformation(String authToken) {
        try {
            // api call
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://id.twitch.tv/oauth2/validate")
                    .header("Authorization", "OAuth " + authToken)
                    .build();

            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();

            // parse response
            ObjectMapper objectMapper = new ObjectMapper();
            HashMap<String, Object> tokenInfo = objectMapper.readValue(responseBody, new TypeReference<HashMap<String, Object>>() {});

            String userId = (String) tokenInfo.get("user_id");
            String userName = (String) tokenInfo.get("login");
            List<String> scopes = (List<String>) tokenInfo.get("scopes");

            // create credential instance
            OAuth2Credential credential = new OAuth2Credential("twitch", authToken, userId, userName, scopes);

            return Optional.ofNullable(credential);
        } catch (Exception ex) {
            // ignore, invalid token
        }

        return Optional.empty();
    }

    /**
     * Validate the Token
     *
     * @param authToken Auth Token
     */
    public Boolean validateToken(String authToken) {
        return getTokenInformation(authToken).isPresent();
    }

}
