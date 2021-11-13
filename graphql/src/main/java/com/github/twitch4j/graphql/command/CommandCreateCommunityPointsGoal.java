package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.CreateCommunityPointsCommunityGoalMutation;
import com.github.twitch4j.graphql.internal.type.CreateCommunityPointsCommunityGoalInput;
import lombok.NonNull;

public class CommandCreateCommunityPointsGoal extends BaseCommand<CreateCommunityPointsCommunityGoalMutation.Data> {
    private final CreateCommunityPointsCommunityGoalInput input;

    /**
     * Constructor
     *
     * @param apolloClient Apollo Client
     * @param input        Create community goal input
     */
    public CommandCreateCommunityPointsGoal(@NonNull ApolloClient apolloClient, @NonNull CreateCommunityPointsCommunityGoalInput input) {
        super(apolloClient);
        this.input = input;
    }

    @Override
    protected ApolloCall<CreateCommunityPointsCommunityGoalMutation.Data> getGraphQLCall() {
        return apolloClient.mutate(
            CreateCommunityPointsCommunityGoalMutation.builder()
                .input(input)
                .build()
        );
    }
}
