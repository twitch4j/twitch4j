package twitch4j.irc.utils;

public class TagsUtil {
	public static boolean mightBeEscaped(String segment) {
		return segment.equals("display-name")
				|| segment.equals("ban-reason")
				|| segment.equals("system-msg");
	}

	public static String replaceEscapes(String segment) {
		return segment.replace("\\s", " ").replace("\\:", ";");
	}
}
