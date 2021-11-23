package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.FetchChattersQuery;
import lombok.NonNull;

public class CommandFetchChatters extends BaseCommand<FetchChattersQuery.Data> {
    private final String channelLogin;

    /**
     * Constructor
     *
     * @param apolloClient Apollo Client
     * @param channelLogin The name of the channel to get the connected chatters of
     */
    public CommandFetchChatters(@NonNull ApolloClient apolloClient, @NonNull String channelLogin) {
        super(apolloClient);
        this.channelLogin = channelLogin;
    }

    @Override
    protected ApolloCall<FetchChattersQuery.Data> getGraphQLCall() {
        return apolloClient.query(
            FetchChattersQuery.builder()
                .login(channelLogin)
                .build()
        );
    }
}
