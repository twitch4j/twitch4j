package com.github.twitch4j.tmi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class Chatters {

    /** Viewer Count */
    @NonNull
    @JsonProperty("chatter_count")
    private Integer viewerCount;

    /** VIPS */
    @JsonIgnore
    private List<String> vips;

    /** Broadcaster */
    @JsonIgnore
    private List<String> broadcaster;

    /** Staff */
    @JsonIgnore
    @Deprecated
    private List<String> staff;

    /** Admins */
    @JsonIgnore
    @Deprecated
    private List<String> admins;

    /** Moderators */
    @JsonIgnore
    private List<String> moderators;

    /** Viewers */
    @JsonIgnore
    private List<String> viewers;

    @JsonProperty("chatters")
    private void unpackMessage(Map<String, List<String>> chatters) {
        broadcaster = chatters.get("broadcaster");
        vips = chatters.get("vips");
        moderators = chatters.get("moderators");
        staff = chatters.get("staff");
        admins = chatters.get("admins");
        viewers = chatters.get("viewers");
    }

    /**
     * Gets all viewers, including vips/moderators/staff/admins
     *
     * @return all viewers (name)
     */
    public List<String> getAllViewers() {
        List<String> newList = new ArrayList<>(viewerCount);
        newList.addAll(broadcaster);
        newList.addAll(vips);
        newList.addAll(moderators);
        newList.addAll(staff);
        newList.addAll(admins);
        newList.addAll(viewers);
        return newList;
    }

}
