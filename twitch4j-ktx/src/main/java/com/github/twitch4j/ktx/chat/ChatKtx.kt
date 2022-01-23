package com.github.twitch4j.ktx.chat

import com.github.twitch4j.chat.TwitchChat
import com.github.twitch4j.chat.events.AbstractChannelEvent
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent
import com.github.twitch4j.ktx.main.flowOn
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Creates an events flow for the given channel.
 *
 * Disclaimer: The autoJoinAndLeave feature will automatically leave the twitch channel when you remove the collector of
 * this flow. If this is a problem for you (e.g. you need to stay joined in the channel after removing a collector)
 * you are better off setting autoJoinAndLeave to false, and join/leave the channel on your own.
 *
 * @param channel           The channel to retrieve events from
 * @param autoJoinAndLeave  Whether we automatically join and leave the channel or not
 * @return                  A flow object that encapsulates handling joining, listening to, and leaving a channel
 */
inline fun <reified T : AbstractChannelEvent> TwitchChat.channelEventsAsFlow(
	channel: String,
	autoJoinAndLeave: Boolean = true
): Flow<T> = channelEventsAsFlow(T::class.java, channel, autoJoinAndLeave)

/**
 * Creates a chat message flow for the given channel.
 *
 * Disclaimer: The autoJoinAndLeave feature will automatically leave the twitch channel when you remove the collector of
 * this flow. If this is a problem for you (e.g. you need to stay joined in the channel after removing a collector)
 * you are better off setting autoJoinAndLeave to false, and join/leave the channel on your own.
 *
 * this feature on false, and join/leave the twitch channel on your own.
 *
 * @param channel           The channel to read chat messages from
 * @param autoJoinAndLeave  Whether we automatically join and leave the channel or not
 * @return                  A flow object that encapsulates joining, listening to messages, and leaving a channel
 */
fun TwitchChat.channelChatAsFlow(
	channel: String,
	autoJoinAndLeave: Boolean = true
): Flow<ChannelMessageEvent> = channelEventsAsFlow(ChannelMessageEvent::class.java, channel, autoJoinAndLeave)


/**
 * Creates an events flow for the given channel.
 *
 * Disclaimer: The autoJoinAndLeave feature will automatically leave the twitch channel when you remove the collector of
 * this flow. If this is a problem for you (e.g. you need to stay joined in the channel after removing a collector)
 * you are better off setting autoJoinAndLeave to false, and join/leave the channel on your own.
 *
 * @param klass             The event class to receive
 * @param channel           The channel to retrieve events from
 * @param autoJoinAndLeave  Whether we automatically join and leave the channel or not
 * @return                  A flow object that encapsulates handling joining, listening to, and leaving a channel
 */
fun <T : AbstractChannelEvent> TwitchChat.channelEventsAsFlow(
	klass: Class<T>,
	channel: String,
	autoJoinAndLeave: Boolean
): Flow<T> = channelFlow {
	if (autoJoinAndLeave) {
		connect()
		joinChannel(channel)
	}

	launch {
		eventManager.flowOn(klass)
			.filter { it.channel.name.equals(channel, true) }
			.collect(::send)
	}

	awaitClose {
		if (autoJoinAndLeave) {
			leaveChannel(channel)
		}
	}
}
