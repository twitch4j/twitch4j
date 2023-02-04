package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelCharityCampaignCondition;
import com.github.twitch4j.eventsub.events.CharityCampaignProgressEvent;

/**
 * Sends notifications when progress is made towards the campaign’s goal or when the broadcaster changes the fundraising goal.
 * <p>
 * It’s possible to receive this event before the Start event.
 * <p>
 * The event data includes the charity’s name and logo but not its description and website. To get that information, subscribe to the Start event or call the Get Charity Campaign endpoint.
 * <p>
 * To get donation information, subscribe to the channel.charity_campaign.donate event.
 * <p>
 * Requires the channel:read:charity scope.
 */
public class CharityCampaignProgressType implements SubscriptionType<ChannelCharityCampaignCondition, ChannelCharityCampaignCondition.ChannelCharityCampaignConditionBuilder<?, ?>, CharityCampaignProgressEvent> {

    @Override
    public String getName() {
        return "channel.charity_campaign.progress";
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
    public Class<CharityCampaignProgressEvent> getEventClass() {
        return CharityCampaignProgressEvent.class;
    }

}
