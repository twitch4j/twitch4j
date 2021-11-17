package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.UpdateClipMutation;
import com.github.twitch4j.graphql.internal.type.UpdateClipInput;
import lombok.NonNull;

public class CommandUpdateClip extends BaseCommand<UpdateClipMutation.Data> {
    private final String slug;
    private final String newTitle;

    /**
     * Constructor
     *
     * @param apolloClient Apollo Client
     * @param slug         The slug of the clip to update
     * @param newTitle     The new title of the clip
     */
    public CommandUpdateClip(@NonNull ApolloClient apolloClient, @NonNull String slug, @NonNull String newTitle) {
        super(apolloClient);
        this.slug = slug;
        this.newTitle = newTitle;
    }

    @Override
    protected ApolloCall<UpdateClipMutation.Data> getGraphQLCall() {
        return apolloClient.mutate(
            UpdateClipMutation.builder()
                .updateClipInput(
                    UpdateClipInput.builder()
                        .slug(slug)
                        .title(newTitle)
                        .build()
                )
                .build()
        );
    }
}
