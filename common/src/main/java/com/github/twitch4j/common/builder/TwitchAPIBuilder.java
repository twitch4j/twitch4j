package com.github.twitch4j.common.builder;

import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.common.errortracking.ErrorTracker;
import com.github.twitch4j.common.errortracking.ErrorTrackingManager;
import com.github.twitch4j.common.errortracking.SentryIOErrorTracking;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TwitchAPIBuilder<T> {

    /**
     * Event Manager
     */
    private EventManager eventManager = new EventManager();

    /**
     * Client Id
     */
    private String clientId = "jzkbprff40iqj646a697cyrvl0zt2m6";

    /**
     * Client Secret
     */
    private String clientSecret = "**SECRET**";

    /**
     * User Agent
     */
    private String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36";

    /**
     * Error Tracking
     */
    private ErrorTrackingManager errorTrackingManager = new ErrorTrackingManager();

    /**
     * With EventManager
     *
     * @param eventManager EventManager
     * @return T
     */
    public T withEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
        return (T) this;
    }

    /**
     * With Client Id
     *
     * @param clientId ClientId
     * @return T
     */
    public T withClientId(String clientId) {
        this.clientId = clientId;
        return (T) this;
    }

    /**
     * With Client Secret
     *
     * @param clientSecret ClientSecret
     * @return T
     */
    public T withClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return (T) this;
    }

    /**
     * With User Agent
     *
     * @param userAgent userAgent
     * @return T
     */
    public T withUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return (T) this;
    }

    /**
     * With Error Tracking Client
     *
     * @param errorTrackingManager Error Tracking
     * @return T
     */
    public T withErrorTracking(ErrorTrackingManager errorTrackingManager) {
        this.errorTrackingManager = errorTrackingManager;
        return (T) this;
    }

    /**
     * Gets the internal error tracker
     *
     * @param moduleName Module Name
     * @return ErrorTracker
     */
    public ErrorTracker getModuleErrorTracker(String moduleName) {
        ErrorTracker errorTracker = new SentryIOErrorTracking("https://2bbb1bbc08484819afb5a14a77707761@sentry.io/1418066");
        errorTracker.addTag("module", moduleName);
        return errorTracker;
    }

}
