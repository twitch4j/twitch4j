package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.CancelPredictionEventMutation;
import com.github.twitch4j.graphql.internal.type.CancelPredictionEventInput;
import lombok.NonNull;

public class CommandCancelPrediction extends BaseCommand<CancelPredictionEventMutation.Data> {
    private final String predictionEventId;

    /**
     * Constructor
     *
     * @param apolloClient      Apollo Client
     * @param predictionEventId The id of the prediction to be cancelled
     */
    public CommandCancelPrediction(@NonNull ApolloClient apolloClient, @NonNull String predictionEventId) {
        super(apolloClient);
        this.predictionEventId = predictionEventId;
    }

    @Override
    protected ApolloCall<CancelPredictionEventMutation.Data> getGraphQLCall() {
        return apolloClient.mutate(
            CancelPredictionEventMutation.builder()
                .input(CancelPredictionEventInput.builder().id(predictionEventId).build())
                .build()
        );
    }
}
