package twitch4j.model;

import java.util.Date;
import java.util.List;
import lombok.Data;
import twitch4j.enums.Scope;

/**
 * Model representing a token authorization.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
public class TokenAuthorization {
	private List<Scope> scopes;

	private Date createdAt;

	private Date updatedAt;

}
