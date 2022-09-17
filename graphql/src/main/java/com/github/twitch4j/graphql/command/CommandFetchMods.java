package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.FetchModsQuery;

public class CommandFetchMods extends BaseCommand<FetchModsQuery.Data> {

    /**
     * The login name of the channel whose mod list is being queried
     */
    private final String channelLogin;

    /**
     * Relay cursor for forward pagination (optional)
     */
    private final String cursor;

    /**
     * The maximum number of nodes to return in a single call
     */
    private final int limit;

    /**
     * Constructor
     *
     * @param apolloClient Apollo Client
     * @param channelLogin The login name of the channel whose mod list is being queried
     * @param cursor       Relay cursor for forward pagination (optional)
     * @param limit        The maximum number of nodes to return in a single call
     */
    public CommandFetchMods(ApolloClient apolloClient, String channelLogin, String cursor, Integer limit) {
        super(apolloClient);
        this.channelLogin = channelLogin;
        this.cursor = cursor;
        this.limit = limit != null ? limit : 100;
    }

    @Override
    protected ApolloCall<FetchModsQuery.Data> getGraphQLCall() {
        return apolloClient.query(
            FetchModsQuery.builder()
                .channelLogin(channelLogin)
                .after(cursor)
                .first(limit)
                .build()
        );
    }
}
