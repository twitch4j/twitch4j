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

package io.twitch4j.irc.model;

import java.util.*;


/**
 * Tags V3 is a {@link Collections#unmodifiableMap(Map)}, which contains parsed tags data.
 * @author Damian Staszewski [https://github.com/stachu540]
 * @version %I%, %G%
 * @since 1.0
 */
public class TagsV3<T> extends AbstractMap<String, Optional<T>> implements Map<String, Optional<T>> {

	private final Map<String, Optional<T>> m;

	/**
	 * Setting tags map are final
	 * @param map {@link Map} parsed data
	 */
	TagsV3(Map<String, Optional<T>> map) {
		this.m = Collections.unmodifiableMap(map);
	}

	@Override
	public Set<Entry<String, Optional<T>>> entrySet() {
		return m.entrySet();
	}

	@Override
	public Optional<T> put(String key, Optional<T> value) {
		return m.put(key, value);
	}
}
