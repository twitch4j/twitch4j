package twitch4j.api.kraken.model;

import java.util.List;
import lombok.Data;

/**
 * Model representing a list of chat rooms.
 */
@Data
public class ChatRoomList {

	/**
	 * Data
	 */
	List<ChatRoom> rooms;
}
