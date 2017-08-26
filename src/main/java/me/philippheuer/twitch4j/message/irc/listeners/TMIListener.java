package me.philippheuer.twitch4j.message.irc.listeners;

import me.philippheuer.twitch4j.message.MessageInterface;
import me.philippheuer.twitch4j.message.irc.IRCParser;

import java.util.ArrayList;
import java.util.List;

public class TMIListener {

	private final MessageInterface tmi;
	private final List<ITMIListener> listeners = new ArrayList<ITMIListener>();

	public TMIListener(MessageInterface tmi) {
		this.tmi = tmi;
	}

	public void listen(IRCParser parser) {
		listeners.forEach(listener -> {
			boolean mode = false;
			List<String> chatters = new ArrayList<String>();
			switch (parser.getCommand().toUpperCase()) {
				case "ACTION":
				case "CLEARCHAT":
				case "JOIN":
				case "PART":
				case "PRIVMSG":
				case "GLOBALUSERSTATE":
				case "ROOMSTATE":
				case "USERNOTICE":
				case "USERSTATE":
				case "HOSTTARGET":
				case "NOTICE":
				case "RECONNECT":
				case "MODE":
					break;
				case "353":
					if (!mode) mode = true;
					break;
				case "366":
					if (parser.getMessage().equals("End of /NAMES list")) return;
					break;

			}
		});
	}
	public void listenConnection() {}

	public void addListener() {}
	public void removeListener() {}
}
