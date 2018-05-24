package twitch4j.api.kraken.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

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
