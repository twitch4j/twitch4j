package me.philippheuer.twitch4j.message.irc.listeners;

import me.philippheuer.twitch4j.message.irc.events.*;

import java.util.List;

public class DefaultListener implements ITMIListener {

	/**
	 * Received action message on channel.
	 *
	 * @param event chat event
	 */
	@Override
	public void onAction(ChatEvent event) {

	}

	/**
	 * Username has been banned on a channel.
	 *
	 * @param event Clear chat event
	 */
	@Override
	public void onBan(ClearChatEvent event) {

	}

	/**
	 * Received message on channel.
	 *
	 * @param event Chat event
	 */
	@Override
	public void onChat(ChatEvent event) {

	}

	/**
	 * Username has cheered to a channel.
	 *
	 * @param event Event emmiter
	 */
	@Override
	public void onCheer(ChatEvent event) {

	}

	/**
	 * Chat of a channel got cleared.
	 *
	 * @param channel channel name
	 */
	@Override
	public void onClearchat(String channel) {

	}

	/**
	 * Connected to server.
	 *
	 * @param event connection data
	 */
	@Override
	public void onConnected(ConnectionEvent event) {

	}

	/**
	 * Connecting to a server.
	 *
	 * @param event connection data
	 */
	@Override
	public void onConnecting(ConnectionEvent event) {

	}

	/**
	 * Got disconnected from server.
	 *
	 * @param event connection data with reason
	 */
	@Override
	public void onDisconnected(ConnectionEvent event) {

	}

	/**
	 * Channel enabled or disabled emote-only mode.
	 *
	 * @param event Channel state
	 */
	@Override
	public void onEmoteOnly(ChannelState event) {

	}

	/**
	 * Channel enabled or disabled followers-only mode.
	 *
	 * @param event status Room state
	 */
	@Override
	public void onFollowersonly(RoomStateEvent.Status event) {

	}

	/**
	 * Channel is now hosted by another broadcaster.
	 *
	 * @param event Host event
	 */
	@Override
	public void onHosted(HostEvent event) {

	}

	/**
	 * Channel is now hosting another channel.
	 *
	 * @param event Host event
	 */
	@Override
	public void onHosting(HostEvent event) {

	}

	/**
	 * Username has joined a channel.
	 *
	 * @param event User status (join/part) on the channel
	 */
	@Override
	public void onJoin(UserStatusEvent event) {

	}

	/**
	 * Received a message.
	 *
	 * @param event Chat event
	 */
	@Override
	public void onMessage(ChatEvent event) {

	}

	/**
	 * Someone got modded on a channel.
	 *
	 * @param event Channel Mod event
	 */
	@Override
	public void onMod(ChannelModEvent event) {

	}

	/**
	 * Received the list of moderators of a channel.
	 *
	 * @param userMods list of moderators
	 */
	@Override
	public void onMods(ChannelModsEvent userMods) {

	}

	/**
	 * Received a notice from server.
	 *
	 * @param event Notice Channel Event
	 */
	@Override
	public void onNotice(ChannelEvent event) {

	}

	/**
	 * User has left a channel.
	 *
	 * @param event User status (join/part) on the channel
	 */
	@Override
	public void onPart(UserStatusEvent event) {

	}

	/**
	 * Received PING from server.
	 */
	@Override
	public void onPing() {

	}

	/**
	 * Sent a PING request ? PONG.
	 *
	 * @param latency Current latency
	 */
	@Override
	public void onPong(float latency) {

	}

	/**
	 * Channel enabled or disabled R9K mode.
	 *
	 * @param event Channel State
	 */
	@Override
	public void onR9kMode(ChannelState event) {

	}

	/**
	 * Trying to reconnect to server.
	 *
	 * @param event events contains information of connection state
	 */
	@Override
	public void onReconnect(ConnectionEvent event) {

	}

	/**
	 * Username has resubbed on a channel.
	 *
	 * @param event Sub event
	 */
	@Override
	public void onResub(SubscribeEvent event) {

	}

	/**
	 * The current state of the channel.
	 *
	 * @param event Room State event
	 */
	@Override
	public void onRoomstate(RoomStateEvent event) {

	}

	/**
	 * Gives you the current state of the channel.
	 *
	 * @param event status Room state
	 */
	@Override
	public void onSlowmode(RoomStateEvent.Status event) {

	}

	/**
	 * Channel enabled or disabled subscribers-only mode.
	 *
	 * @param event Channel state
	 */
	@Override
	public void onSubscribers(ChannelState event) {

	}

	/**
	 * Username has subscribed to a channel.
	 *
	 * @param event Sub event
	 */
	@Override
	public void onSubscription(SubscribeEvent event) {

	}

	/**
	 * Username has been timed out on a channel.
	 *
	 * @param event    Clear Chat data
	 * @param duration duration of timeout
	 */
	@Override
	public void onTimeout(ClearChatEvent event, int duration) {

	}

	/**
	 * Channel ended the current hosting.
	 *
	 * @param event Host Event
	 */
	@Override
	public void onUnhost(HostEvent event) {

	}

	/**
	 * Someone got unmodded on a channel.
	 *
	 * @param event Channel Mod event
	 */
	@Override
	public void onUnmod(ChannelModEvent event) {

	}

	/**
	 * Received a whisper.
	 *
	 * @param event Chat event
	 */
	@Override
	public void onWhisper(ChatEvent event) {

	}

	/**
	 * List of users in the channel after joining them
	 *
	 * @param channel  Channel name
	 * @param userlist list of the current chatters
	 */
	@Override
	public void onNames(String channel, List<String> userlist) {

	}

	/**
	 * Disconnecting from server.
	 *
	 * @param event connection data with reason
	 */
	@Override
	public void onDisconnecting(ConnectionEvent event) {

	}
}
