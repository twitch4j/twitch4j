package de.philippheuer.twitch4j.endpoints;

import java.util.List;

import java.util.Optional;

import de.philippheuer.twitch4j.Twitch4J;
import de.philippheuer.twitch4j.model.*;

public class UserEndpoint extends AbstractTwitchEndpoint {
	
	/**
	 * Get UserEndpoint
	 * @throws UserNotFoundException 
	 */
	public UserEndpoint(Twitch4J api) {
		super(api);
	}
	
	/**
	 * Endpoint to get the UserId from the UserName
	 * 
	 * https://api.twitch.tv/kraken/users?login=USERNAME
	 * @throws UserNotFoundException 
	 */
	public Optional<Long> getUserIdByUserName(String userName) {
		// REST Request
		String requestUrl = String.format("%s/users?login=%s", getApi().getTwitchEndpoint(), userName);
		UserList responseObject = getRestTemplate().getForObject(requestUrl, UserList.class);
		List<User> userList = responseObject.getUsers();
		
		// User found?
		if(userList.size() == 1) {
			return Optional.ofNullable(userList.get(0).getId());
		} else {
			return Optional.empty();
		}
	}
	
	/**
	 * Endpoint to get User Information
	 * 
	 * https://api.twitch.tv/kraken/users?login=USERNAME
	 */
	public User getUser(String userName) {
		// REST Request
		// ...
		
		return null;
	}
}
