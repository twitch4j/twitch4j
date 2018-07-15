package twitch4j.enums;

public enum UserType {
	STAFF,
	ADMIN,
	GLOBAL_MOD,
	NONE;

	public static UserType of(String broadcasterType) {
		for (UserType type : values()) {
			if (type.name().equalsIgnoreCase(broadcasterType)) {
				return type;
			}
		}

		return NONE;
	}
}
