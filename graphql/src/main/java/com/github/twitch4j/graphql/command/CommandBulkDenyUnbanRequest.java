package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.BulkDenyUnbanRequestMutation;
import com.github.twitch4j.graphql.internal.type.BulkDenyUnbanRequestInput;
import lombok.NonNull;

import java.util.List;

public class CommandBulkDenyUnbanRequest extends BaseCommand<BulkDenyUnbanRequestMutation.Data> {
    private final List<String> ids;

    /**
     * Constructor
     *
     * @param apolloClient Apollo Client
     * @param ids          IDs of the unban requests to be denied
     */
    public CommandBulkDenyUnbanRequest(@NonNull ApolloClient apolloClient, @NonNull List<String> ids) {
        super(apolloClient);
        this.ids = ids;
    }

    @Override
    protected ApolloCall<BulkDenyUnbanRequestMutation.Data> getGraphQLCall() {
        return apolloClient.mutate(
            BulkDenyUnbanRequestMutation.builder()
                .input(
                    BulkDenyUnbanRequestInput.builder()
                        .ids(ids)
                        .build()
                )
                .build()
        );
    }
}
