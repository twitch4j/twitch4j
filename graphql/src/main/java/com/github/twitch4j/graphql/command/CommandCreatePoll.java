package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.CreatePollMutation;
import com.github.twitch4j.graphql.internal.type.CreatePollInput;
import lombok.NonNull;

public class CommandCreatePoll extends BaseCommand<CreatePollMutation.Data> {
    private final CreatePollInput input;

    /**
     * Constructor
     *
     * @param apolloClient Apollo Client
     * @param input        Inputs for creating a new poll
     */
    public CommandCreatePoll(@NonNull ApolloClient apolloClient, @NonNull CreatePollInput input) {
        super(apolloClient);
        this.input = input;
    }

    @Override
    protected ApolloCall<CreatePollMutation.Data> getGraphQLCall() {
        return apolloClient.mutate(
            CreatePollMutation.builder()
                .input(input)
                .build()
        );
    }
}
