package me.philippheuer.twitch4j.message.irc.parsers.commands;

import lombok.AccessLevel;
import lombok.Getter;
import me.philippheuer.twitch4j.message.irc.parsers.commands.messages.ClearChatMessage;

import java.util.Map;

@Getter
public class ModMessage implements Parse {
    @Getter(AccessLevel.NONE)
    private final ClearChatMessage arguments;
    private final Map tags;

    public ModMessage(Map tags, String[] args) {
        this.tags = tags;
        arguments = new ClearChatMessage(args);
    }

    public String getChannel() {
        return arguments.getChannel();
    }

    public String getUser() {
        return arguments.getUser();
    }
    @Override
    public String toString() {
        return String.format("[%s] %s", getClass().getSimpleName(), arguments.toString());
    }
}
