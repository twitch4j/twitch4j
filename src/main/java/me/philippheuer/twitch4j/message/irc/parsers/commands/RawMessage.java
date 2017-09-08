package me.philippheuer.twitch4j.message.irc.parsers.commands;

import lombok.Getter;

@Getter
public class RawMessage implements Parse {
    private final String rawMessage;

    public RawMessage(String rawMessage) {
        this.rawMessage = rawMessage;
    }

    @Override
    public String toString() {
        return rawMessage;
    }
}
