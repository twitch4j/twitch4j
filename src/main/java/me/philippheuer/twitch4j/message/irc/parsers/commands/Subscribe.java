package me.philippheuer.twitch4j.message.irc.parsers.commands;

import lombok.Getter;
import me.philippheuer.twitch4j.message.irc.parsers.commands.messages.DefaultMessage;

import java.util.Map;

@Getter
public class Subscribe implements Parse {
    private final Map tags;
    private final DefaultMessage arguments;

    public Subscribe(Map tags, String[] args) {
        this.tags = tags;
        this.arguments = new DefaultMessage(args);
    }

    @Override
    public String toString() {
        return String.format("[%s] @%s #%s", getClass().getSimpleName(), tags.toString(), arguments.getChannel() + ((arguments.getMessage().isEmpty()) ? "" : " :" + arguments.getMessage()));
    }
}
