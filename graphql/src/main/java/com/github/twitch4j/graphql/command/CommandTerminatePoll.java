package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.TerminatePollMutation;
import com.github.twitch4j.graphql.internal.type.TerminatePollInput;
import lombok.NonNull;

public class CommandTerminatePoll extends BaseCommand<TerminatePollMutation.Data> {
    private final String pollId;

    /**
     * Constructor
     *
     * @param apolloClient Apollo Client
     * @param pollId       The id of the poll to terminate
     */
    public CommandTerminatePoll(@NonNull ApolloClient apolloClient, @NonNull String pollId) {
        super(apolloClient);
        this.pollId = pollId;
    }

    @Override
    protected ApolloCall<TerminatePollMutation.Data> getGraphQLCall() {
        return apolloClient.mutate(
            TerminatePollMutation.builder()
                .input(
                    TerminatePollInput.builder()
                        .pollID(pollId)
                        .build()
                )
                .build()
        );
    }
}
