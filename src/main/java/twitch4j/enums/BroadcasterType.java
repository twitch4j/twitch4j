package twitch4j.enums;

public enum BroadcasterType {
	PARTNER,
	AFFILIATE,
	NONE;

	public static BroadcasterType of(String broadcasterType) {
		for (BroadcasterType type : values()) {
			if (type.name().equalsIgnoreCase(broadcasterType)) {
				return type;
			}
		}

		return NONE;
	}
}
