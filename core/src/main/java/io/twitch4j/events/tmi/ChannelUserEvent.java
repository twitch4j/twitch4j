package io.twitch4j.events.tmi;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.twitch4j.impl.events.tmi.ChannelUserEventBuilder;
import io.twitch4j.tmi.IUser;

@JsonDeserialize(builder = ChannelUserEventBuilder.class)
public abstract class ChannelUserEvent extends ChannelEvent {
    public abstract IUser getUser();
}
