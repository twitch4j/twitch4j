package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.ArchivePollMutation;
import com.github.twitch4j.graphql.internal.type.ArchivePollInput;
import lombok.NonNull;

public class CommandArchivePoll extends BaseCommand<ArchivePollMutation.Data> {
    private final String pollId;

    /**
     * Constructor
     *
     * @param apolloClient Apollo Client
     * @param pollId       The id of the poll to archive
     */
    public CommandArchivePoll(@NonNull ApolloClient apolloClient, @NonNull String pollId) {
        super(apolloClient);
        this.pollId = pollId;
    }

    @Override
    protected ApolloCall<ArchivePollMutation.Data> getGraphQLCall() {
        return apolloClient.mutate(
            ArchivePollMutation.builder()
                .input(
                    ArchivePollInput.builder()
                        .pollID(pollId)
                        .build()
                )
                .build()
        );
    }
}
