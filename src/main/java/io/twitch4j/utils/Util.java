/*
 * MIT License
 *
 * Copyright (c) 2018 twitch4j
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.twitch4j.utils;

import io.twitch4j.auth.ICredential;
import io.twitch4j.auth.Scope;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Util {
	private static Logger logger = LoggerFactory.getLogger("Util");

	public static void openBrowser(String url) {
		try {
			if (Desktop.isDesktopSupported()) {
				Desktop.getDesktop().browse(new URI(url));
			} else {
				Runtime runtime = Runtime.getRuntime();
				String os = System.getProperty("os.name").toLowerCase();
				if (os.contains("win")) {
					runtime.exec(String.format("rundll32 url.dll,FileProtocolHandler %s", url));
				} else if (os.contains("mac")) {
					runtime.exec(String.format("open %s", url));
				} else if (os.contains("nix") || os.contains("nux")) {
					String[] browsers = { "epiphany", "firefox", "mozilla", "konqueror", "netscape", "opera", "links", "lynx" };

					StringBuffer cmd = new StringBuffer();
					for (int i = 0; i < browsers.length; i++) {
						if (i == 0)	cmd.append(String.format("%s \"%s\"", browsers[i], url));
						else cmd.append(String.format(" || %s \"%s\"", browsers[i], url));
					}
					runtime.exec(new String[] { "sh", "-c", cmd.toString() });
				}
			}
		} catch (IOException | URISyntaxException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	public static String generateNonce(String topic) {
		StringBuilder sb = new StringBuilder();
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(topic.getBytes());

			for (byte b : md5.digest()) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1) sb.append(0);
				sb.append(hex);
			}
		} catch (Exception ex) {
			logger.error(ExceptionUtils.getMessage(ex), ex);
		}

		return sb.toString().toLowerCase();
	}

	public static <T> boolean checkEmptyAllFields(T object, String... fields) throws Exception {
		Set<FieldCondition> fieldConditions = Arrays.stream(fields)
				.map(field -> {
					boolean condition = !field.startsWith("!");
					String name = condition ? field.substring(1) : field;
					return new FieldCondition(name, condition);
				}).collect(Collectors.toSet());

		return checkEmptyAllFields(object, fieldsList ->
				fieldsList.stream().allMatch(field -> {
					Optional<FieldCondition> condition = fieldConditions.stream()
							.filter(fc -> fc.fieldName.equals(field.getName())).findFirst();
					if (condition.isPresent()) {
						try {
							return condition.get().condition && !Objects.isNull(field.get(object));
						} catch (IllegalAccessException ignored) {
						}
					}
					return true;
				})
		);
	}

	@SuppressWarnings("unchecked")
	public static <T> boolean checkEmptyAllFields(T object, Predicate<Collection<Field>> condition) throws Exception {
		return condition.test(Arrays.asList(object.getClass().getDeclaredFields()));
	}

	public static <T> boolean hasCollectionDifference(Collection<T> target, Collection<T> source) {
		return !source.stream().allMatch(target::contains) && !target.stream().allMatch(source::contains);
	}

	@AllArgsConstructor
	private static class FieldCondition {
		protected final String fieldName;
		protected final boolean condition;
	}
}
