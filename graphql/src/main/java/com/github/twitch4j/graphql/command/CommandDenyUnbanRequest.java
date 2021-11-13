package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.DenyUnbanRequestMutation;
import com.github.twitch4j.graphql.internal.type.DenyUnbanRequestInput;
import lombok.NonNull;

public class CommandDenyUnbanRequest extends BaseCommand<DenyUnbanRequestMutation.Data> {
    private final String id;
    private final String message;

    /**
     * Constructor
     *
     * @param apolloClient Apollo Client
     * @param id           ID of the unban request to be resolved
     * @param message      Optional message from the resolver to be shown to the unban requester
     */
    public CommandDenyUnbanRequest(@NonNull ApolloClient apolloClient, @NonNull String id, String message) {
        super(apolloClient);
        this.id = id;
        this.message = message;
    }

    @Override
    protected ApolloCall<DenyUnbanRequestMutation.Data> getGraphQLCall() {
        return apolloClient.mutate(
            DenyUnbanRequestMutation.builder()
                .input(
                    DenyUnbanRequestInput.builder()
                        .id(id)
                        .resolverMessage(message)
                        .build()
                )
                .build()
        );
    }
}
