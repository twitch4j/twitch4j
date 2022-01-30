package com.github.twitch4j.kotlin.mock

import com.github.philippheuer.events4j.core.EventManager
import com.github.twitch4j.chat.TwitchChat
import com.github.twitch4j.common.util.ThreadUtils

/**
 * The bare minimum we need to test the TwitchChat extensions
 */
class MockChat : TwitchChat(
		EventManager().apply { autoDiscovery() },
		null,
		null,
		null,
		false,
		emptyList(),
		1,
		null,
		null,
		null,
		null,
		ThreadUtils.getDefaultScheduledThreadPoolExecutor("MOCK", 1),
		1,
		null,
		false,
		false,
		null,
		false,
		100,
		1
) {
	var isConnected = false

	override fun connect() {
		isConnected = true
	}

	override fun disconnect() {
		isConnected = false
	}

	override fun joinChannel(channelName: String) {
		val lowerChannelName = channelName.lowercase()
		currentChannels.add(lowerChannelName)
	}

	override fun leaveChannel(channelName: String): Boolean {
		val lowerChannelName = channelName.lowercase()
		currentChannels.remove(lowerChannelName)
		return true
	}
}
