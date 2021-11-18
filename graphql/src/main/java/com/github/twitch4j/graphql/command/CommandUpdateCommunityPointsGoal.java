package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.UpdateCommunityPointsCommunityGoalMutation;
import com.github.twitch4j.graphql.internal.type.UpdateCommunityPointsCommunityGoalInput;
import lombok.NonNull;

public class CommandUpdateCommunityPointsGoal extends BaseCommand<UpdateCommunityPointsCommunityGoalMutation.Data> {
    private final UpdateCommunityPointsCommunityGoalInput input;

    /**
     * Constructor
     *
     * @param apolloClient Apollo Client
     * @param input        The updated settings for this community points goal. channelID and goalID must be specified
     */
    public CommandUpdateCommunityPointsGoal(@NonNull ApolloClient apolloClient, @NonNull UpdateCommunityPointsCommunityGoalInput input) {
        super(apolloClient);
        this.input = input;
    }

    @Override
    protected ApolloCall<UpdateCommunityPointsCommunityGoalMutation.Data> getGraphQLCall() {
        return apolloClient.mutate(
            UpdateCommunityPointsCommunityGoalMutation.builder()
                .input(input)
                .build()
        );
    }
}
