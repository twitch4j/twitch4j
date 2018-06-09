package twitch4j.irc.chat.message;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class TagsMessage extends LinkedHashMap<String, String> implements Map<String, String> {

	TagsMessage(Map<String, String> tags) {
		super(tags);
	}

	public String getAsString(String identifier) {
		return getOrDefault(identifier, "");
	}

	public int getAsInt(String identifier) {
		try {
			return Integer.decode(getOrDefault(identifier, "-1"));
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	long getAsLong(String identifier) {
		try {
			return Long.decode(getOrDefault(identifier, "-1"));
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	public boolean getAsBoolean(String identifier) {
		return getOrDefault(identifier, "0").equals("1");
	}

}
