package com.github.twitch4j.common.errortracking;

import java.util.Map;

public interface ErrorTracker {

    /**
     * Handle the exception
     *
     * @param ex Exception
     */
    void handleException(Throwable ex);

    /**
     * Records a action to trace the origin of the error
     *
     * @param actionMessage
     */
    void recordAction(String actionMessage);

    /**
     * Adds a tag
     *
     * @param name Name
     * @param value Value
     */
    void addTag(String name, String value);

    /**
     * Sets the user context
     *
     * @param userId  User Id
     * @param username Username
     * @param data Data
     */
    void setUserContext(String userId, String username, Map<String, Object> data);

    /**
     * Clear User Context
     */
    void clearUserContext();

    /**
     * Clear the Context
     */
    void clearContext();

}
