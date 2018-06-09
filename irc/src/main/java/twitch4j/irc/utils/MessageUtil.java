package twitch4j.irc.utils;

import twitch4j.irc.chat.message.Message;
import twitch4j.irc.chat.message.MessageCommand;

public class MessageUtil {
	public static MessageCommand parseCommand(String cmd) {
		switch (cmd) {
			case "PRIVMSG":
				return MessageCommand.PRIV_MSG;
			case "NOTICE":
				return MessageCommand.NOTICE;
			case "PING":
				return MessageCommand.PING;
			case "PONG":
				return MessageCommand.PONG;
			case "HOSTTARGET":
				return MessageCommand.HOST_TARGET;
			case "CLEARCHAT":
				return MessageCommand.CLEAR_CHAT;
			case "USERSTATE":
				return MessageCommand.USER_STATE;
			case "GLOBALUSERSTATE":
				return MessageCommand.GLOBAL_USER_STATE;
			case "NICK":
				return MessageCommand.NICK;
			case "JOIN":
				return MessageCommand.JOIN;
			case "PART":
				return MessageCommand.PART;
			case "PASS":
				return MessageCommand.PASS;
			case "CAP":
				return MessageCommand.CAP;
			case "001":
				return MessageCommand.RPL_001;
			case "002":
				return MessageCommand.RPL_002;
			case "003":
				return MessageCommand.RPL_003;
			case "004":
				return MessageCommand.RPL_004;
			case "353":
				return MessageCommand.RPL_353;
			case "366":
				return MessageCommand.RPL_366;
			case "372":
				return MessageCommand.RPL_372;
			case "375":
				return MessageCommand.RPL_375;
			case "376":
				return MessageCommand.RPL_376;
			case "WHISPER":
				return MessageCommand.WHISPER;
			case "SERVERCHANGE":
				return MessageCommand.SERVER_CHANGE;
			case "RECONNECT":
				return MessageCommand.RECONNECT;
			case "ROOMSTATE":
				return MessageCommand.ROOM_STATE;
			case "USERNOTICE":
				return MessageCommand.USER_NOTICE;
			default:
				return MessageCommand.UNKNOWN;
		}
	}

	public static String stringify(Message message) {
		StringBuilder sb = new StringBuilder();
		if (!message.getTags().isEmpty()) {
			sb.append("@").append(message.getTags()).append(" ");
		}
		sb.append(":").append(message.getHostmask().toString()).append(" ")
				.append(message.getCommand().name()
						.replace("RPL_", ""));
		if (!isBlank(message.getParameters())) {
			sb.append(" ").append(message.getParameters());
		}
		if (!isBlank(message.getMessage())) {
			sb.append(" :").append(message);
		}
		return sb.toString();
	}

	public static boolean isBlank(String s) {
		return s == null || s.length() == 0;
	}
}
