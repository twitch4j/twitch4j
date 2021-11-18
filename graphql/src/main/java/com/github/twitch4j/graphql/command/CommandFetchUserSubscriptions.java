package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.FetchUserSubscriptionsQuery;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

public class CommandFetchUserSubscriptions extends BaseCommand<FetchUserSubscriptionsQuery.Data> {
    private final String userId;
    private final Integer first;
    private final String after;

    /**
     * Constructor
     *
     * @param apolloClient Apollo Client
     * @param userId       The id of the authenticated user
     * @param first        The number of subscriptions to return in a single call
     * @param after        Cursor for pagination
     */
    public CommandFetchUserSubscriptions(ApolloClient apolloClient, @NonNull String userId, @Nullable Integer first, @Nullable String after) {
        super(apolloClient);
        this.userId = userId;
        this.first = first;
        this.after = after;
    }

    @Override
    protected ApolloCall<FetchUserSubscriptionsQuery.Data> getGraphQLCall() {
        return apolloClient.query(
            FetchUserSubscriptionsQuery.builder()
                .after(after)
                .first(first)
                .id(userId)
                .build()
        );
    }
}
