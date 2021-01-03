package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.UnfollowMutation;
import com.github.twitch4j.graphql.internal.type.UnfollowUserInput;

/**
 * Unfollow User
 */
public class CommandUnfollowUser extends BaseCommand<UnfollowMutation.Data> {

    private final Long targetUserId;

    public CommandUnfollowUser(ApolloClient apolloClient, Long targetUserId) {
        super(apolloClient);
        this.targetUserId = targetUserId;
    }

    @Override
    protected ApolloCall<UnfollowMutation.Data> getGraphQLCall() {
        ApolloCall<UnfollowMutation.Data> apolloCall = apolloClient.mutate(
            UnfollowMutation.builder()
                .unfollowUserInput(
                    UnfollowUserInput.builder()
                        .targetID(targetUserId.toString())
                        .build()
                )
                .build()
        );

        return apolloCall;
    }

}
