package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.DeleteClipsMutation;
import com.github.twitch4j.graphql.internal.type.DeleteClipsInput;

import java.util.List;

public class CommandDeleteClips extends BaseCommand<DeleteClipsMutation.Data> {
    private final List<String> slugs;

    /**
     * Constructor
     *
     * @param apolloClient Apollo Client
     * @param slugs        The list of clip slugs to be deleted
     */
    public CommandDeleteClips(ApolloClient apolloClient, List<String> slugs) {
        super(apolloClient);
        this.slugs = slugs;
    }

    @Override
    protected ApolloCall<DeleteClipsMutation.Data> getGraphQLCall() {
        return apolloClient.mutate(
            DeleteClipsMutation.builder()
                .deleteClipsInput(
                    DeleteClipsInput.builder()
                        .slugs(slugs)
                        .build()
                )
                .build()
        );
    }
}
