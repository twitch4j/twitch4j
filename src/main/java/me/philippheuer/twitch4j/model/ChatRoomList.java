package me.philippheuer.twitch4j.model;

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
