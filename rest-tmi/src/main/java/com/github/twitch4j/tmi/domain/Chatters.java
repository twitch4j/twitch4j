package com.github.twitch4j.tmi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Chatters {

    /** Viewer Count */
    @NonNull
    @JsonProperty("chatter_count")
    private Integer viewerCount;

    /** VIPS */
    @JsonIgnore
    private List<String> vips;

    /** Staff */
    @JsonIgnore
    private List<String> staff;

    /** Admins */
    @JsonIgnore
    private List<String> admins;

    /** Moderators */
    @JsonIgnore
    private List<String> moderators;

    /** Viewers */
    @JsonIgnore
    private List<String> viewers;

    @JsonProperty("chatters")
    private void unpackMessage(Map<String, List<String>> chatters) {
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
        List<String> newList = new ArrayList<String>();
        newList.addAll(vips);
        newList.addAll(moderators);
        newList.addAll(staff);
        newList.addAll(admins);
        newList.addAll(viewers);
        return newList;
    }

}
