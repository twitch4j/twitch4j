package com.github.twitch4j.helix.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.auth.providers.TwitchIdentityProvider;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.TwitchHelixBuilder;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.testcontainers.containers.GenericContainer;

@UtilityClass
public class TwitchCLIMockUtil {
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public TwitchHelix getHelixClient(GenericContainer container) {
        String server = "http://"+container.getHost()+":"+container.getMappedPort(8080);

        // client id and secret
        Pair<String, String> clientIdAndSecret = getClientIdAndSecret(server);

        // app access token
        TwitchIdentityProvider idp = new TwitchIdentityProvider(server+"/auth", clientIdAndSecret.getKey(), clientIdAndSecret.getValue(), null);

        // client
        return TwitchHelixBuilder.builder()
            .withBaseUrl(server+"/mock")
            .withClientId(clientIdAndSecret.getKey())
            .withClientSecret(clientIdAndSecret.getValue())
            .withDefaultAuthToken(new OAuth2Credential("twitch", idp.getAppAccessToken().getAccessToken()))
            .build();
    }

    @SneakyThrows
    private Pair<String, String> getClientIdAndSecret(String server) {
        Request request = new Request.Builder().get().url(server+"/units/clients").build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        Assertions.assertTrue(response.isSuccessful(), "request to mock api /units/clients should be successful");
        JsonNode node = OBJECT_MAPPER.readTree(response.body().string());
        String clientId = node.at("/data/0/ID").asText();
        String clientSecret = node.at("/data/0/Secret").asText();

        return Pair.of(clientId, clientSecret);
    }

}
