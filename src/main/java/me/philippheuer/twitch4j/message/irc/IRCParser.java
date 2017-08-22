package me.philippheuer.twitch4j.message.irc;

import lombok.Getter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.model.Channel;
import me.philippheuer.twitch4j.model.User;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IRCParser {

	@Getter
	private final TwitchClient twitchClient;
	private final Matcher message;

	public IRCParser(TwitchClient client, String message) {
		this.twitchClient = client;
		final Pattern MESSAGE_REGEX = Pattern.compile("^(?:@(.*?) )?(?::(.+?)(?:!(.+?))?(?:@(.+?))? )?((?:[A-z]+)|(?:[0-9]{3}))(?: (?!:)(.+?))?(?: :(.*))?$");
		final Matcher matcher = MESSAGE_REGEX.matcher(message);
		matcher.find();
		this.message = matcher;
	}

//	@Override
//	public String toString() {
//		return message.group();
//	}

	private String shapeParser() {
		if (getCommand().equalsIgnoreCase("PRIVMSG"))
			return String.format("[%s] #%s | %s: %s", getCommand(), getChannel().getName(), getSender().getName(), getMessage());
		else if (getCommand().equalsIgnoreCase("WHISPER"))
			return String.format("[%s] %s: %s", getCommand(), getSender().getName(), getMessage());
		else if (getCommand().equalsIgnoreCase("JOIN") || getCommand().equalsIgnoreCase("PART"))
            return String.format("[%s] #%s | @%s", getCommand(), getChannel().getName(), getSender().getName());
        else if (getCommand().equalsIgnoreCase("ROOMSTATE") || getCommand().equalsIgnoreCase("USERSTATE"))
            return String.format("[%s] #%s @T=%s", getCommand(), getChannel().getName(), getTags().toString());
        else return String.format("[%s]", getCommand());

//		StringBuilder builder = new StringBuilder();
//		builder.append("[")
//				.append(message.group(5))
//				.append("] ")
//				.append((message.group(5).equalsIgnoreCase("PRIVMSG")) ? getChannel().getName() + " | " : null)
//				.append(getSender().getName())
//				.append(": ")
//				.append(message.group(7));
//		System.out.println(builder.toString());
//		return builder.toString();
	}

	@Override
    public String toString() {
        return shapeParser();
    }

	/**
	 * Replaying message to the last sender where received message
	 * @param message message
	 */
	public void replay(String message) {
		if (getCommand().equalsIgnoreCase("PRIVMSG")) {
			twitchClient.getMessageInterface().sendMessage(getChannel().getName(), message);
		} else if (getCommand().equalsIgnoreCase("WHISPER")) {
			twitchClient.getMessageInterface().sendPrivateMessage(getSender().getName(), message);
		}
	}

	public <T extends Object> Map<String, T> getTags() {
		Map<String, T> tagList = new HashMap<>();
		String rawTags = message.group(1);
		for (String tagData: rawTags.split(";")) {
		    String key = tagData.split("=")[0];
		    T value = parseTagValue(tagData.split("=")[0], tagData.split("=")[1]);
			tagList.put(key, value);
		}
		return tagList;
	}

	private <T extends Object> T parseTagValue(String key, String value) {
        if ((value.contains(",") || value.contains("/")) && key.equalsIgnoreCase("badges")) {
            Map<String, Integer> badges = new HashMap<String, Integer>();
            for (String badge : value.split(",")) {
                badges.put(badge.split("/")[0], Integer.parseInt(badge.split("/")[1]));
            }
            return (T) badges;
        } else if ((value.contains("/") || value.contains(":")) && key.equalsIgnoreCase("emotes")) {
            Map<Integer, Map.Entry<Integer, Integer>> emotes = new HashMap<Integer, Map.Entry<Integer, Integer>>();
            for (String emote : value.split("/")) {
                emotes.put(Integer.parseInt(emote.split(":")[0]), new AbstractMap.SimpleEntry<Integer, Integer>(Integer.parseInt(emote.split(":")[1].split("-")[0]), Integer.parseInt(emote.split(":")[1].split("-")[1])));
            }
            return (T) emotes;
        } else if (key.equalsIgnoreCase("subscriber") || key.equalsIgnoreCase("mod") || key.equalsIgnoreCase("turbo"))
            return (T) Boolean.valueOf(value.equals("1"));
        else if (isInt(value)) return (T) Integer.valueOf(value);
        else return  (T) value;
    }

	private boolean isInt(String numbers) {
		try {
			Integer.parseInt(numbers);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public String getRawMessage() { return message.group(0); }

	public <T extends Object> boolean hasTag(String name) {
		Map<String, T> tags = getTags();
		return !tags.isEmpty() && tags.containsKey(name);
	}

	public <T extends Object> T getTag(String name) {
		return (T) getTags().get(name);
	}

	public User getSender() {
	    long userId;
	    if (getCommand().equalsIgnoreCase("JOIN") || getCommand().equalsIgnoreCase("PART"))
	        userId = twitchClient.getUserEndpoint().getUserIdByUserName(message.group(3)).get();
		else if (hasTag("user-id"))
		    userId = Long.parseLong(getTag("user-id"));
		else return null;
		Optional<User> sender = twitchClient.getUserEndpoint().getUser(userId);
		return sender.orElse(null);
	}

	public Channel getChannel() {
		if (message.group(6).startsWith("#"))
			return twitchClient.getChannelEndpoint(message.group(6).substring(1)).getChannel();
		else return null;
	}

	public String getCommand() {
		return message.group(5);
	}

	public String getMessage() {
		return message.group(7);
	}
}
