package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.pubsub.domain.CreatedUnbanRequest;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ChannelUnbanRequestCreateEvent extends UnbanRequestEvent {

    public ChannelUnbanRequestCreateEvent(String userId, String channelId, CreatedUnbanRequest unbanRequest) {
        super(userId, channelId, unbanRequest);
    }

    @Override
    public CreatedUnbanRequest getUnbanRequest() {
        return (CreatedUnbanRequest) super.getUnbanRequest();
    }

}
