package me.philippheuer.twitch4j.message.irc.parsers.commands;

import lombok.AccessLevel;
import lombok.Getter;
import me.philippheuer.twitch4j.message.irc.parsers.commands.messages.ArgumentMessage;

import java.util.Map;

@Getter
public class ChannelState implements Parse {

    private final Map tags;
    private final String channel;
    @Getter(AccessLevel.PRIVATE)
    private final ArgumentMessage arguments = null;

    public ChannelState(Map tags, String channel) {
        this.tags = tags;
        this.channel = channel.substring(1);
    }

    @Override
    public String toString() {
        return String.format("@%s [%s] #%s", tags.toString(), getClass().getSimpleName(), channel);
    }
}
