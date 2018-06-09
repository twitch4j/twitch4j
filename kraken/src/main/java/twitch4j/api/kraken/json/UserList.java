package twitch4j.api.kraken.json;

import java.util.List;
import lombok.Data;

/**
 * Model representing users.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
public class UserList {
	/**
	 * Data
	 */
	private List<User> users;

}
