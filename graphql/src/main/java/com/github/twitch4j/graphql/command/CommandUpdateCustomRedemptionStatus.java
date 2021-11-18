package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.UpdateCommunityPointsCustomRewardRedemptionStatusMutation;
import com.github.twitch4j.graphql.internal.type.CommunityPointsCustomRewardRedemptionStatus;
import com.github.twitch4j.graphql.internal.type.UpdateCommunityPointsCustomRewardRedemptionStatusInput;
import lombok.NonNull;

public class CommandUpdateCustomRedemptionStatus extends BaseCommand<UpdateCommunityPointsCustomRewardRedemptionStatusMutation.Data> {
    private final String channelId;
    private final String redemptionId;
    private final CommunityPointsCustomRewardRedemptionStatus newStatus;

    /**
     * Constructor
     *
     * @param apolloClient Apollo Client
     * @param channelId    The channel ID the redemption was made in
     * @param redemptionId The redemption ID
     * @param newStatus    The new status to set the redemption to
     */
    public CommandUpdateCustomRedemptionStatus(@NonNull ApolloClient apolloClient, @NonNull String channelId, @NonNull String redemptionId,
                                               @NonNull CommunityPointsCustomRewardRedemptionStatus newStatus) {
        super(apolloClient);
        this.channelId = channelId;
        this.redemptionId = redemptionId;
        this.newStatus = newStatus;
    }

    @Override
    protected ApolloCall<UpdateCommunityPointsCustomRewardRedemptionStatusMutation.Data> getGraphQLCall() {
        return apolloClient.mutate(
            UpdateCommunityPointsCustomRewardRedemptionStatusMutation.builder()
                .input(
                    UpdateCommunityPointsCustomRewardRedemptionStatusInput.builder()
                        .channelID(channelId)
                        .redemptionID(redemptionId)
                        .newStatus(newStatus)
                        .build()
                )
                .build()
        );
    }
}
