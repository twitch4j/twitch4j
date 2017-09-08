package me.philippheuer.twitch4j.message.irc.parsers.commands;

import lombok.Getter;
import me.philippheuer.twitch4j.message.commands.CommandPermission;
import me.philippheuer.twitch4j.message.irc.parsers.commands.messages.ArgumentMessage;
import me.philippheuer.twitch4j.message.irc.parsers.commands.messages.DefaultMessage;
import me.philippheuer.twitch4j.message.irc.parsers.commands.messages.PrivateMessage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
public class Message<T extends ArgumentMessage> implements Parse {
    private final Map tags;
    private final String sender;
    private final T arguments;
    private final Set<CommandPermission> permissions = new HashSet<CommandPermission>();

    @SuppressWarnings("unchecked")
    public Message(Map tags, String sender, String[] args) {
        this.tags = tags;
        this.sender = sender;
        if (args[0].startsWith("#")) {
            this.arguments = (T) new DefaultMessage(args);
        } else this.arguments = (T) new PrivateMessage(args);
        boolean channelOwner = (tags.get("user-id") == tags.get("room-id"));

        // Check for Permissions
		if (tags.containsKey("badges")) {
			Map<String, String> badges = (Map) tags.get("badges");

			if(badges != null) {
				// - Broadcaster
				if (badges.containsKey("broadcaster") && channelOwner) {
					permissions.add(CommandPermission.BROADCASTER);
					permissions.add(CommandPermission.MODERATOR);
				}
				// Twitch Prime
				if (badges.containsKey("premium")) {
					permissions.add(CommandPermission.PRIME_TURBO);
				}
				// Moderator
				if (badges.containsKey("moderator")) {
					permissions.add(CommandPermission.MODERATOR);
				}
				// Partner
				if (badges.containsKey("partner")) {
					permissions.add(CommandPermission.PARTNER);
				}
			}
		}
		// Twitch Turbo
		if (tags.containsKey("turbo") && (boolean) tags.get("turbo")) {
			permissions.add(CommandPermission.PRIME_TURBO);
		}
		// Subscriber
		if (tags.containsKey("subscriber") && (boolean) tags.get("subscriber")) {
			permissions.add(CommandPermission.SUBSCRIBER);
		}
		// Everyone
		permissions.add(CommandPermission.EVERYONE);
    }

    @Override
    public String toString() {
        return String.format("[%s] @%s %s", getClass().getSimpleName(), tags, arguments.toString());
    }

}
