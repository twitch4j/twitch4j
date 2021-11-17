package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.UpdateCommunityPointsCustomRewardRedemptionStatusesByRedemptionsMutation;
import com.github.twitch4j.graphql.internal.type.CommunityPointsCustomRewardRedemptionStatus;
import com.github.twitch4j.graphql.internal.type.UpdateCommunityPointsCustomRewardRedemptionStatusesByRedemptionsInput;
import lombok.NonNull;

import java.util.List;

public class CommandUpdateCustomRedemptionStatuses extends BaseCommand<UpdateCommunityPointsCustomRewardRedemptionStatusesByRedemptionsMutation.Data> {
    private final String channelId;
    private final List<String> redemptionIds;
    private final CommunityPointsCustomRewardRedemptionStatus oldStatus;
    private final CommunityPointsCustomRewardRedemptionStatus newStatus;

    /**
     * Constructor
     *
     * @param apolloClient  Apollo Client
     * @param channelId     The channel ID the redemptions were made in
     * @param redemptionIds The redemption IDs to update
     * @param oldStatus     The old status redemptions are currently set to
     * @param newStatus     The new status to set the redemptions to
     */
    public CommandUpdateCustomRedemptionStatuses(@NonNull ApolloClient apolloClient, @NonNull String channelId, @NonNull List<String> redemptionIds,
                                                 @NonNull CommunityPointsCustomRewardRedemptionStatus oldStatus, @NonNull CommunityPointsCustomRewardRedemptionStatus newStatus) {
        super(apolloClient);
        this.channelId = channelId;
        this.redemptionIds = redemptionIds;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
    }

    @Override
    protected ApolloCall<UpdateCommunityPointsCustomRewardRedemptionStatusesByRedemptionsMutation.Data> getGraphQLCall() {
        return apolloClient.mutate(
            UpdateCommunityPointsCustomRewardRedemptionStatusesByRedemptionsMutation.builder()
                .input(
                    UpdateCommunityPointsCustomRewardRedemptionStatusesByRedemptionsInput.builder()
                        .channelID(channelId)
                        .newStatus(newStatus)
                        .oldStatus(oldStatus)
                        .redemptionIDs(redemptionIds)
                        .build()
                )
                .build()
        );
    }
}
