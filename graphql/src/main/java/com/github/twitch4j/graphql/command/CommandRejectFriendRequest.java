package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.RejectFriendRequestMutation;
import com.github.twitch4j.graphql.internal.type.RejectFriendRequestInput;
import lombok.NonNull;

public class CommandRejectFriendRequest extends BaseCommand<RejectFriendRequestMutation.Data> {
    private final String targetId;

    /**
     * Constructor
     *
     * @param apolloClient Apollo Client
     * @param targetId     The authenticated user will reject the friend request sent by the user with an ID equal to targetID
     */
    public CommandRejectFriendRequest(@NonNull ApolloClient apolloClient, @NonNull String targetId) {
        super(apolloClient);
        this.targetId = targetId;
    }

    @Override
    protected ApolloCall<RejectFriendRequestMutation.Data> getGraphQLCall() {
        return apolloClient.mutate(
            RejectFriendRequestMutation.builder()
                .input(
                    RejectFriendRequestInput.builder()
                        .targetID(targetId)
                        .build()
                )
                .build()
        );
    }
}
