package me.philippheuer.twitch4j.message.irc.parsers.commands;

import lombok.AccessLevel;
import lombok.Getter;
import me.philippheuer.twitch4j.message.irc.parsers.commands.messages.HostMessage;

import java.util.Map;

@Getter(AccessLevel.PRIVATE)
public class Host implements Parse {

    private final Map tags = null;
    private final HostMessage arguments;

    public Host(String[] args) {
        arguments = new HostMessage(args);
    }

    public String getChannel() {
        return arguments.getChannel();
    }

    public String getHostedChannel() {
        return arguments.getHostedChannel();
    }

    public int getViewers() {
        return arguments.getViewers();
    }

    @Override
    public String toString() {
        return String.format("[%s] #s [%s] -> #%s", getClass().getSimpleName(), getChannel(), getViewers(), getHostedChannel());
    }
}
