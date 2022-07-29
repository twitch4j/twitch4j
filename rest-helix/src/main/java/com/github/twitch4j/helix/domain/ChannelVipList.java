package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class ChannelVipList {

    /**
     * The list of VIPs.
     * <p>
     * The list is empty if the channel doesn't have VIP users.
     * The list does not include the broadcaster.
     */
    private List<ChannelVip> data;

    /**
     * Contains the information used to page through the list of results.
     */
    private HelixPagination pagination;

}
