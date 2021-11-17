package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.CreateModeratorCommentMutation;
import com.github.twitch4j.graphql.internal.type.CreateModeratorCommentInput;
import lombok.NonNull;

public class CommandCreateModComment extends BaseCommand<CreateModeratorCommentMutation.Data> {
    private final String channelId;
    private final String targetId;
    private final String text;

    /**
     * Constructor
     *
     * @param apolloClient Apollo Client
     * @param channelId    The id of the channel where the moderator comment should be created
     * @param targetId     The id of the target user of the moderator comment
     * @param text         The body of the comment
     */
    public CommandCreateModComment(@NonNull ApolloClient apolloClient, @NonNull String channelId, @NonNull String targetId, @NonNull String text) {
        super(apolloClient);
        this.channelId = channelId;
        this.targetId = targetId;
        this.text = text;
    }

    @Override
    protected ApolloCall<CreateModeratorCommentMutation.Data> getGraphQLCall() {
        return apolloClient.mutate(
            CreateModeratorCommentMutation.builder()
                .createModeratorCommentInput(
                    CreateModeratorCommentInput.builder()
                        .channelID(channelId)
                        .targetID(targetId)
                        .text(text)
                        .build()
                )
                .build()
        );
    }
}
