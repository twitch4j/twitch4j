package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.DeleteCommunityPointsCommunityGoalMutation;
import com.github.twitch4j.graphql.internal.type.DeleteCommunityPointsCommunityGoalInput;
import lombok.NonNull;

public class CommandDeleteCommunityPointsGoal extends BaseCommand<DeleteCommunityPointsCommunityGoalMutation.Data> {
    private final String channelId;
    private final String goalId;

    /**
     * Constructor
     *
     * @param apolloClient Apollo Client
     * @param channelId    The channel
     * @param goalId       The community goal
     */
    public CommandDeleteCommunityPointsGoal(@NonNull ApolloClient apolloClient, @NonNull String channelId, @NonNull String goalId) {
        super(apolloClient);
        this.channelId = channelId;
        this.goalId = goalId;
    }

    @Override
    protected ApolloCall<DeleteCommunityPointsCommunityGoalMutation.Data> getGraphQLCall() {
        return apolloClient.mutate(
            DeleteCommunityPointsCommunityGoalMutation.builder()
                .input(
                    DeleteCommunityPointsCommunityGoalInput.builder()
                        .channelID(channelId)
                        .goalID(goalId)
                        .build()
                )
                .build()
        );
    }
}
