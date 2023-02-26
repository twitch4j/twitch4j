package com.github.twitch4j.eventsub.condition;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Tag("unittest")
public class EventSubConditionEqualsTest {

    @Test
    public void testFromToConditionEquality() {
        assertEquals(
            ChannelRaidCondition.builder().fromBroadcasterUserId("a").toBroadcasterUserId("b").build(),
            ChannelRaidCondition.builder().fromBroadcasterUserId("a").toBroadcasterUserId("b").build()
        );

        assertEquals(
            ChannelRaidCondition.builder().fromBroadcasterUserId("a").toBroadcasterUserId(null).build(),
            ChannelRaidCondition.builder().fromBroadcasterUserId("a").build()
        );

        assertEquals(
            ChannelRaidCondition.builder().fromBroadcasterUserId("a").toBroadcasterUserId("").build(),
            ChannelRaidCondition.builder().fromBroadcasterUserId("a").build()
        );

        assertEquals(
            ChannelRaidCondition.builder().fromBroadcasterUserId(null).toBroadcasterUserId("b").build(),
            ChannelRaidCondition.builder().fromBroadcasterUserId("").toBroadcasterUserId("b").build()
        );

        assertEquals(
            ChannelRaidCondition.builder().toBroadcasterUserId("b").build(),
            ChannelRaidCondition.builder().fromBroadcasterUserId("").toBroadcasterUserId("b").build()
        );

        assertNotEquals(
            ChannelRaidCondition.builder().toBroadcasterUserId("b").build(),
            ChannelRaidCondition.builder().toBroadcasterUserId("c").build()
        );
    }

    @Test
    public void testCampaignConditionEquality() {
        assertEquals(
            DropEntitlementGrantCondition.builder().organizationId("a").campaignId("b").categoryId("").build(),
            DropEntitlementGrantCondition.builder().organizationId("a").campaignId("b").build()
        );

        assertEquals(
            DropEntitlementGrantCondition.builder().organizationId("a").campaignId("").categoryId("").build(),
            DropEntitlementGrantCondition.builder().organizationId("a").build()
        );

        assertEquals(
            DropEntitlementGrantCondition.builder().organizationId("a").campaignId("").categoryId("c").build(),
            DropEntitlementGrantCondition.builder().organizationId("a").categoryId("c").build()
        );

        assertNotEquals(
            DropEntitlementGrantCondition.builder().organizationId("a").campaignId("b").categoryId("c").build(),
            DropEntitlementGrantCondition.builder().organizationId("a").categoryId("c").build()
        );
    }

    @Test
    public void testRewardConditionEquality() {
        assertEquals(
            ChannelPointsCustomRewardRedemptionAddCondition.builder().broadcasterUserId("a").rewardId("").build(),
            ChannelPointsCustomRewardRedemptionAddCondition.builder().broadcasterUserId("a").build()
        );

        assertNotEquals(
            ChannelPointsCustomRewardRedemptionAddCondition.builder().broadcasterUserId("a").rewardId("").build(),
            ChannelPointsCustomRewardRedemptionAddCondition.builder().broadcasterUserId("b").build()
        );

        assertNotEquals(
            ChannelPointsCustomRewardRedemptionAddCondition.builder().broadcasterUserId("a").rewardId("c").build(),
            ChannelPointsCustomRewardRedemptionAddCondition.builder().broadcasterUserId("a").build()
        );
    }

    @Test
    public void testModerationConditionEquality() {
        assertEquals(
            ShieldModeCondition.builder().broadcasterUserId("a").moderatorUserId("").build(),
            ShieldModeCondition.builder().broadcasterUserId("a").moderatorUserId(null).build()
        );

        assertNotEquals(
            ShieldModeCondition.builder().broadcasterUserId("a").moderatorUserId("b").build(),
            ShieldModeCondition.builder().broadcasterUserId("a").moderatorUserId(null).build()
        );
    }

}
