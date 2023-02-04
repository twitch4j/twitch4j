package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelCharityCampaignCondition;
import com.github.twitch4j.eventsub.events.ChannelCharityDonateEvent;

/**
 * Channel Charity Campaign Donate
 * <p>
 * Sends an event notification when a user donates to the broadcaster’s charity campaign.
 *
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_CHARITY_READ
 */
public class ChannelCharityDonateType implements SubscriptionType<ChannelCharityCampaignCondition, ChannelCharityCampaignCondition.ChannelCharityCampaignConditionBuilder<?, ?>, ChannelCharityDonateEvent> {

    @Override
    public String getName() {
        return "channel.charity_campaign.donate";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public ChannelCharityCampaignCondition.ChannelCharityCampaignConditionBuilder<?, ?> getConditionBuilder() {
        return ChannelCharityCampaignCondition.builder();
    }

    @Override
    public Class<ChannelCharityDonateEvent> getEventClass() {
        return ChannelCharityDonateEvent.class;
    }

}
