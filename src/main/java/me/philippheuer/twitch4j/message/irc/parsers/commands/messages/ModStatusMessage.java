package me.philippheuer.twitch4j.message.irc.parsers.commands.messages;

import lombok.Getter;

@Getter
public class ModStatusMessage implements ArgumentMessage {
    private final String channel;
    private final String user;
    private final boolean mod;

    public ModStatusMessage(String[] args) {
        channel = args[0].substring(1);
        mod = args[1].equals("+o");
        user = args[2];
    }

    @Override
    public String toString() {
        return String.format("#%s %s %s", channel, (mod) ? "+" : "-", user);
    }
}
