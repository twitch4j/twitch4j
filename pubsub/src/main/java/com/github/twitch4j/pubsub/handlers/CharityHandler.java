package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.CharityCampaignStatus;
import com.github.twitch4j.pubsub.domain.CharityDonationData;
import com.github.twitch4j.pubsub.events.CharityCampaignDonationEvent;
import com.github.twitch4j.pubsub.events.CharityCampaignStatusEvent;

class CharityHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "charity-campaign-donation-events-v1";
    }

    @Override
    public boolean handle(Args args) {
        switch (args.getType()) {
            case "charity_campaign_donation":
                CharityDonationData donation = TypeConvert.jsonToObject(args.getRawMessage(), CharityDonationData.class);
                args.getEventManager().publish(new CharityCampaignDonationEvent(args.getLastTopicPart(), donation));
                return true;
            case "charity_campaign_status":
                CharityCampaignStatus status = TypeConvert.jsonToObject(args.getRawMessage(), CharityCampaignStatus.class);
                args.getEventManager().publish(new CharityCampaignStatusEvent(args.getLastTopicPart(), status));
                return true;
            default:
                return false;
        }
    }
}
