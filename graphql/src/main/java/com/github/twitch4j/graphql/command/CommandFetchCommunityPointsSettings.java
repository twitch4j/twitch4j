package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.FetchCommunityPointsSettingsQuery;
import org.jetbrains.annotations.NotNull;

public class CommandFetchCommunityPointsSettings extends BaseCommand<FetchCommunityPointsSettingsQuery.Data> {

    private final String channelLogin;

    /**
     * Constructor
     *
     * @param apolloClient Apollo Client
     * @param channelLogin Channel login name
     */
    public CommandFetchCommunityPointsSettings(@NotNull ApolloClient apolloClient, @NotNull String channelLogin) {
        super(apolloClient);
        this.channelLogin = channelLogin;
    }

    @Override
    protected ApolloCall<FetchCommunityPointsSettingsQuery.Data> getGraphQLCall() {
        return apolloClient.query(
            FetchCommunityPointsSettingsQuery.builder()
                .login(channelLogin)
                .build()
        );
    }

}
