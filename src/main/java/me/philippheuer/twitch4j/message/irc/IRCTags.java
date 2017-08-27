package me.philippheuer.twitch4j.message.irc;

import lombok.Getter;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Tag list
 * @author Damian Staszewski
 * @param <T> extended {@link Object}
 */

@SuppressWarnings("unchecked")
public class IRCTags <T extends Object> {

	/**
	 * Tag List
	 */
	private final Map<String, T> tags = new HashMap<String, T>();

	/**
	 * Constructor class
	 * @param rawTags raw tags
	 */
	public IRCTags(String rawTags) {
		for (String tagData: rawTags.split(";")) {
			String key = tagData.split("=")[0];

			if(tagData.split("=").length == 1)
				tags.put(key, null); // No Value - should be nullable
			else
				tags.put(key, parseTagValue(tagData.split("=")[0], tagData.split("=")[1]));
		}
	}

	/**
	 * Parsing tag value
	 * @param key key
	 * @param value value
	 * @return value converted to Object class
	 */
	private T parseTagValue(String key, String value) {
		if (value == null) return null;
		try {
			if (key.equalsIgnoreCase("badges")) {
				Map<String, Integer> badges = new HashMap<String, Integer>();

				if((value.contains(",") || value.contains("/"))) {
					for (String badge : value.split(",")) {
						badges.put(badge.split("/")[0], Integer.parseInt(badge.split("/")[1]));
					}
				}

				return (T) badges;
			} else if ((value.contains("/") || value.contains(":")) && key.equalsIgnoreCase("emotes")) {
				Map<Integer, Map.Entry<Integer, Integer>> emotes = new HashMap<Integer, Map.Entry<Integer, Integer>>();
				for (String emote : value.split("/")) {
					emotes.put(Integer.parseInt(emote.split(":")[0]), new AbstractMap.SimpleEntry<>(Integer.parseInt(emote.split(":")[1].split("-")[0]), Integer.parseInt(emote.split(":")[1].split("-")[1])));
				}
				return (T) emotes;
			} else if (key.equalsIgnoreCase("subscriber") || key.equalsIgnoreCase("mod") || key.equalsIgnoreCase("turbo")) {
				return (T) Boolean.valueOf(value.equals("1"));
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return (T) value;
	}

	/**
	 * Tag size
	 * @return Tag size
	 */
	public int size() {
		return tags.size();
	}

	/**
	 * Getting tag exists
	 *
	 * @param name tag name
	 * @return tag existence
	 */
	public boolean hasTag(String name) {
		return tags.containsKey(name);
	}

	/**
	 * Get one tag
	 *
	 * @param name tag name
	 * @param <T> some classes extends Object
	 * @return getting tag data returned classes extends Object
	 */
	public <T extends Object> T getTag(String name) {
		return (T) tags.get(name);
	}

	/**
	 * annotated to {@link Map#forEach(BiConsumer)}
	 * @param action method from {@link BiConsumer} interface;
	 */
	public void forEach(BiConsumer action) {
		tags.forEach(action);
	}

	@Override
	public String toString() {
		return tags.toString();
	}

	public T getOrDefaultTag(String key, T value) {
		return tags.getOrDefault(key, value);
	}
}
