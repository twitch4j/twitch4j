package me.philippheuer.twitch4j.api.kraken.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaginatedList<E> extends KrakenModel {
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
