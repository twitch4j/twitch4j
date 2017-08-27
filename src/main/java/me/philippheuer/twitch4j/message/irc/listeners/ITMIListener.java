package me.philippheuer.twitch4j.message.irc.listeners;

import me.philippheuer.twitch4j.message.irc.events.*;

import java.util.List;

public interface ITMIListener {
	/**
	 * Received action message on channel.
	 * @param event chat event
	 */
	void onAction(ChatEvent event);

	/**
	 * Username has been banned on a channel.
	 * @param event Clear chat event
	 */
	void onBan(ClearChatEvent event);

	/**
	 * Received message on channel.
	 * @param event Chat event
	 */
	void onChat(ChatEvent event);

	/**
	 * Username has cheered to a channel.
	 * @param event Event emmiter
	 */
	void onCheer(ChatEvent event);

	/**
	 * Chat of a channel got cleared.
	 * @param channel channel name
	 */
	void onClearchat(String channel);

	/**
	 * Connected to server.
	 * @param event connection data
	 */
	void onConnected(ConnectionEvent event);

	/**
	 * Connecting to a server.
	 * @param event connection data
	 */
	void onConnecting(ConnectionEvent event);

	/**
	 * Got disconnected from server.
	 * @param event connection data with reason
	 */
	void onDisconnected(ConnectionEvent event);

	/**
	 * Channel enabled or disabled emote-only mode.
	 * @param event Channel state
	 */
	void onEmoteOnly(ChannelState event);

	/**
	 * Channel enabled or disabled followers-only mode.
	 * @param event status Room state
	 */
	void onFollowersonly(RoomStateEvent.Status event);

	/**
	 * Channel is now hosted by another broadcaster.
	 * @param event Host event
	 */
	void onHosted(HostEvent event);

	/**
	 * Channel is now hosting another channel.
	 * @param event Host event
	 */
	void onHosting(HostEvent event);

	/**
	 * Username has joined a channel.
	 * @param event User status (join/part) on the channel
	 */
	void onJoin(UserStatusEvent event);

	/**
	 * Received a message.
	 * @param event Chat event
	 */
	void onMessage(ChatEvent event);

	/**
	 * Someone got modded on a channel.
	 * @param event Channel Mod event
	 */
	void onMod(ChannelModEvent event);

	/**
	 * Received the list of moderators of a channel.
	 * @param userMods list of moderators
	 */
	void onMods(ChannelModsEvent userMods);

	/**
	 * Received a notice from server.
	 * @param event Notice Channel Event
	 */
	void onNotice(ChannelEvent event);

	/**
	 * User has left a channel.
	 * @param event User status (join/part) on the channel
	 */
	void onPart(UserStatusEvent event);

	/**
	 * Received PING from server.
	 */
	void onPing();

	/**
	 * Sent a PING request ? PONG.
	 * @param latency Current latency
	 */
	void onPong(float latency);

	/**
	 * Channel enabled or disabled R9K mode.
	 * @param event Channel State
	 */
	void onR9kMode(ChannelState event);

	/**
	 * Trying to reconnect to server.
	 * @param event events contains information of connection state
	 */
	void onReconnect(ConnectionEvent event);

	/**
	 * Username has resubbed on a channel.
	 * @param event Sub event
	 */
	void onResub(SubscribeEvent event);

	/**
	 * The current state of the channel.
	 * @param event Room State event
	 */
	void onRoomstate(RoomStateEvent event);

	/**
	 * Gives you the current state of the channel.
	 * @param event status Room state
	 */
	void onSlowmode(RoomStateEvent.Status event);

	/**
	 * Channel enabled or disabled subscribers-only mode.
	 * @param event Channel state
	 */
	void onSubscribers(ChannelState event);

	/**
	 * Username has subscribed to a channel.
	 * @param event Sub event
	 */
	void onSubscription(SubscribeEvent event);

	/**
	 * Username has been timed out on a channel.
	 * @param event Clear Chat data
	 * @param duration duration of timeout
	 */
	void onTimeout(ClearChatEvent event, int duration);

	/**
	 * Channel ended the current hosting.
	 * @param event Host Event
	 */
	void onUnhost(HostEvent event);

	/**
	 * Someone got unmodded on a channel.
	 * @param event Channel Mod event
	 */
	void onUnmod(ChannelModEvent event);

	/**
	 * Received a whisper.
	 * @param event Chat event
 	 */
	void onWhisper(ChatEvent event);

	/**
	 * List of users in the channel after joining them
	 * @param channel Channel name
	 * @param userlist list of the current chatters
	 */
	void onNames(String channel, List<String> userlist);

	/**
	 * Disconnecting from server.
	 * @param event connection data with reason
	 */
	void onDisconnecting(ConnectionEvent event);
}


