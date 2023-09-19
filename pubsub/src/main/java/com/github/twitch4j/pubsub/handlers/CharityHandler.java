package com.github.twitch4j.pubsub.handlers;

import com.github.philippheuer.events4j.api.IEventManager;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.PubSubResponsePayloadMessage;
import com.github.twitch4j.pubsub.domain.CharityCampaignStatus;
import com.github.twitch4j.pubsub.domain.CharityDonationData;
import com.github.twitch4j.pubsub.events.CharityCampaignDonationEvent;
import com.github.twitch4j.pubsub.events.CharityCampaignStatusEvent;

import java.util.Collection;

class CharityHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "charity-campaign-donation-events-v1";
    }

    @Override
    public boolean handle(IEventManager eventManager, String[] topicParts, PubSubResponsePayloadMessage message, Collection<String> botOwnerIds) {
        switch (message.getType()) {
            case "charity_campaign_donation":
                CharityDonationData donation = TypeConvert.jsonToObject(message.getRawMessage(), CharityDonationData.class);
                eventManager.publish(new CharityCampaignDonationEvent(topicParts[topicParts.length - 1], donation));
                return true;
            case "charity_campaign_status":
                CharityCampaignStatus status = TypeConvert.jsonToObject(message.getRawMessage(), CharityCampaignStatus.class);
                eventManager.publish(new CharityCampaignStatusEvent(topicParts[topicParts.length - 1], status));
                return true;
            default:
                return false;
        }
    }
}
