package io.twitch4j.api.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PaginatedList<E, I> {
    @JsonProperty("_cursor")
    private I cursor;
    private PageInteraction<E, I> interactivePage;

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

    public PaginatedList<E, I> next() {
        return interactivePage.invoke(cursor);
    }

    public PaginatedList<E, I> previous() {
        return interactivePage.invoke(cursor, true);
    }
}
