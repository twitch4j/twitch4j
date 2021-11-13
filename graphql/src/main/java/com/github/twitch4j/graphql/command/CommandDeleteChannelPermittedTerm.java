package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.DeleteChannelPermittedTermMutation;
import com.github.twitch4j.graphql.internal.type.DeleteChannelPermittedTermInput;
import lombok.NonNull;

import java.util.List;

public class CommandDeleteChannelPermittedTerm extends BaseCommand<DeleteChannelPermittedTermMutation.Data> {
    private final String channelId;
    private final List<String> phrases;

    /**
     * Constructor
     *
     * @param apolloClient Apollo Client
     * @param channelId    the ID of the owner of the permitted term being deleted
     * @param phrases      the string representation of the term being deleted
     */
    public CommandDeleteChannelPermittedTerm(@NonNull ApolloClient apolloClient, @NonNull String channelId, @NonNull List<String> phrases) {
        super(apolloClient);
        this.channelId = channelId;
        this.phrases = phrases;
    }

    @Override
    protected ApolloCall<DeleteChannelPermittedTermMutation.Data> getGraphQLCall() {
        return apolloClient.mutate(
            DeleteChannelPermittedTermMutation.builder()
                .input(
                    DeleteChannelPermittedTermInput.builder()
                        .channelID(channelId)
                        .phrases(phrases)
                        .build()
                )
                .build()
        );
    }
}
