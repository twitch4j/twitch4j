package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.FetchVideoCommentsQuery;

public class CommandFetchVideoComments extends BaseCommand<FetchVideoCommentsQuery.Data> {
    private final String channelId;
    private final String videoId;
    private final String id;
    private final String after;
    private final String before;
    private final Integer first;
    private final Integer last;

    /**
     * Constructor
     *
     * @param apolloClient Apollo Client
     * @param channelId    Channel ID
     * @param videoId      Video ID
     * @param id           Comments ID
     * @param after        After
     * @param before       Before
     * @param first        First
     * @param last         Last
     */
    public CommandFetchVideoComments(ApolloClient apolloClient, String channelId, String videoId, String id, String after, String before, Integer first, Integer last) {
        super(apolloClient);
        this.channelId = channelId;
        this.videoId = videoId;
        this.id = id;
        this.after = after;
        this.before = before;
        this.first = first;
        this.last = last;

        if (channelId == null && videoId == null && id == null)
            throw new NullPointerException("No key provided to fetch video comments!");
    }

    @Override
    protected ApolloCall<FetchVideoCommentsQuery.Data> getGraphQLCall() {
        return apolloClient.query(
            FetchVideoCommentsQuery.builder()
                .channelID(channelId)
                .videoID(videoId)
                .id(id)
                .after(after)
                .before(before)
                .first(first)
                .last(last)
                .build()
        );
    }
}
