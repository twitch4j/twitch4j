/*
 * MIT License
 *
 * Copyright (c) 2018 twitch4j
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.philippheuer.twitch4j.auth;

import me.philippheuer.twitch4j.api.helix.Helix;

public enum Scope {
	/**
	 * Manage a clip object.
	 */
	@Helix
	CLIPS_EDIT("clips:edit"),
	/**
	 * Manage a user object.
	 */
	@Helix
	USER_EDIT("user:edit"),
	/**
	 * Read authorized user's email address.
	 */
	@Helix
	USER_READ_EMAIL("user:read:email"),
	/**
	 * Read whether a user is subscribed to your channel.
	 */
	CHANNEL_CHECK_SUBSCRIPTION,
	/**
	 * Trigger commercials on channel.
	 */
	CHANNEL_COMMERCIAL,
	/**
	 * Write channel metadata (game, status, etc).
	 */
	CHANNEL_EDITOR,
	/**
	 * Add posts and reactions to a channel feed.
	 */
	CHANNEL_FEED_EDIT,
	/**
	 * View a channel feed.
	 */
	CHANNEL_FEED_READ,
	/**
	 * Read nonpublic channel information, including email address and stream key.
	 */
	CHANNEL_READ,
	/**
	 * Reset a channel’s stream key.
	 */
	CHANNEL_STREAM,
	/**
	 * Read all subscribers to your channel.
	 */
	CHANNEL_SUBSCRIPTIONS,
	/**
	 * Log into chat and send messages.
	 */
	CHAT_LOGIN,
	/**
	 * Manage a user's collections (of videos).
	 */
	COLLECTIONS_EDIT,
	/**
	 * Manage a user's communities.
	 */
	COMMUNITIES_EDIT,
	/**
	 * Manage community moderators.
	 */
	COMMUNITIES_MODERATE,
	/**
	 * Use OpenID Connect authentication.
	 */
	OPENID,
	/**
	 * Turn on/off ignoring a user. Ignoring users means you cannot see them type, receive messages from them, etc.
	 */
	USER_BLOCKS_EDIT,
	/**
	 * Read a user’s list of ignored users.
	 */
	USER_BLOCKS_READ,
	/**
	 * Manage a user’s followed channels.
	 */
	USER_FOLLOWS_EDIT,
	/**
	 * Read nonpublic user information, like email address.
	 */
	USER_READ,
	/**
	 * Read a user’s subscriptions.
	 */
	USER_SUBSCRIPTIONS,
	/**
	 * Turn on Viewer Heartbeat Service ability to record user data.
	 */
	VIEWING_ACTIVITY_READ,
	/**
	 * Empty Scope
	 */
	NONE;

	private final String name;

	Scope(String scope) {
		this.name = scope;
	}

	Scope() {
		this.name = name().toLowerCase();
	}

	@Override
	public String toString() {
		return name;
	}
}
