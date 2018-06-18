package twitch4j.common.enums;

public enum VideoAccess {
	PUBLIC,
	PRIVATE;

	public static VideoAccess of(String viewable) {
		for (VideoAccess access : values()) {
			if (access.name().equalsIgnoreCase(viewable)) {
				return access;
			}
		}

		return PRIVATE;
	}
}
