package me.philippheuer.twitch4j.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Model representing a list of twitch communities.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CommunityList extends AbstractResultList {
	/**
	 * Data
	 */
	private List<Community> communities;

}
