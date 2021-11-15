package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.FetchModCommentsQuery;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

public class CommandFetchModComments extends BaseCommand<FetchModCommentsQuery.Data> {
    private final String channelId;
    private final String targetId;
    private final String after;

    /**
     * Constructor
     *
     * @param apolloClient Apollo Client
     * @param channelId    The id of the channel where the moderator comment should be queried
     * @param targetId     The id of the target user of the moderator comment
     * @param after        Cursor for pagination
     */
    public CommandFetchModComments(@NonNull ApolloClient apolloClient, @NonNull String channelId, @NonNull String targetId, @Nullable String after) {
        super(apolloClient);
        this.channelId = channelId;
        this.targetId = targetId;
        this.after = after;
    }

    @Override
    protected ApolloCall<FetchModCommentsQuery.Data> getGraphQLCall() {
        return apolloClient.query(
            FetchModCommentsQuery.builder()
                .channelID(channelId)
                .targetID(targetId)
                .after(after)
                .build()
        );
    }
}
