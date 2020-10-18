package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.pubsub.domain.UpdatedUnbanRequest;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ChannelUnbanRequestUpdateEvent extends UnbanRequestEvent {

    public ChannelUnbanRequestUpdateEvent(String userId, String channelId, UpdatedUnbanRequest unbanRequest) {
        super(userId, channelId, unbanRequest);
    }

    @Override
    public UpdatedUnbanRequest getUnbanRequest() {
        return (UpdatedUnbanRequest) super.getUnbanRequest();
    }

}
