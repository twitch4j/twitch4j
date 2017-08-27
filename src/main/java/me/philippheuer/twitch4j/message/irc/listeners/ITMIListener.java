package me.philippheuer.twitch4j.message.irc.listeners;

import me.philippheuer.twitch4j.message.irc.events.*;

import java.util.List;
import java.util.Map;

public interface ITMIListener {
	/**
	 * Received action message on channel.
	 * @param event Event emmiter
	 */
	void onAction(ChatEvent event);

	/**
	 * Username has been banned on a channel.
	 * @param event Event emmiter
	 */
	void onBan(ChannelEvent event);

	/**
	 * Received message on channel.
	 * @param event Event emmiter
	 */
	void onChat(ChatEvent event);

	/**
	 * Username has cheered to a channel.
	 * @param event Event emmiter
	 */
	void onCheer(CheerEvent event);

	/**
	 * Chat of a channel got cleared.
	 * @param event Event emmiter
	 */
	void onClearchat(ChannelEvent event);

	/**
	 * Connected to server.
	 * @param event Event emmiter
	 */
	void onConnected(ConnectionEvent event);

	/**
	 * Connecting to a server.
	 * @param event Event emmiter
	 */
	void onConnecting(ConnectionEvent event);

	/**
	 * Got disconnected from server.
	 * @param event Event emmiter
	 */
	void onDisconnected(ConnectionEvent event);

	/**
	 * Channel enabled or disabled emote-only mode.
	 * @param event Event emmiter
	 */
	void onEmoteOnly(ChannelState event);

	/**
	 * Received the emote-sets from Twitch.
	 * @param event Event emmiter
	 */
	void onEmotesets(Map<String, Map.Entry<Integer, Integer>> event);

	/**
	 * Channel enabled or disabled followers-only mode.
	 * @param event Event emmiter
	 */
	void onFollowersonly(ChannelEvent event);

	/**
	 * Channel is now hosted by another broadcaster.
	 * @param event Event emmiter
	 */
	void onHosted(HostEvent event);

	/**
	 * Channel is now hosting another channel.
	 * @param event Event emmiter
	 */
	void onHosting(HostEvent event);

	/**
	 * Username has joined a channel.
	 * @param event Event emmiter
	 */
	void onJoin(ServerStatusEvent event);

	/**
	 * Connection established, sending informations to server.
	 * @param event Event emmiter
	 */
	void onLogon(ConnectionEvent event);

	/**
	 * Received a message.
	 * @param event Event emmiter
	 */
	void onMessage(ChatEvent event);

	/**
	 * Someone got modded on a channel.
	 * @param event Event emmiter
	 */
	void onMod(ChannelEvent event);

	/**
	 * Received the list of moderators of a channel.
	 * @param userMods Event emmiter
	 */
	void onMods(ChannelMods userMods);

	/**
	 * Received a notice from server.
	 * @param event Event emmiter
	 */
	void onNotice(ChannelEvent event);

	/**
	 * User has left a channel.
	 * @param event Event emmiter
	 */
	void onPart(ServerStatusEvent event);

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
	 * @param event Event emmiter
	 */
	void onR9kMode(ChannelState event);

	/**
	 * Trying to reconnect to server.
	 */
	void onReconnect(ConnectionEvent event);

	/**
	 * Username has resubbed on a channel.
	 * @param event Event emmiter
	 */
	void onResub(SubscribeEvent event);

	/**
	 * The current state of the channel.
	 * @param event Event emmiter
	 */
	void onRoomstate(ChannelEvent event);

	/**
	 * Channel is no longer located on this cluster.
	 * @param event Event emmiter
	 */
	void onServerchange(ServerStatusEvent event);

	/**
	 * Gives you the current state of the channel.
	 * @param event Event emmiter
	 */
	void onSlowmode(ChannelEvent event);

	/**
	 * Channel enabled or disabled subscribers-only mode.
	 * @param event Event emmiter
	 */
	void onSubscribers(ChannelState event);

	/**
	 * Username has subscribed to a channel.
	 * @param event Event emmiter
	 */
	void onSubscription(SubscribeEvent event);

	/**
	 * Username has been timed out on a channel.
	 * @param event Event emmiter
	 */
	void onTimeout(ChannelEvent event);

	/**
	 * Channel ended the current hosting.
	 */
	void onUnhost(HostEvent event);

	/**
	 * Someone got unmodded on a channel.
	 * @param event Event emmiter
	 */
	void onUnmod(ChannelEvent event);

	/**
	 * Received a whisper.
	 * @param event Event emmiter
 	 */
	void onWhisper(ChatEvent event);

	void onNames(List<String> userlist);
}


