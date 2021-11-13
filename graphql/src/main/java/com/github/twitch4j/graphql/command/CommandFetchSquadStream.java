package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.FetchSquadStreamQuery;
import lombok.NonNull;

public class CommandFetchSquadStream extends BaseCommand<FetchSquadStreamQuery.Data> {
    private final String id;

    /**
     * Constructor
     *
     * @param apolloClient Apollo Client
     * @param id           Squad Id
     */
    public CommandFetchSquadStream(@NonNull ApolloClient apolloClient, @NonNull String id) {
        super(apolloClient);
        this.id = id;
    }

    @Override
    protected ApolloCall<FetchSquadStreamQuery.Data> getGraphQLCall() {
        return apolloClient.query(
            FetchSquadStreamQuery.builder()
                .id(id)
                .build()
        );
    }
}
