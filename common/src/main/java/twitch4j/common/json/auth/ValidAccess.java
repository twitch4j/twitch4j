package twitch4j.common.json.auth;

import lombok.Data;
import twitch4j.common.auth.Scope;

import java.util.List;

@Data
public class ValidAccess {
	private final String clientId;
	private final String login;
	private final List<Scope> scopes;
	private final Long userId;
}
