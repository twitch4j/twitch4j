package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.FetchLastBroadcastQuery;

public class CommandFetchLastBroadcast extends BaseCommand<FetchLastBroadcastQuery.Data> {
    private final String userId;
    private final String userLogin;

    /**
     * Constructor
     *
     * @param apolloClient Apollo Client
     * @param userId       Id of target user
     * @param userLogin    Login name of target user
     */
    public CommandFetchLastBroadcast(ApolloClient apolloClient, String userId, String userLogin) {
        super(apolloClient);
        this.userId = userId;
        this.userLogin = userLogin;

        if (userId == null && userLogin == null)
            throw new NullPointerException("Either the user id or login must be specified!");
    }

    @Override
    protected ApolloCall<FetchLastBroadcastQuery.Data> getGraphQLCall() {
        return apolloClient.query(
            FetchLastBroadcastQuery.builder()
                .id(userId)
                .login(userLogin)
                .build()
        );
    }
}
