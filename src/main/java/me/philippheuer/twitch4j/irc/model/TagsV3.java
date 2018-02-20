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

package me.philippheuer.twitch4j.irc.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;


/**
 * Tags V3 is a {@link Collections#unmodifiableMap(Map)}, which contains parsed tags data.
 * @author Damian Staszewski [https://github.com/stachu540]
 * @version %I%, %G%
 * @since 1.0
 */
public class TagsV3 implements Map<String, Object> {

	private final Map<String, Object> m;

	/**
	 * Setting tags map are final
	 * @param map {@link Map} parsed data
	 */
	TagsV3(Map<String, Object> map) {
		this.m = Collections.unmodifiableMap(map);
	}

	@Override
	public int size() {
		return m.size();
	}

	@Override
	public boolean isEmpty() {
		return m.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return m.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return m.containsValue(value);
	}

	@Override
	public Object get(Object key) {
		return m.get(key);
	}

	@Override
	public Object put(String key, Object value) {
		return m.put(key, value);
	}

	@Override
	public Object remove(Object key) {
		return m.remove(key);
	}

	@Override
	public void putAll(Map<? extends String, ?> m) {
		this.m.putAll(m);
	}

	@Override
	public void clear() {
		m.clear();
	}

	@Override
	public Set<String> keySet() {
		return m.keySet();
	}

	@Override
	public Collection<Object> values() {
		return m.values();
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		return m.entrySet();
	}

	@Override
	public boolean equals(Object obj) {
		return m.equals(obj);
	}

	@Override
	public int hashCode() {
		return m.hashCode();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + m.toString() + ")";
	}
}
