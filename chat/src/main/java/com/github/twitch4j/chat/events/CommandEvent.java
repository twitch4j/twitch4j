package com.github.twitch4j.chat.events;

import com.github.twitch4j.chat.commands.CommandPermission;
import com.github.twitch4j.chat.domain.ChatUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

import java.util.Set;

/**
 * This event gets called when a message is received in a channel.
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class CommandEvent extends TwitchEvent {

    /**
     * Source: channel / privateMessage
     */
    private String source;

    /**
     * Source Id: channelName or userName
     */
    private String sourceId;

    /**
     * User
     */
    private ChatUser user;

    /**
     * Command Prefix
     */
    private String commandPrefix;

    /**
     * Command
     */
    private String command;

    /**
     * Permissions of the user
     */
    private Set<CommandPermission> permissions;

    /**
     * Event Constructor
     *
     * @param source        Source (used for response method)
     * @param sourceId      Source Id (used for response method)
     * @param user          The user who triggered the event.
     * @param commandPrefix The command prefix used.
     * @param command       The plain command without prefix.
     * @param permissions   The permissions of the triggering user.
     */
    public CommandEvent(String source, String sourceId, ChatUser user, String commandPrefix, String command, Set<CommandPermission> permissions) {
        super();
        this.source = source;
        this.sourceId = sourceId;
        this.user = user;
        this.commandPrefix = commandPrefix;
        this.command = command;
        this.permissions = permissions;
    }

    /**
     * Respond to the command origin (channel or private)
     *
     * @param message Message
     */
    public void respondToUser(String message) {
        getTwitchChat().sendMessage(sourceId, message);
    }
}
