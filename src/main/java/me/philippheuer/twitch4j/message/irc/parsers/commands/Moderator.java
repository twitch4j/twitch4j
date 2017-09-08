package me.philippheuer.twitch4j.message.irc.parsers.commands;

import lombok.AccessLevel;
import lombok.Getter;
import me.philippheuer.twitch4j.message.irc.parsers.commands.messages.ModStatusMessage;

import java.util.Map;

@Getter(AccessLevel.PRIVATE)
public class Moderator implements Parse {

    private final Map tags = null;
    private final ModStatusMessage arguments;

    public Moderator(String[] arguments) {
        this.arguments = new ModStatusMessage(arguments);
    }

    public boolean isModed() {
        return this.arguments.isMod();
    }

    public String getChannel() {
        return this.arguments.getChannel();
    }

    public String getUser() {
        return this.arguments.getUser();
    }

    @Override
    public String toString() {
        return String.format("[%s] #%s [%s] -> %s", getClass().getSimpleName(), getChannel(), (isModed()) ? "GRANT" : "REVOKE", getUser());
    }
}
