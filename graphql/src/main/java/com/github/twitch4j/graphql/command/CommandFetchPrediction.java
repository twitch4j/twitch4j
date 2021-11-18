package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.FetchPredictionQuery;
import lombok.NonNull;

public class CommandFetchPrediction extends BaseCommand<FetchPredictionQuery.Data> {
    private final String predictionEventId;

    /**
     * Constructor
     *
     * @param apolloClient      Apollo Client
     * @param predictionEventId Prediction ID
     */
    public CommandFetchPrediction(@NonNull ApolloClient apolloClient, @NonNull String predictionEventId) {
        super(apolloClient);
        this.predictionEventId = predictionEventId;
    }

    @Override
    protected ApolloCall<FetchPredictionQuery.Data> getGraphQLCall() {
        return apolloClient.query(
            FetchPredictionQuery.builder()
                .id(predictionEventId)
                .build()
        );
    }
}
