package me.philippheuer.twitch4j.connect;

import me.philippheuer.twitch4j.api.TwitchApi;
import me.philippheuer.twitch4j.api.model.IUser;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UserProfileBuilder;
import org.springframework.web.client.HttpClientErrorException;

public class TwitchAdapter implements ApiAdapter<TwitchApi> {
	@Override
	public boolean test(TwitchApi api) {
		try {
			api.userOperation().getUser();
			return true;
		} catch (HttpClientErrorException e) {
			return false;
		}
	}

	@Override
	@SuppressWarnings("Duplicates")
	public void setConnectionValues(TwitchApi api, ConnectionValues values) {
		IUser user = api.userOperation().getUser();
		values.setProviderUserId(user.getStringId());
		values.setDisplayName(user.getDisplayName());
		values.setProfileUrl(user.getChannel().getUrl());
		values.setImageUrl(user.getLogoUrl());
	}

	@Override
	public UserProfile fetchUserProfile(TwitchApi kraken) {
		IUser user = kraken.userOperation().getUser();

		String display = user.getDisplayName();
		if (display == null) {
			display = user.getUsername();
		}
		return new UserProfileBuilder()
				.setName(display)
				.setUsername(user.getUsername())
				.setEmail(user.getEmail())
				.build();
	}

	@Override
	public void updateStatus(TwitchApi api, String message) {
		// currently unsupported
	}
}
