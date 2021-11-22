package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.FetchBanStatusQuery;
import lombok.NonNull;

public class CommandFetchBanStatus extends BaseCommand<FetchBanStatusQuery.Data> {
    private final String channelId;
    private final String userId;

    /**
     * Constructor
     *
     * @param apolloClient Apollo Client
     * @param channelId    Channel ID
     * @param userId       User ID
     */
    public CommandFetchBanStatus(@NonNull ApolloClient apolloClient, @NonNull String channelId, @NonNull String userId) {
        super(apolloClient);
        this.channelId = channelId;
        this.userId = userId;
    }

    @Override
    protected ApolloCall<FetchBanStatusQuery.Data> getGraphQLCall() {
        return apolloClient.query(
            FetchBanStatusQuery.builder()
                .channelID(channelId)
                .userID(userId)
                .build()
        );
    }
}
