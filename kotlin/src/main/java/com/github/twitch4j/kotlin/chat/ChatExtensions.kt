package com.github.twitch4j.kotlin.chat

import com.github.philippheuer.events4j.kotlin.flowOn
import com.github.twitch4j.chat.ITwitchChat
import com.github.twitch4j.chat.events.AbstractChannelEvent
import com.github.twitch4j.chat.events.AbstractChannelMessageEvent
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Creates an events flow for the given channel.
 *
 * This function creates a flow for the given [T] events in the given [channel]. It is wrapped in a joinChannel and
 * leaveChannel for convenience, which can be turned off.
 *
 * Note: The autoJoinAndLeave feature will automatically leave the twitch channel when you remove the collector from
 * this flow. If this is a problem for you (e.g. you need to stay joined in the channel after removing a collector) you
 * are better off setting autoJoinAndLeave to false, and join/leave the channel on your own.
 *
 * Example usage:
 * ```kotlin
 * twitchChat.channelEventsAsFlow<ChannelJoinEvent>("twitch4j")
 *     .collect {
 *         println("[${it.channel.name}] ${it.user.name} just joined the channel!")
 *     }
 * ```
 *
 * @param T                 The class of the events you want to receive
 * @param channel           The channel to retrieve events from
 * @param autoJoinAndLeave  Whether we automatically join and leave the channel or not
 * @return                  A flow object that encapsulates handling joining, listening to, and leaving a channel
 */
inline fun <reified T : AbstractChannelEvent> ITwitchChat.channelEventsAsFlow(
    channel: String,
    autoJoinAndLeave: Boolean = true
): Flow<T> = channelEventsAsFlow(T::class.java, channel, autoJoinAndLeave)

/**
 * Creates a flow for both normal as action messages for the given channel.
 *
 * This function creates a flow for channel messages and channel actions (/me text). It is wrapped in a joinChannel and
 * leaveChannel for convenience, which can be turned off.
 *
 * Note: The autoJoinAndLeave feature will automatically leave the twitch channel when you remove the collector from
 * this flow. If this is a problem for you (e.g. you need to stay joined in the channel after removing a collector) you
 * are better off setting autoJoinAndLeave to false, and join/leave the channel on your own.
 *
 * Example usage:
 * ```kotlin
 * twitchChat.channelChatAsFlow("twitch4j")
 *     .collect {
 *         println("[${it.channel.name}] ${it.user.name} said: ${it.message}")
 *     }
 * ```
 *
 * @param channel           The channel to read chat messages from
 * @param autoJoinAndLeave  Whether we automatically join and leave the channel or not
 * @return                  A flow object that encapsulates joining, listening to messages, and leaving a channel
 */
fun ITwitchChat.channelChatAsFlow(
    channel: String,
    autoJoinAndLeave: Boolean = true
): Flow<AbstractChannelMessageEvent> =
    channelEventsAsFlow(AbstractChannelMessageEvent::class.java, channel, autoJoinAndLeave)


/**
 * Creates an events flow for the given channel.
 *
 * This function creates a flow for the given [T] events in the given [channel]. It is wrapped in a joinChannel and
 * leaveChannel for convenience, which can be turned off.
 *
 * Note: The autoJoinAndLeave feature will automatically leave the twitch channel when you remove the collector from
 * this flow. If this is a problem for you (e.g. you need to stay joined in the channel after removing a collector) you
 * are better off setting autoJoinAndLeave to false, and join/leave the channel on your own.
 *
 * Example usage:
 * ```kotlin
 * twitchChat.channelEventsAsFlow(JoinEvent::class.java, "twitch4j", true)
 *     .collect {
 *         println("[${it.channel.name}] ${it.user.name} joined the channel")
 *     }
 * ```
 *
 * @param T                 The class of the events you want to receive
 * @param klass             The event class to receive
 * @param channel           The channel to retrieve events from
 * @param autoJoinAndLeave  Whether we automatically join and leave the channel or not
 * @return                  A flow object that encapsulates handling joining, listening to, and leaving a channel
 */
fun <T : AbstractChannelEvent> ITwitchChat.channelEventsAsFlow(
    klass: Class<T>,
    channel: String,
    autoJoinAndLeave: Boolean
): Flow<T> = channelFlow {
    if (autoJoinAndLeave) {
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
