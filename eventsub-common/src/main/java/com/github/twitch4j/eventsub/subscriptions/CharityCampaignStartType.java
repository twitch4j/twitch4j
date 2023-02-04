package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelCharityCampaignCondition;
import com.github.twitch4j.eventsub.events.CharityCampaignStartEvent;

/**
 * Sends a notification when the broadcaster starts a charity campaign.
 * <p>
 * It’s possible to receive this event after the Progress event.
 * <p>
 * Requires the channel:read:charity scope.
 */
public class CharityCampaignStartType implements SubscriptionType<ChannelCharityCampaignCondition, ChannelCharityCampaignCondition.ChannelCharityCampaignConditionBuilder<?, ?>, CharityCampaignStartEvent> {

    @Override
    public String getName() {
        return "channel.charity_campaign.start";
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
    public Class<CharityCampaignStartEvent> getEventClass() {
        return CharityCampaignStartEvent.class;
    }

}
