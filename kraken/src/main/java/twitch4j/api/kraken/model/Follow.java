package twitch4j.api.kraken.model;

import java.util.Date;
import lombok.Data;

/**
 * Model representing a follower.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
public class Follow {

	private Date createdAt;

	private Boolean notifications;

	private User user;

	private Channel channel;

}
