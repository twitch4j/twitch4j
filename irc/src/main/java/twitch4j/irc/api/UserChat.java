package twitch4j.irc.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import twitch4j.common.json.Badges;

@Data
public class UserChat {

	@JsonProperty("_id")
	private Long id;

	private String login;

	private String displayName;

	private String color;

	@JsonProperty("is_verified_bot")
	private boolean verifiedBot;

	@JsonProperty("is_known_bot")
	private boolean knownBot;

	private List<Badges> badges;

}
