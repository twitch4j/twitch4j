package com.github.twitch4j.helix.domain;

/**
 * The action to take on a message held for review by AutoMod.
 *
 * @see com.github.twitch4j.helix.TwitchHelix#manageAutoModHeldMessage(String, String, String, AutoModHeldMessageAction)
 */
public enum AutoModHeldMessageAction {
    ALLOW,
    DENY
}
