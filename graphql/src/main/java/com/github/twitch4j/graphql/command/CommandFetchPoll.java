package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.FetchPollQuery;
import lombok.NonNull;

public class CommandFetchPoll extends BaseCommand<FetchPollQuery.Data> {
    private final String pollId;

    /**
     * Constructor
     *
     * @param apolloClient Apollo Client
     * @param pollId       Poll Id
     */
    public CommandFetchPoll(@NonNull ApolloClient apolloClient, @NonNull String pollId) {
        super(apolloClient);
        this.pollId = pollId;
    }

    @Override
    protected ApolloCall<FetchPollQuery.Data> getGraphQLCall() {
        return apolloClient.query(
            FetchPollQuery.builder()
                .id(pollId)
                .build()
        );
    }
}
