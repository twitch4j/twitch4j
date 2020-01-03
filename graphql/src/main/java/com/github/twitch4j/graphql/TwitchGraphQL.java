package com.github.twitch4j.graphql;

import com.apollographql.apollo.ApolloClient;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.twitch4j.graphql.command.CommandFollowUser;
import com.github.twitch4j.graphql.command.CommandUnfollowUser;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

@Slf4j
public class TwitchGraphQL {

    /**
     * Event Manager
     */
    private final EventManager eventManager;

    /**
     * Client Id
     */
    private String clientId;

    /**
     * Client Secret
     */
    private String clientSecret;

    /**
     * Constructor
     *
     * @param eventManager Event Manager
     * @param clientId Client Id
     * @param clientSecret Client Secret
     */
    public TwitchGraphQL(EventManager eventManager, String clientId, String clientSecret) {
        this.eventManager = eventManager;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    /**
     * Get ApolloClient with injected
     *
     * @param credential Credential to inject
     * @return ApolloClient
     */
    private ApolloClient getApolloClient(OAuth2Credential credential) {
        // Http Client
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();

                    Request.Builder requestBuilder = original
                        .newBuilder()
                        .header("Client-Id", clientId);

                    if (credential != null) {
                        requestBuilder.header("Authorization", "OAuth " + credential.getAccessToken());
                    }

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            })
            .build();

        // Apollo Client
        return ApolloClient.builder()
            .serverUrl("https://api.twitch.tv/gql")
            .okHttpClient(okHttpClient)
            .build();
    }

    /**
     * Follow a user
     *
     * @param auth               Credential
     * @param targetUserId       target user, that the user the auth token was provided for will follow
     * @param goLiveNotification should the follower be notified everytime a streamer goes live?
     * @return CommandFollowUser
     */
    public CommandFollowUser followUser(OAuth2Credential auth, Long targetUserId, Boolean goLiveNotification) {
        return new CommandFollowUser(getApolloClient(auth), targetUserId, goLiveNotification);
    }

    /**
     * Unfollow a user
     *
     * @param auth         Credential
     * @param targetUserId target user, that the user the auth token was provided for will follow
     * @return CommandUnfollowUser
     */
    public CommandUnfollowUser unfollowUser(OAuth2Credential auth, Long targetUserId) {
        return new CommandUnfollowUser(getApolloClient(auth), targetUserId);
    }

}
