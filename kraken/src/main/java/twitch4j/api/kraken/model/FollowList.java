package twitch4j.api.kraken.model;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Model representing a list of followers.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FollowList extends AbstractResultList {
	/**
	 * Data
	 */
	private List<Follow> follows;

}
