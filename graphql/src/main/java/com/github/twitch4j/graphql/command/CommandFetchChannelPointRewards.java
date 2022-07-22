package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.FetchChannelPointRewardsQuery;
import org.jetbrains.annotations.NotNull;

public class CommandFetchChannelPointRewards extends BaseCommand<FetchChannelPointRewardsQuery.Data> {

    private final String channelLogin;

    /**
     * Constructor
     *
     * @param apolloClient Apollo Client
     * @param channelLogin Channel login name
     */
    public CommandFetchChannelPointRewards(@NotNull ApolloClient apolloClient, @NotNull String channelLogin) {
        super(apolloClient);
        this.channelLogin = channelLogin;
    }

    @Override
    protected ApolloCall<FetchChannelPointRewardsQuery.Data> getGraphQLCall() {
        return apolloClient.query(
            FetchChannelPointRewardsQuery.builder()
                .login(channelLogin)
                .build()
        );
    }

}
