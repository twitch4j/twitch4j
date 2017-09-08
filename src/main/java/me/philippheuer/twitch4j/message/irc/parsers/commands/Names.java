package me.philippheuer.twitch4j.message.irc.parsers.commands;

import lombok.Getter;

import java.util.List;

@Getter
public class Names implements Parse {

    private final String channel;
    private final List<String> names;

    public Names(String channel, List<String> names) {
        this.channel = channel.substring(1);
        this.names = names;
    }

    @Override
    public String toString() {
        return String.format("[%s] #%s %s", getClass().getSimpleName(), channel, names.toString());
    }
}
