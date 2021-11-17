package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.FetchUserEmoteSetsQuery;
import lombok.NonNull;

public class CommandFetchUserEmoteSets extends BaseCommand<FetchUserEmoteSetsQuery.Data> {
    private final String userId;

    /**
     * Constructor
     *
     * @param apolloClient Apollo Client
     * @param userId       The ID of the authenticated user
     */
    public CommandFetchUserEmoteSets(ApolloClient apolloClient, @NonNull String userId) {
        super(apolloClient);
        this.userId = userId;
    }

    @Override
    protected ApolloCall<FetchUserEmoteSetsQuery.Data> getGraphQLCall() {
        return apolloClient.query(
            FetchUserEmoteSetsQuery.builder()
                .id(userId)
                .build()
        );
    }
}
