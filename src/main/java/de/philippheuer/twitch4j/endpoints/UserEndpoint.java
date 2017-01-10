package de.philippheuer.twitch4j.endpoints;

import java.util.List;

import java.util.Optional;

import org.springframework.util.Assert;

import de.philippheuer.twitch4j.TwitchClient;
import de.philippheuer.twitch4j.model.*;

public class UserEndpoint extends AbstractTwitchEndpoint {
	
	/**
	 * Get UserEndpoint
	 * @throws UserNotFoundException 
	 */
	public UserEndpoint(TwitchClient api) {
		super(api);
	}
	
	/**
	 * Endpoint to get the UserId from the UserName
	 * 
	 * https://api.twitch.tv/kraken/users?login=USERNAME
	 * @throws UserNotFoundException 
	 */
	public Optional<Long> getUserIdByUserName(String userName) {
		// Validate Arguments
		Assert.notNull(userName, "Please provide a Username!");
		
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
	public Optional<User> getUser(Long userId) {
		// Validate Arguments
		Assert.notNull(userId, "Please provide a User ID!");
		
		// REST Request
		try {
			String requestUrl = String.format("%s/users/%d", getApi().getTwitchEndpoint(), userId);
			User responseObject = getRestTemplate().getForObject(requestUrl, User.class);
			
			return Optional.ofNullable(responseObject);
		} catch (Exception ex) {
			return Optional.empty();
		}
	}
}
