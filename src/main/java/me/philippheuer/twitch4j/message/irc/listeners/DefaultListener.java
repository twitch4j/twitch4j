package me.philippheuer.twitch4j.message.irc.listeners;

import me.philippheuer.twitch4j.events.Event;

public class DefaultListener implements TMIListener{
	/**
	 * Received action message on channel.
	 *
	 * @param event
	 */
	@Override
	public void onAction(Event event) {

	}

	/**
	 * Username has been banned on a channel.
	 *
	 * @param event
	 */
	@Override
	public void onBan(Event event) {

	}

	/**
	 * Received message on channel.
	 *
	 * @param event
	 */
	@Override
	public void onChat(Event event) {

	}

	/**
	 * Username has cheered to a channel.
	 *
	 * @param event
	 */
	@Override
	public void onCheer(Event event) {

	}

	/**
	 * Chat of a channel got cleared.
	 *
	 * @param event
	 */
	@Override
	public void onClearchat(Event event) {

	}

	/**
	 * Connected to server.
	 *
	 * @param event
	 */
	@Override
	public void onConnected(Event event) {

	}

	/**
	 * Connecting to a server.
	 *
	 * @param event
	 */
	@Override
	public void onConnecting(Event event) {

	}

	/**
	 * Got disconnected from server.
	 *
	 * @param event
	 */
	@Override
	public void onDisconnected(Event event) {

	}

	/**
	 * Channel enabled or disabled emote-only mode.
	 *
	 * @param event
	 */
	@Override
	public void onEmoteonly(Event event) {

	}

	/**
	 * Received the emote-sets from Twitch.
	 *
	 * @param event
	 */
	@Override
	public void onEmotesets(Event event) {

	}

	/**
	 * Channel enabled or disabled followers-only mode.
	 *
	 * @param event
	 */
	@Override
	public void onFollowersonly(Event event) {

	}

	/**
	 * Channel is now hosted by another broadcaster.
	 *
	 * @param event
	 */
	@Override
	public void onHosted(Event event) {

	}

	/**
	 * Channel is now hosting another channel.
	 *
	 * @param event
	 */
	@Override
	public void onHosting(Event event) {

	}

	/**
	 * Username has joined a channel.
	 *
	 * @param event
	 */
	@Override
	public void onJoin(Event event) {

	}

	/**
	 * Connection established, sending informations to server.
	 *
	 * @param event
	 */
	@Override
	public void onLogon(Event event) {

	}

	/**
	 * Received a message.
	 *
	 * @param event
	 */
	@Override
	public void onMessage(Event event) {

	}

	/**
	 * Someone got modded on a channel.
	 *
	 * @param event
	 */
	@Override
	public void onMod(Event event) {

	}

	/**
	 * Received the list of moderators of a channel.
	 *
	 * @param event
	 */
	@Override
	public void onMods(Event event) {

	}

	/**
	 * Received a notice from server.
	 *
	 * @param event
	 */
	@Override
	public void onNotice(Event event) {

	}

	/**
	 * User has left a channel.
	 *
	 * @param event
	 */
	@Override
	public void onPart(Event event) {

	}

	/**
	 * Received PING from server.
	 *
	 * @param event
	 */
	@Override
	public void onPing(Event event) {

	}

	/**
	 * Sent a PING request ? PONG.
	 *
	 * @param event
	 */
	@Override
	public void onPong(Event event) {

	}

	/**
	 * Channel enabled or disabled R9K mode.
	 *
	 * @param event
	 */
	@Override
	public void onR9kbeta(Event event) {

	}

	/**
	 * Trying to reconnect to server.
	 *
	 * @param event
	 */
	@Override
	public void onReconnect(Event event) {

	}

	/**
	 * Username has resubbed on a channel.
	 *
	 * @param event
	 */
	@Override
	public void onResub(Event event) {

	}

	/**
	 * The current state of the channel.
	 *
	 * @param event
	 */
	@Override
	public void onRoomstate(Event event) {

	}

	/**
	 * Channel is no longer located on this cluster.
	 *
	 * @param event
	 */
	@Override
	public void onServerchange(Event event) {

	}

	/**
	 * Gives you the current state of the channel.
	 *
	 * @param event
	 */
	@Override
	public void onSlowmode(Event event) {

	}

	/**
	 * Channel enabled or disabled subscribers-only mode.
	 *
	 * @param event
	 */
	@Override
	public void onSubscribers(Event event) {

	}

	/**
	 * Username has subscribed to a channel.
	 *
	 * @param event
	 */
	@Override
	public void onSubscription(Event event) {

	}

	/**
	 * Username has been timed out on a channel.
	 *
	 * @param event
	 */
	@Override
	public void onTimeout(Event event) {

	}

	/**
	 * Channel ended the current hosting.
	 *
	 * @param event
	 */
	@Override
	public void onUnhost(Event event) {

	}

	/**
	 * Someone got unmodded on a channel.
	 *
	 * @param event
	 */
	@Override
	public void onUnmod(Event event) {

	}

	/**
	 * Received a whisper.
	 *
	 * @param event
	 */
	@Override
	public void onWhisper(Event event) {

	}
}
