package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.DeleteChannelBlockedTermMutation;
import com.github.twitch4j.graphql.internal.type.DeleteChannelBlockedTermInput;
import lombok.NonNull;

import java.util.List;

public class CommandDeleteChannelBlockedTerm extends BaseCommand<DeleteChannelBlockedTermMutation.Data> {
    private final String channelId;
    private final List<String> phrases;

    /**
     * Constructor
     *
     * @param apolloClient Apollo Client
     * @param channelId    the owner of the term being deleted
     * @param phrases      the strings representation of the term being deleted
     */
    public CommandDeleteChannelBlockedTerm(@NonNull ApolloClient apolloClient, @NonNull String channelId, @NonNull List<String> phrases) {
        super(apolloClient);
        this.channelId = channelId;
        this.phrases = phrases;
    }

    @Override
    protected ApolloCall<DeleteChannelBlockedTermMutation.Data> getGraphQLCall() {
        return apolloClient.mutate(
            DeleteChannelBlockedTermMutation.builder()
                .input(
                    DeleteChannelBlockedTermInput.builder()
                        .channelID(channelId)
                        .phrases(phrases)
                        .build()
                )
                .build()
        );
    }
}
