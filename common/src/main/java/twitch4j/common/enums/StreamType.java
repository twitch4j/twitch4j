package twitch4j.common.enums;

public enum StreamType {
	LIVE,
	PLAYLIST;

	public static StreamType of(String type) {
		if (type.equalsIgnoreCase("live")) {
			return LIVE;
		} else {
			return PLAYLIST;
		}
	}
}
