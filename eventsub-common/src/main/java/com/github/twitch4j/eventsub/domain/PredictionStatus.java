package com.github.twitch4j.eventsub.domain;

public enum PredictionStatus {

    /**
     * A winning outcome has been chosen and the Channel Points have been distributed to the users who guessed the correct outcome.
     */
    RESOLVED,

    /**
     * The Prediction is active and viewers can make predictions.
     */
    ACTIVE,

    /**
     * The Prediction has been canceled and the Channel Points have been refunded to participants.
     */
    CANCELED,

    /**
     * The Prediction has been locked and viewers can no longer make predictions.
     */
    LOCKED

}
