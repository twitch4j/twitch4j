package me.philippheuer.twitch4j.message.irc.parsers;

import me.philippheuer.twitch4j.message.irc.TwitchChat;
import org.apache.commons.lang3.StringUtils;
import me.philippheuer.twitch4j.message.irc.parsers.commands.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Parser {
    private final List<String> namesList = new ArrayList<String>();
	private final TwitchChat chat;

    public Parser(TwitchChat chat) { // IRC
		this.chat = chat;
    }

    @SuppressWarnings("unchecked")
    public <T extends Parse> T parse(String rawMessage) {
        String rawTags = null;
        String address = null;
        String command = null;
        List<String> arguments = new ArrayList<String>();
        for (String msg: rawMessage.split(" ")) {
            if (msg.startsWith("@")) rawTags = msg.substring(1);
            else if (msg.startsWith(":") && address == null) address = msg;
            else if (msg.matches("^([A-Z]*|[0-9]{3})$") && command == null) command = msg;
            else if ((command.startsWith("CAP") && !command.endsWith("ACK")) ||
                    (msg.equals("*") && command.equals("CAP"))) // cap tags
                command += " " + msg;
            else arguments.add(msg);
        }

        Map tags = Tag.parse(rawTags);
        String sender = StringUtils.substringBetween(address, "!", "@"); // Using Apache Commons Lang to extract sender username
        String[] args = arguments.toArray(new String[arguments.size()]);
        T returned = (T) new RawMessage(rawMessage);
        switch (command.toUpperCase()) {
            case "PING":
                // send pong
                chat.sendPong(args[0]);
                break;
            case "JOIN":
            case "PART":
                returned = (T) new ChannelInteraction(command, args[0], sender);
                break;
            case "MODE":
                returned = (T) new Moderator(args);
                break;
            case "353":
                boolean msg1 = false;
                for (String arg : args) {
                    if (arg.startsWith(":")) msg1 = true;
                    if (msg1) namesList.add(arg.replace(":", ""));
                }
                break;
            case "366":
                final List<String> names = Collections.unmodifiableList(namesList);
                returned = (T) new Names(args[1], names);
                break;
            case "CLEARCHAT":
                returned = (T) new ModMessage(tags, args);
                break;
            case "GLOBALUSERSTATE":
                // irc.setConnection(Connection.CONNECTED);
                returned = (T) new UserState(tags);
                break;
            case "PRIVMSG":
                returned = (T) new Message(tags, sender, args);
                break;
            case "ROOMSTATE":
                returned = (T) new ChannelState(tags, args[0]);
                break;
            case "USERNOTICE":
                returned = (T) new Subscribe(tags, args);
                break;
            case "HOSTTARGET":
                returned = (T) new Host(args);
                break;
            case "USERSTATE":
                returned = (T) new UserState(tags, args[0]);
                break;
            case "NOTICE":
                returned = (T) new Notice(tags.get("msg-id").toString(), args);
                break;
            default:
                returned = (T) new RawMessage(rawMessage);
                break;
        }
        return returned;
    }
}
