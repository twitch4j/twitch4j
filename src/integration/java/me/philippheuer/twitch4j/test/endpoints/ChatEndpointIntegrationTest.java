package me.philippheuer.twitch4j.test.endpoints;

import me.philippheuer.twitch4j.endpoints.ChatEndpoint;
import me.philippheuer.twitch4j.model.ChatRoomList;
import me.philippheuer.twitch4j.test.TwitchClientIntegrationTest;
import me.philippheuer.util.test.IntegrationTestCategory;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Optional;


@Category(IntegrationTestCategory.class)
public class ChatEndpointIntegrationTest extends TwitchClientIntegrationTest {
	public ChatEndpointIntegrationTest() {
	}

	@Test
	public void testGetChatRoomsByChannel() {
		ChatEndpoint chatEndpoint = twitchClient.getChatEndpoint();

		Optional<ChatRoomList> chatRoomList = chatEndpoint.getChatRoomsByChannel(CHANNEL_ID);

		assertNotNull(chatRoomList);
		assertTrue(chatRoomList.isPresent());
		assertFalse(chatRoomList.get().getRooms().isEmpty());
	}
}
