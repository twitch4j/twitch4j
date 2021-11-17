package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.ResolvePredictionEventMutation;
import com.github.twitch4j.graphql.internal.type.ResolvePredictionEventInput;
import lombok.NonNull;

public class CommandResolvePrediction extends BaseCommand<ResolvePredictionEventMutation.Data> {
    private final String predictionEventId;
    private final String outcomeId;

    /**
     * Constructor
     *
     * @param apolloClient      Apollo Client
     * @param predictionEventId ID of the prediction to be resolved
     * @param outcomeId         ID of the prediction outcome to be selected as the winner
     */
    public CommandResolvePrediction(@NonNull ApolloClient apolloClient, @NonNull String predictionEventId, String outcomeId) {
        super(apolloClient);
        this.predictionEventId = predictionEventId;
        this.outcomeId = outcomeId;
    }

    @Override
    protected ApolloCall<ResolvePredictionEventMutation.Data> getGraphQLCall() {
        return apolloClient.mutate(
            ResolvePredictionEventMutation.builder()
                .input(
                    ResolvePredictionEventInput.builder()
                        .eventID(predictionEventId)
                        .outcomeID(outcomeId)
                        .build()
                )
                .build()
        );
    }
}
