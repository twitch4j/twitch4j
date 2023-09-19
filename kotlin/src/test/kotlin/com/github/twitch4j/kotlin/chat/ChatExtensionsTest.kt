package com.github.twitch4j.kotlin.chat

import com.github.twitch4j.chat.events.AbstractChannelEvent
import com.github.twitch4j.chat.events.channel.ChannelJoinEvent
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent
import com.github.twitch4j.chat.util.MessageParser
import com.github.twitch4j.common.events.domain.EventChannel
import com.github.twitch4j.common.events.domain.EventUser
import com.github.twitch4j.kotlin.mock.MockChat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import lombok.extern.slf4j.Slf4j
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

@ExperimentalCoroutinesApi
@Slf4j
@Tag("unittest")
class ChatExtensionsTest {

    @Test
    fun `Assert that channels are not the same`() {
        assertNotEquals(testChannel1.id.lowercase(), testChannel2.id.lowercase())
        assertNotEquals(testChannel1.name.lowercase(), testChannel2.name.lowercase())
    }

    @Test
    fun `Check if we only receive events from the current channel`() = runTest {
        val mockTwitchChat = MockChat()

        var eventsReceived = 0

        val collectorJob = launch {
            mockTwitchChat.channelEventsAsFlow<AbstractChannelEvent>(testChannelName1, false)
                .collect {
                    assertEquals(it.channel.name, testChannelName1, "Incorrect channel event received")
                    eventsReceived++
                }
        }
        runCurrent()

        launch {
            mockTwitchChat.eventManager.apply {
                publish(testChannelJoinEvent1)
                publish(testChannelJoinEvent2)
                publish(testChannelMessageEvent1)
                publish(testChannelMessageEvent1)
                publish(testChannelMessageEvent2)
                publish(testChannelMessageEvent1)
                publish(testChannelMessageEvent2)
            }
        }
        runCurrent()

        collectorJob.cancel()

        assertEquals(4, eventsReceived, "Expected the amount of events received to be exactly 4")
    }

    @Test
    fun `Check if we only receive message events`() = runTest {
        val mockTwitchChat = MockChat()

        var eventsReceived = 0

        val collectorJob = launch {
            mockTwitchChat.channelChatAsFlow(testChannelName1)
                .collect {
                    eventsReceived++
                }
        }
        runCurrent()

        launch {
            mockTwitchChat.eventManager.apply {
                publish(testChannelJoinEvent1)
                publish(testChannelJoinEvent2)
                publish(testChannelMessageEvent1)
                publish(testChannelMessageEvent1)
                publish(testChannelMessageEvent2)
                publish(testChannelMessageEvent1)
                publish(testChannelMessageEvent2)
            }
        }
        runCurrent()

        collectorJob.cancel()

        assertEquals(3, eventsReceived, "Expected the amount of events received to be exactly 3")
    }

    @Test
    fun `Check if we only receive the events we ask for`() = runTest {
        val mockTwitchChat = MockChat()

        var eventsReceived = 0

        val collectorJob = launch {
            mockTwitchChat.channelEventsAsFlow<ChannelJoinEvent>(testChannelName1)
                .collect {
                    eventsReceived++
                }
        }
        runCurrent()

        launch {
            mockTwitchChat.eventManager.apply {
                publish(testChannelJoinEvent1)
                publish(testChannelJoinEvent2)
                publish(testChannelMessageEvent1)
                publish(testChannelMessageEvent1)
                publish(testChannelMessageEvent2)
                publish(testChannelMessageEvent1)
                publish(testChannelMessageEvent2)
            }
        }
        runCurrent()

        collectorJob.cancel()

        assertEquals(1, eventsReceived, "Expected the amount of events received to be exactly 3")
    }

    @Test
    fun `Check if we do not join the channel automatically when we pass false to channelFlow`() = runTest {
        val mockTwitchChat = MockChat().apply { disconnect() }

        val collectorJob = launch {
            mockTwitchChat.channelEventsAsFlow<AbstractChannelEvent>(testChannelName1, false)
                .collect { /* NO-OP */ }
        }
        runCurrent()

        assertEquals(false, mockTwitchChat.isConnected, "Automatically connected, it should not do that!")
        assertEquals(0, mockTwitchChat.channels.size, "Did join the channel, although we told it not to")

        collectorJob.cancel()
    }

    @Test
    fun `Check if we automatically join and leave`() = runTest {
        val mockTwitchChat = MockChat()

        val collectorJob = launch {
            mockTwitchChat.channelEventsAsFlow<AbstractChannelEvent>(testChannelName1, true)
                .collect { /* NO-OP */ }
        }
        runCurrent()

        assertEquals(1, mockTwitchChat.channels.size, "Did not join any channel")
        assertEquals(
            testChannelName1.lowercase(),
            mockTwitchChat.channels.firstOrNull(),
            "Did not connect to the correct channel"
        )

        collectorJob.cancel()
        runCurrent()

        assertEquals(0, mockTwitchChat.channels.size, "Did not leave the channel after cancelling the flow")
    }

    private val testChannelId1 = "channelTestId1"
    private val testChannelId2 = "channelTestId2"
    private val testChannelName1 = "channelTestName1"
    private val testChannelName2 = "channelTestName2"

    private val testChannel1 = EventChannel(testChannelId1, testChannelName1)
    private val testChannel2 = EventChannel(testChannelId2, testChannelName2)
    private val testUser = EventUser("testUserId", "testUserName")

    private val testMessage = ":awakenedredstone PRIVMSG #twitch4j :TestMessage"
    private val testIrcMessageEvent = MessageParser.parse(testMessage)
    private val testChannelJoinEvent1 = ChannelJoinEvent(testChannel1, testUser)
    private val testChannelJoinEvent2 = ChannelJoinEvent(testChannel2, testUser)
    private val testChannelMessageEvent1 =
        ChannelMessageEvent(testChannel1, testIrcMessageEvent, testUser, "TestMessage")
    private val testChannelMessageEvent2 =
        ChannelMessageEvent(testChannel2, testIrcMessageEvent, testUser, "TestMessage")
}
