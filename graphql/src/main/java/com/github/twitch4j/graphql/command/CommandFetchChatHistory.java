package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.FetchChatHistoryQuery;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

public class CommandFetchChatHistory extends BaseCommand<FetchChatHistoryQuery.Data> {
    private final String channelId;
    private final String userId;
    private final String after;

    /**
     * Constructor
     *
     * @param apolloClient Apollo Client
     * @param channelId    Channel ID
     * @param userId       User ID
     * @param after        Optional cursor for pagination
     */
    public CommandFetchChatHistory(@NonNull ApolloClient apolloClient, @NonNull String channelId, @NonNull String userId, @Nullable String after) {
        super(apolloClient);
        this.channelId = channelId;
        this.userId = userId;
        this.after = after;
    }

    @Override
    protected ApolloCall<FetchChatHistoryQuery.Data> getGraphQLCall() {
        return apolloClient.query(
            FetchChatHistoryQuery.builder()
                .channelID(channelId)
                .userID(userId)
                .after(after)
                .build()
        );
    }
}
