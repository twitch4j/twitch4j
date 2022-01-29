package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.FetchUnbanRequestsQuery;
import com.github.twitch4j.graphql.internal.type.UnbanRequestStatus;
import com.github.twitch4j.graphql.internal.type.UnbanRequestsSortOrder;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

public class CommandFetchUnbanRequests extends BaseCommand<FetchUnbanRequestsQuery.Data> {

    private final String channelLogin;
    private final String cursor;
    private final Integer limit;
    private final UnbanRequestsSortOrder order;
    private final UnbanRequestStatus status;
    private final String userId;

    /**
     * Constructor
     *
     * @param apolloClient Required: Apollo Client
     * @param channelLogin Required: The name of the channel where the ban took place
     * @param cursor       Optional: Cursor for forward pagination
     * @param limit        Optional: The number of elements to request in a single query (default: 25)
     * @param order        Optional: Whether unban requests should be returned by most recent or oldest (default: NEWEST)
     * @param status       Optional: The status of unban requests to query from (default: PENDING)
     * @param userId       Optional: Whether the unban request of a specific user should be queried
     */
    public CommandFetchUnbanRequests(@NonNull ApolloClient apolloClient, @NonNull String channelLogin, @Nullable String cursor, @Nullable Integer limit, @Nullable UnbanRequestsSortOrder order, @Nullable UnbanRequestStatus status, @Nullable String userId) {
        super(apolloClient);
        this.channelLogin = channelLogin;
        this.cursor = cursor;
        this.limit = limit;
        this.order = order == null ? UnbanRequestsSortOrder.NEWEST : order;
        this.status = status == null ? UnbanRequestStatus.PENDING : status;
        this.userId = userId;
    }

    @Override
    protected ApolloCall<FetchUnbanRequestsQuery.Data> getGraphQLCall() {
        return apolloClient.query(
            FetchUnbanRequestsQuery.builder()
                .channelLogin(channelLogin)
                .after(cursor)
                .first(limit)
                .order(order)
                .status(status)
                .userId(userId)
                .build()
        );
    }

}
