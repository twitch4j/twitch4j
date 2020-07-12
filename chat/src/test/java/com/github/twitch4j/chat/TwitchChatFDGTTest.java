package com.github.twitch4j.chat;

import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.chat.enums.TMIConnectionState;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.chat.events.channel.CheerEvent;
import com.github.twitch4j.chat.events.channel.IRCMessageEvent;
import com.github.twitch4j.chat.util.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.util.*;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration Tests with FDGT.dev
 */
@Slf4j
@Tag("integration")
public class TwitchChatFDGTTest {

    private static TwitchChat twitchChat;

    private static FakeValuesService fakeValuesService = new FakeValuesService(new Locale("en-GB"), new RandomService());

    private static final Integer TESTCASE_COUNT_BITS = 5;

    @BeforeAll
    public static void connectToChat() {
        // event manager
        EventManager eventManager = new EventManager();
        eventManager.autoDiscovery();

        // construct twitchChat
        twitchChat = TwitchChatBuilder.builder()
            .withEventManager(eventManager)
            .withChatAccount(TestUtils.getCredential())
            .withBaseUrl(TwitchChat.TWITCH_FDGT_SOCKET_SERVER)
            .withCommandTrigger("!")
            .build();
        twitchChat.joinChannel("twitch4j");

        // wait until we're connected
        await().atMost(10, SECONDS).until(() -> twitchChat.getConnectionState() == TMIConnectionState.CONNECTED);
    }

    @Test
    @DisplayName("Receives Bits / Cheers")
    public void sendReceiveBits() {
        List<CheerEvent> cheers = new ArrayList<>();
        twitchChat.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(CheerEvent.class, event -> {
            cheers.add(event);
        });
        twitchChat.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(IRCMessageEvent.class, event -> {
            log.debug(event.toString());
        });

        // testcases
        List<Map<String, String>> testCases = new ArrayList<>();
        for (int i = 0; i < TESTCASE_COUNT_BITS; i++) {
            Map<String, String> testCase = new HashMap<>();

            testCase.put("channel", fakeValuesService.regexify("[a-z0-9]{3,24}"));
            testCase.put("bits", fakeValuesService.regexify("[1-9]{1,5}"));
            testCase.put("message", String.format("Cheer%s %s", testCase.get("bits"), fakeValuesService.regexify("[a-zA-Z0-9]{6,35}")));

            testCases.add(testCase);
        }

        testCases.forEach(testCase -> twitchChat.sendMessage("twitch4j", String.format("bits --channel %s --bitscount %s %s", testCase.get("channel"), testCase.get("bits"), testCase.get("message"))));

        await().atMost(10, SECONDS).until(() -> cheers.size() == testCases.size());

        for (int i = 0; i < TESTCASE_COUNT_BITS; i++) {
            Map<String, String> testCase = testCases.get(i);
            CheerEvent cheerEvent = cheers.get(i);

            assertEquals(cheerEvent.getChannel().getName(), testCase.get("channel"));
            assertEquals(cheerEvent.getBits(), Integer.parseInt(testCase.get("bits")));
            assertEquals(cheerEvent.getMessage().toLowerCase(), testCase.get("message").toLowerCase());
        }
    }
}
