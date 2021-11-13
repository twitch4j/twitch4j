package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.CreatePredictionEventMutation;
import com.github.twitch4j.graphql.internal.type.CreatePredictionEventInput;
import lombok.NonNull;

public class CommandCreatePrediction extends BaseCommand<CreatePredictionEventMutation.Data> {
    private final CreatePredictionEventInput input;

    /**
     * Constructor
     *
     * @param apolloClient Apollo Client
     * @param input        Inputs for creating a new prediction
     */
    public CommandCreatePrediction(@NonNull ApolloClient apolloClient, @NonNull CreatePredictionEventInput input) {
        super(apolloClient);
        this.input = input;
    }

    @Override
    protected ApolloCall<CreatePredictionEventMutation.Data> getGraphQLCall() {
        return apolloClient.mutate(
            CreatePredictionEventMutation.builder()
                .input(input)
                .build()
        );
    }
}
