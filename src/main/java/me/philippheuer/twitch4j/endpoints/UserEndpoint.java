package me.philippheuer.twitch4j.endpoints;

import java.util.List;
import java.util.Optional;

import org.springframework.util.Assert;

import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.model.*;

public class UserEndpoint extends AbstractTwitchEndpoint {
	
	/**
	 * Get UserEndpoint
	 * @throws UserNotFoundException 
	 */
	public UserEndpoint(TwitchClient client) {
		super(client);
	}
	
	/**
	 * Endpoint to get the UserId from the UserName
	 * 
	 * https://api.twitch.tv/kraken/users?login=USERNAME
	 * @throws UserNotFoundException 
	 */
	public Optional<Long> getUserIdByUserName(String userName) {
		// Validate Arguments
		Assert.hasLength(userName, "Please provide a Username!");
		
		// REST Request
		String requestUrl = String.format("%s/users?login=%s", getClient().getTwitchEndpoint(), userName);
		if(!restObjectCache.containsKey(requestUrl)) {
			UserList responseObject = getRestTemplate().getForObject(requestUrl, UserList.class);
			restObjectCache.put(requestUrl, responseObject);
		}
		
		List<User> userList = ((UserList)restObjectCache.get(requestUrl)).getUsers();
		
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
			String requestUrl = String.format("%s/users/%d", getClient().getTwitchEndpoint(), userId);
			if(!restObjectCache.containsKey(requestUrl)) {
				User responseObject = getRestTemplate().getForObject(requestUrl, User.class);
				restObjectCache.put(requestUrl, responseObject);
			}
			
			return Optional.ofNullable((User)restObjectCache.get(requestUrl));
		} catch (Exception ex) {
			return Optional.empty();
		}
	}
}
