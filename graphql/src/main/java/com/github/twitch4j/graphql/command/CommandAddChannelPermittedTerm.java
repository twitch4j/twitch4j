package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.AddChannelPermittedTermMutation;
import com.github.twitch4j.graphql.internal.type.AddChannelPermittedTermInput;
import lombok.NonNull;

import java.util.List;

public class CommandAddChannelPermittedTerm extends BaseCommand<AddChannelPermittedTermMutation.Data> {
    private final String channelId;
    private final List<String> phrases;

    /**
     * Constructor
     *
     * @param apolloClient Apollo Client
     * @param channelId    the owner of the permitted terms
     * @param phrases      the strings that are permitted in a channel
     */
    public CommandAddChannelPermittedTerm(@NonNull ApolloClient apolloClient, @NonNull String channelId, @NonNull List<String> phrases) {
        super(apolloClient);
        this.channelId = channelId;
        this.phrases = phrases;
    }

    @Override
    protected ApolloCall<AddChannelPermittedTermMutation.Data> getGraphQLCall() {
        return apolloClient.mutate(
            AddChannelPermittedTermMutation.builder()
                .input(
                    AddChannelPermittedTermInput.builder()
                        .channelID(channelId)
                        .phrases(phrases)
                        .build()
                )
                .build()
        );
    }
}
