package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.CreateClipMutation;
import com.github.twitch4j.graphql.internal.type.CreateClipInput;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

public class CommandCreateClip extends BaseCommand<CreateClipMutation.Data> {
    private final String channelId;
    private final Double offsetSeconds;
    private final String broadcastId;
    private final String videoId;

    /**
     * Constructor
     *
     * @param apolloClient  Apollo Client
     * @param channelId     The id of the channel to be clipped
     * @param offsetSeconds The number of seconds into the stream or broadcast to start the clip at
     * @param broadcastId   The id of the broadcast to clip from
     * @param videoId       The id of the video to clip from, if not clipping a live broadcast
     */
    public CommandCreateClip(@NonNull ApolloClient apolloClient, @NonNull String channelId, @NonNull Double offsetSeconds, @Nullable String broadcastId, @Nullable String videoId) {
        super(apolloClient);
        this.channelId = channelId;
        this.offsetSeconds = offsetSeconds;
        this.broadcastId = broadcastId;
        this.videoId = videoId;
    }

    @Override
    protected ApolloCall<CreateClipMutation.Data> getGraphQLCall() {
        return apolloClient.mutate(
            CreateClipMutation.builder()
                .input(
                    CreateClipInput.builder()
                        .broadcasterID(channelId)
                        .offsetSeconds(offsetSeconds)
                        .broadcastID(broadcastId)
                        .videoID(videoId)
                        .build()
                )
                .build()
        );
    }
}
