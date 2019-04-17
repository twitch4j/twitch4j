package com.github.twitch4j.common.errortracking;

import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import io.sentry.context.Context;
import io.sentry.event.BreadcrumbBuilder;
import io.sentry.event.UserBuilder;

import java.util.Map;

public class SentryIOErrorTracking implements ErrorTracker {

    /**
     * Sentry IO Client
     */
    private SentryClient sentryClient;

    /**
     * Context
     */
    private Context context;

    /**
     * Construcator
     *
     * @param dsn dsn //
     */
    public SentryIOErrorTracking(String dsn) {
        // Create a SentryClient instance that you manage manually.
        sentryClient = SentryClientFactory.sentryClient(dsn);
        sentryClient.setRelease("v1.0.0-alpha.13");

        // Get the current context instance.
        context = sentryClient.getContext();
    }

    /**
     * Handle the exception
     *
     * @param ex Exception
     */
    public void handleException(Throwable ex) {
        sentryClient.sendException(ex);
    }

    /**
     * Records a action to trace the origin of the error
     *
     * @param actionMessage
     */
    public void recordAction(String actionMessage) {
        context.recordBreadcrumb(
            new BreadcrumbBuilder().setMessage(actionMessage).build()
        );
    }

    /**
     * Adds a tag
     *
     * @param name Name
     * @param value Value
     */
    public void addTag(String name, String value) {
        context.addTag(name, value);
    }

    /**
     * Sets the user context
     *
     * @param userId  User Id
     * @param username Username
     * @param data Data
     */
    public void setUserContext(String userId, String username, Map<String, Object> data) {
        // Set the current user in the context.
        context.setUser(
            new UserBuilder()
                .setId(userId)
                .setUsername(username)
                .setData(data)
                .build()
        );
    }

    /**
     * Clear User Context
     */
    public void clearUserContext() {
        context.setUser(null);
    }

    /**
     * Clear the Context
     */
    public void clearContext() {
        context.clear();
    }

}
