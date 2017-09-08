package me.philippheuer.twitch4j.message.irc.parsers.commands;

import lombok.Getter;

@Getter
public class Notice implements Parse {

    private final String channel;
    private final String message;
    private final String msgId;


    public Notice(String msgId, String[] args) {
        this.msgId = msgId.toUpperCase();
        this.channel = args[0];
        this.message = String.join(" ", args).replace(String.format("#%s :", channel), "");
    }

    @Override
    public String toString() {
        return String.format("[%s] @[%s] #%s :%s", getClass().getSimpleName(), msgId, channel, message);
    }
}
