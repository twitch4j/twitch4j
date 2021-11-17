package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.AddChannelBlockedTermMutation;
import com.github.twitch4j.graphql.internal.type.AddChannelBlockedTermInput;
import lombok.NonNull;

import java.util.List;

public class CommandAddChannelBlockedTerm extends BaseCommand<AddChannelBlockedTermMutation.Data> {
    private final String channelId;
    private final Boolean isModEditable;
    private final List<String> phrases;

    /**
     * Constructor
     *
     * @param apolloClient  Apollo Client
     * @param channelId     the channelID owner of terms
     * @param isModEditable whether the term is viewable or editable by mods
     * @param phrases       the strings that are blocked
     */
    public CommandAddChannelBlockedTerm(@NonNull ApolloClient apolloClient, @NonNull String channelId, Boolean isModEditable, @NonNull List<String> phrases) {
        super(apolloClient);
        this.channelId = channelId;
        this.isModEditable = isModEditable;
        this.phrases = phrases;
    }

    @Override
    protected ApolloCall<AddChannelBlockedTermMutation.Data> getGraphQLCall() {
        return apolloClient.mutate(
            AddChannelBlockedTermMutation.builder()
                .input(
                    AddChannelBlockedTermInput.builder()
                        .channelID(channelId)
                        .isModEditable(isModEditable)
                        .phrases(phrases)
                        .build()
                )
                .build()
        );
    }
}
