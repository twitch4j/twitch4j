package me.philippheuer.twitch4j.message.irc.listeners;

import me.philippheuer.twitch4j.events.Event;

public interface TMIListener {
	/**
	 * Received action message on channel.
	 * @param event Event emmiter
	 */
	void onAction(Event event);

	/**
	 * Username has been banned on a channel.
	 * @param event Event emmiter
	 */
	void onBan(Event event);

	/**
	 * Received message on channel.
	 * @param event Event emmiter
	 */
	void onChat(Event event);

	/**
	 * Username has cheered to a channel.
	 * @param event Event emmiter
	 */
	void onCheer(Event event);

	/**
	 * Chat of a channel got cleared.
	 * @param event Event emmiter
	 */
	void onClearchat(Event event);

	/**
	 * Connected to server.
	 * @param event Event emmiter
	 */
	void onConnected(Event event);

	/**
	 * Connecting to a server.
	 * @param event Event emmiter
	 */
	void onConnecting(Event event);

	/**
	 * Got disconnected from server.
	 * @param event Event emmiter
	 */
	void onDisconnected(Event event);

	/**
	 * Channel enabled or disabled emote-only mode.
	 * @param event Event emmiter
	 */
	void onEmoteonly(Event event);

	/**
	 * Received the emote-sets from Twitch.
	 * @param event Event emmiter
	 */
	void onEmotesets(Event event);

	/**
	 * Channel enabled or disabled followers-only mode.
	 * @param event Event emmiter
	 */
	void onFollowersonly(Event event);

	/**
	 * Channel is now hosted by another broadcaster.
	 * @param event Event emmiter
	 */
	void onHosted(Event event);

	/**
	 * Channel is now hosting another channel.
	 * @param event Event emmiter
	 */
	void onHosting(Event event);

	/**
	 * Username has joined a channel.
	 * @param event Event emmiter
	 */
	void onJoin(Event event);

	/**
	 * Connection established, sending informations to server.
	 * @param event Event emmiter
	 */
	void onLogon(Event event);

	/**
	 * Received a message.
	 * @param event Event emmiter
	 */
	void onMessage(Event event);

	/**
	 * Someone got modded on a channel.
	 * @param event Event emmiter
	 */
	void onMod(Event event);

	/**
	 * Received the list of moderators of a channel.
	 * @param event Event emmiter
	 */
	void onMods(Event event);

	/**
	 * Received a notice from server.
	 * @param event Event emmiter
	 */
	void onNotice(Event event);

	/**
	 * User has left a channel.
	 * @param event Event emmiter
	 */
	void onPart(Event event);

	/**
	 * Received PING from server.
	 * @param event Event emmiter
	 */
	void onPing(Event event);

	/**
	 * Sent a PING request ? PONG.
	 * @param event Event emmiter
	 */
	void onPong(Event event);

	/**
	 * Channel enabled or disabled R9K mode.
	 * @param event Event emmiter
	 */
	void onR9kbeta(Event event);

	/**
	 * Trying to reconnect to server.
	 */
	void onReconnect(Event event);

	/**
	 * Username has resubbed on a channel.
	 * @param event Event emmiter
	 */
	void onResub(Event event);

	/**
	 * The current state of the channel.
	 * @param event Event emmiter
	 */
	void onRoomstate(Event event);

	/**
	 * Channel is no longer located on this cluster.
	 * @param event Event emmiter
	 */
	void onServerchange(Event event);

	/**
	 * Gives you the current state of the channel.
	 * @param event Event emmiter
	 */
	void onSlowmode(Event event);

	/**
	 * Channel enabled or disabled subscribers-only mode.
	 * @param event Event emmiter
	 */
	void onSubscribers(Event event);

	/**
	 * Username has subscribed to a channel.
	 * @param event Event emmiter
	 */
	void onSubscription(Event event);

	/**
	 * Username has been timed out on a channel.
	 * @param event Event emmiter
	 */
	void onTimeout(Event event);

	/**
	 * Channel ended the current hosting.
	 */
	void onUnhost(Event event);

	/**
	 * Someone got unmodded on a channel.
	 * @param event Event emmiter
	 */
	void onUnmod(Event event);

	/**
	 * Received a whisper.
	 * @param event Event emmiter
 	 */
	void onWhisper(Event event);
}


