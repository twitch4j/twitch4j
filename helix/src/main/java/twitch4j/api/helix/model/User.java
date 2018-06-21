package twitch4j.api.helix.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import twitch4j.common.enums.BroadcasterType;
import twitch4j.common.enums.UserType;
import lombok.Data;

import javax.annotation.Nullable;

@Data
public class User {
	private Long id;
	@JsonProperty("login")
	private String username;
	private String displayName;
	private UserType type;
	private BroadcasterType broadcasterType;
	private String description;
	private String profileImageUrl;
	private String offlineImageUrl;
	private Long viewCount;
	@Nullable
	private String email;
}
