package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.ChannelTermsAction;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class ChannelTermsEvent extends TwitchEvent {

    /**
     * The ID for the channel where the channel terms action took place.
     */
    String channelId;

    /**
     * Data regarding the channel terms action that took place.
     */
    ChannelTermsAction data;

}
