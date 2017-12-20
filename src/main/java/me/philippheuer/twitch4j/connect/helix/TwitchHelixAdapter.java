package me.philippheuer.twitch4j.connect.helix;

import me.philippheuer.twitch4j.api.helix.HelixApi;
import me.philippheuer.twitch4j.api.model.IUser;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UserProfileBuilder;
import org.springframework.web.client.HttpClientErrorException;

public class TwitchHelixAdapter implements ApiAdapter<HelixApi> {
    @Override
    public boolean test(HelixApi kraken) {
        try {
            kraken.userOperation().getUser();
            return true;
        } catch (HttpClientErrorException e) {
            return false;
        }
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void setConnectionValues(HelixApi helix, ConnectionValues values) {
        IUser user = helix.userOperation().getUser();
        values.setProviderUserId(user.getStringId());
        values.setDisplayName(user.getDisplayName());
        values.setProfileUrl(user.getChannel().getUrl());
        values.setImageUrl(user.getLogoUrl());
    }

    @Override
    public UserProfile fetchUserProfile(HelixApi helix) {
        IUser user = helix.userOperation().getUser();

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
    public void updateStatus(HelixApi helix, String message) {
        // currently unsupported
    }
}
