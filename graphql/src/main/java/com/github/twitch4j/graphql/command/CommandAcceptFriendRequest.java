package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.AcceptFriendRequestMutation;
import com.github.twitch4j.graphql.internal.type.AcceptFriendRequestInput;
import lombok.NonNull;

public class CommandAcceptFriendRequest extends BaseCommand<AcceptFriendRequestMutation.Data> {
    private final String targetId;

    /**
     * Constructor
     *
     * @param apolloClient Apollo Client
     * @param targetId     The authenticated user will accept the friend request sent by the user with an ID equal to targetID
     */
    public CommandAcceptFriendRequest(@NonNull ApolloClient apolloClient, @NonNull String targetId) {
        super(apolloClient);
        this.targetId = targetId;
    }

    @Override
    protected ApolloCall<AcceptFriendRequestMutation.Data> getGraphQLCall() {
        return apolloClient.mutate(
            AcceptFriendRequestMutation.builder()
                .input(
                    AcceptFriendRequestInput.builder()
                        .targetID(targetId)
                        .build()
                )
                .build()
        );
    }
}
