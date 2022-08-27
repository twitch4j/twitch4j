package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.CharityCampaignStatus;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class CharityCampaignStatusEvent extends TwitchEvent {
    String channelId;
    CharityCampaignStatus data;
}
