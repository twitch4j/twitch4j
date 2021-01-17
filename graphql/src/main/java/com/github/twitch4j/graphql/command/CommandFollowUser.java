package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.FollowMutation;
import com.github.twitch4j.graphql.internal.type.FollowUserInput;

/**
 * Follow User
 */
public class CommandFollowUser extends BaseCommand<FollowMutation.Data> {

    private final Long targetUserId;

    private final Boolean goLiveNotification;

    public CommandFollowUser(ApolloClient apolloClient, Long targetUserId, Boolean goLiveNotification) {
        super(apolloClient);
        this.targetUserId = targetUserId;
        this.goLiveNotification = goLiveNotification;
    }

    @Override
    protected ApolloCall<FollowMutation.Data> getGraphQLCall() {
        ApolloCall<FollowMutation.Data> apolloCall = apolloClient.mutate(
            FollowMutation.builder()
                .followUserInput(
                    FollowUserInput.builder()
                        .targetID(targetUserId.toString())
                        .disableNotifications(goLiveNotification ? false : true)
                        .build()
                )
                .build()
        );

        return apolloCall;
    }

}
