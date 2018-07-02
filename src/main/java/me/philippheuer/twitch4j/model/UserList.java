package me.philippheuer.twitch4j.model;

import lombok.Data;

import java.util.List;

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
