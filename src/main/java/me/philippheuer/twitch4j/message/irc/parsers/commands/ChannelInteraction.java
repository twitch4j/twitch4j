package me.philippheuer.twitch4j.message.irc.parsers.commands;

import lombok.Getter;

@Getter
public class ChannelInteraction implements Parse {

    private final String user;
    private final String channel;
    /**
	 * True: user join the channel
     * False: user leaving channel
     */
    private final boolean action;

    public ChannelInteraction(String command, String channel, String sender) {
        if (channel.startsWith("#")) channel = channel.substring(1);

        this.channel = channel;
        this.user = sender;
        this.action = command.equals("JOIN");
    }

    @Override
    public String toString() {
        return String.format("[%s] @%s %s #%s", getClass().getSimpleName(), user, (action) ? "joined" : "left", channel);
    }
}
