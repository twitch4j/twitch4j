package me.philippheuer.twitch4j.message.irc.parsers.commands;

import lombok.Getter;

import java.util.Map;

@Getter
public class UserState implements Parse {
    private final Map tags;
    private final String channel;

    public UserState(Map tags) {
        this.tags = tags;
        this.channel = null;
    }

    public UserState(Map tags, String channel) {
        this.tags = tags;
        this.channel = channel;
    }

    @Override
    public String toString() {
        return String.format("[%s] @%s%s", getClass().getSimpleName(), tags.toString(), (channel != null) ? " " + channel : "");
    }
}
