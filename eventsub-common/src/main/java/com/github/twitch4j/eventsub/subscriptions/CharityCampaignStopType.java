package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelCharityCampaignCondition;
import com.github.twitch4j.eventsub.events.CharityCampaignStopEvent;

/**
 * Sends a notification when the broadcaster stops a charity campaign.
 * <p>
 * The event data does not include information about the charity such as its name, description, and logo. To get that information, subscribe to the Start event or call the Get Charity Campaign endpoint.
 * <p>
 * Requires the channel:read:charity scope.
 */
public class CharityCampaignStopType implements SubscriptionType<ChannelCharityCampaignCondition, ChannelCharityCampaignCondition.ChannelCharityCampaignConditionBuilder<?, ?>, CharityCampaignStopEvent> {

    @Override
    public String getName() {
        return "channel.charity_campaign.stop";
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
    public Class<CharityCampaignStopEvent> getEventClass() {
        return CharityCampaignStopEvent.class;
    }

}
