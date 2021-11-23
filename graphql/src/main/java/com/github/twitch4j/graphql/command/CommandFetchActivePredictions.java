package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.FetchActivePredictionsQuery;
import lombok.NonNull;

public class CommandFetchActivePredictions extends BaseCommand<FetchActivePredictionsQuery.Data> {
    private final String channelId;

    /**
     * Constructor
     *
     * @param apolloClient Apollo Client
     * @param channelId    The id of the channel to fetch active predictions of
     */
    public CommandFetchActivePredictions(@NonNull ApolloClient apolloClient, @NonNull String channelId) {
        super(apolloClient);
        this.channelId = channelId;
    }

    @Override
    protected ApolloCall<FetchActivePredictionsQuery.Data> getGraphQLCall() {
        return apolloClient.query(
            FetchActivePredictionsQuery.builder()
                .channelId(channelId)
                .build()
        );
    }
}
