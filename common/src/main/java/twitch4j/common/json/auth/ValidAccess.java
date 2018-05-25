package twitch4j.common.json.auth;

import java.util.List;
import lombok.Data;
import twitch4j.common.auth.Scope;

@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class ValidAccess {
	private String clientId;
	private String login;
	private List<Scope> scopes;
	private Long userId;
}
