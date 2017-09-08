package me.philippheuer.twitch4j.message.irc.parsers.commands.messages;

import lombok.Getter;

@Getter
public class ClearChatMessage implements ArgumentMessage {

    private final String channel;
    private final String user;

    public ClearChatMessage(String[] args) {
        channel = args[0].substring(1);
        user = (args.length > 1) ? args[1] : null;
    }

    @Override
    public String toString() {
        return String.format("#%s%s", channel, (user != null) ? " -> " + user : "");
    }
}
