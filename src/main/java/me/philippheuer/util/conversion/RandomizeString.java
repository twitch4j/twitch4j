package me.philippheuer.util.conversion;

import java.util.Random;
import java.util.regex.Pattern;

/**
 * Abstract base for result lists.
 *
 * @author Damian Staszewski [https://github.com/stachu540]
 * @version %I%, %G%
 * @since 1.0
 */
public class RandomizeString {
	private final String saltStr;

	public RandomizeString(int length) {
		final String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() <= length) {
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			boolean toLower = new Random().nextBoolean();
			char c = SALTCHARS.charAt(index);
			salt.append((Pattern.matches("[0-9]", String.valueOf(c)) ? c : (toLower) ? String.valueOf(c).toLowerCase() : c ));
		}
		saltStr = salt.toString();
	}

	@Override
	public String toString() {
		return saltStr;
	}
}
