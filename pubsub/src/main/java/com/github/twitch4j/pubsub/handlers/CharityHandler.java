package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.events.TwitchEvent;
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
    public TwitchEvent apply(Args args) {
        switch (args.getType()) {
            case "charity_campaign_donation":
                CharityDonationData donation = TypeConvert.jsonToObject(args.getRawMessage(), CharityDonationData.class);
                return new CharityCampaignDonationEvent(args.getLastTopicPart(), donation);
            case "charity_campaign_status":
                CharityCampaignStatus status = TypeConvert.jsonToObject(args.getRawMessage(), CharityCampaignStatus.class);
                return new CharityCampaignStatusEvent(args.getLastTopicPart(), status);
            default:
                return null;
        }
    }
}
