package io.twitch4j.events.tmi;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.twitch4j.events.Event;
import io.twitch4j.impl.events.tmi.ChannelEventBuilder;
import io.twitch4j.tmi.channel.IChannel;

@JsonDeserialize(builder = ChannelEventBuilder.class)
public abstract class ChannelEvent extends Event {
    public abstract IChannel getChannel();
}
