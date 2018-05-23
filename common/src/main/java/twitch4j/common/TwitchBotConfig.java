package twitch4j.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TwitchBotConfig {
	private final String login;
	private final String pass;
}
