package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.BanUserFromChatRoomMutation;
import com.github.twitch4j.graphql.internal.type.BanUserFromChatRoomInput;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

public class CommandBanUser extends BaseCommand<BanUserFromChatRoomMutation.Data> {
    private final String channelId;
    private final String targetUserLogin;
    private final String reason;

    /**
     * Constructor
     *
     * @param apolloClient    Apollo Client
     * @param channelId       The id of the channel to ban the user from
     * @param targetUserLogin The login name of the user to be banned
     * @param reason          An optional ban reason
     */
    public CommandBanUser(@NonNull ApolloClient apolloClient, @NonNull String channelId, @NonNull String targetUserLogin, @Nullable String reason) {
        super(apolloClient);
        this.channelId = channelId;
        this.targetUserLogin = targetUserLogin;
        this.reason = reason;
    }

    @Override
    protected ApolloCall<BanUserFromChatRoomMutation.Data> getGraphQLCall() {
        return apolloClient.mutate(
            BanUserFromChatRoomMutation.builder()
                .input(
                    BanUserFromChatRoomInput.builder()
                        .channelID(channelId)
                        .bannedUserLogin(targetUserLogin)
                        .reason(reason)
                        .build()
                )
                .build()
        );
    }
}
