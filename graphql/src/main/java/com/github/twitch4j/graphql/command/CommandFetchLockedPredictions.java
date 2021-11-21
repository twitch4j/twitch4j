package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.FetchLockedPredictionsQuery;
import lombok.NonNull;

public class CommandFetchLockedPredictions extends BaseCommand<FetchLockedPredictionsQuery.Data> {
    private final String channelId;

    /**
     * Constructor
     *
     * @param apolloClient Apollo Client
     * @param channelId    The id of the channel to fetch locked predictions of
     */
    public CommandFetchLockedPredictions(@NonNull ApolloClient apolloClient, @NonNull String channelId) {
        super(apolloClient);
        this.channelId = channelId;
    }

    @Override
    protected ApolloCall<FetchLockedPredictionsQuery.Data> getGraphQLCall() {
        return apolloClient.query(
            FetchLockedPredictionsQuery.builder()
                .channelId(channelId)
                .build()
        );
    }
}
