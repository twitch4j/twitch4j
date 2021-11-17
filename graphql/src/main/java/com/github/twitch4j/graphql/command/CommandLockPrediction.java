package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.LockPredictionEventMutation;
import com.github.twitch4j.graphql.internal.type.LockPredictionEventInput;
import lombok.NonNull;

public class CommandLockPrediction extends BaseCommand<LockPredictionEventMutation.Data> {
    private final String predictionEventId;

    /**
     * Constructor
     *
     * @param apolloClient      Apollo Client
     * @param predictionEventId ID of the prediction to be locked
     */
    public CommandLockPrediction(@NonNull ApolloClient apolloClient, @NonNull String predictionEventId) {
        super(apolloClient);
        this.predictionEventId = predictionEventId;
    }

    @Override
    protected ApolloCall<LockPredictionEventMutation.Data> getGraphQLCall() {
        return apolloClient.mutate(
            LockPredictionEventMutation.builder()
                .input(LockPredictionEventInput.builder().id(predictionEventId).build())
                .build()
        );
    }
}
