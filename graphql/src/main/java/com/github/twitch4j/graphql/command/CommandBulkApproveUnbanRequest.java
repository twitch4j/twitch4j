package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.BulkApproveUnbanRequestMutation;
import com.github.twitch4j.graphql.internal.type.BulkApproveUnbanRequestInput;

import java.util.List;

public class CommandBulkApproveUnbanRequest extends BaseCommand<BulkApproveUnbanRequestMutation.Data> {
    private final List<String> ids;

    /**
     * Constructor
     *
     * @param apolloClient Apollo Client
     * @param ids          IDs of the unban requests to be approved
     */
    public CommandBulkApproveUnbanRequest(ApolloClient apolloClient, List<String> ids) {
        super(apolloClient);
        this.ids = ids;
    }

    @Override
    protected ApolloCall<BulkApproveUnbanRequestMutation.Data> getGraphQLCall() {
        return apolloClient.mutate(
            BulkApproveUnbanRequestMutation.builder()
                .input(
                    BulkApproveUnbanRequestInput.builder()
                        .ids(ids)
                        .build()
                )
                .build()
        );
    }
}
