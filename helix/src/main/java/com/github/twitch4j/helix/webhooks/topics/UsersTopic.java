package com.github.twitch4j.helix.webhooks.topics;

import com.github.twitch4j.helix.domain.UserList;
import lombok.Getter;
import lombok.NonNull;

import java.util.TreeMap;

/**
 * Notifies when a user changes information about his/her profile.
 * This web hook requires the user:read:email OAuth scope, to get notifications of email changes.
 */
@Getter
public class UsersTopic extends TwitchWebhookTopic<UserList> {

    public static final String PATH = "/users";

    private static TreeMap<String, Object> mapParameters(String userId) {
        TreeMap<String, Object> parameterMap = new TreeMap<>();
        parameterMap.put("user_id", userId);
        return parameterMap;
    }

    /**
     * @return The user whose data is monitored.
     */
    private String userId;

    /**
     * Notifies when a user changes information about his/her profile.
     * This web hook requires the user:read:email OAuth scope, to get notifications of email changes.
     *
     * @param userId Required. Specifies the user whose data is monitored.
     */
    public UsersTopic(@NonNull String userId) {
        super(
            PATH,
            UserList.class,
            mapParameters(userId)
        );
        this.userId = userId;
    }

}
