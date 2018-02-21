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

package io.twitch4j.api.kraken.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.twitch4j.api.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.twitch4j.api.Model;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaginatedList<E> extends Model {
	@JsonProperty("_cursor")
	private String cursor;
	private int offset;
	private InteractivePage<E> next;
	private InteractivePage<E> previous;

	@JsonAlias({
			"follows",
			"blocks",
			"subscriptions",
			"videos",
			"vods",
			"communities",
			"banned_users",
			"timed_out_users",
			"clips",
			"collections",
			"top",
			"teams",
			"channels",
			"games",
			"streams",
			"featured",
			"posts",
			"comments"
	})
	private List<E> data;

	public PaginatedList<E> next() {
		if (StringUtils.isBlank(cursor)) {
			return next.invoke(++offset);
		} else return next.invoke(cursor);
	}

	public PaginatedList<E> previous() {
		if (StringUtils.isBlank(cursor)) {
			if (offset == 0)
				return this;
			else return previous.invoke(--offset);
		} else return previous.invoke(cursor);
	}

	public interface InteractivePage<E> {
		PaginatedList<E> invoke(String cursor);
		PaginatedList<E> invoke(int offset);
	}
}
