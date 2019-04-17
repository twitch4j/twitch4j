package com.github.twitch4j.common.errortracking;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@Slf4j
public class ErrorTrackingManager implements ErrorTracker {

    private Set<ErrorTracker> errorTrackers;

    /**
     * Constructor
     */
    public ErrorTrackingManager() {
        errorTrackers = new HashSet<>();
    }

    /**
     * Registers a new error tracker
     *
     * @param errorTracker ErrorTracker
     */
    public void addErrorTracker(ErrorTracker errorTracker) {
        errorTrackers.add(errorTracker);
    }

    /**
     * Handle the exception
     *
     * @param ex Exception
     */
    public void handleException(Throwable ex) {
        log.error(ex.toString());
        errorTrackers.forEach(tracker -> tracker.handleException(ex));
    }

    /**
     * Records a action to trace the origin of the error
     *
     * @param actionMessage
     */
    public void recordAction(String actionMessage) {
        errorTrackers.forEach(tracker -> tracker.recordAction(actionMessage));
    }

    /**
     * Adds a tag
     *
     * @param name Name
     * @param value Value
     */
    public void addTag(String name, String value) {
        errorTrackers.forEach(tracker -> tracker.addTag(name, value));
    }

    /**
     * Sets the user context
     *
     * @param userId  User Id
     * @param username Username
     * @param data Data
     */
    public void setUserContext(String userId, String username, Map<String, Object> data) {
        errorTrackers.forEach(tracker -> tracker.setUserContext(userId, username, data));
    }

    /**
     * Clear User Context
     */
    public void clearUserContext() {
        errorTrackers.forEach(tracker -> tracker.clearUserContext());
    }

    /**
     * Clear the Context
     */
    public void clearContext() {
        errorTrackers.forEach(tracker -> tracker.clearContext());
    }
}
