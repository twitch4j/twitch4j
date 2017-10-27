package me.philippheuer.twitch4j.events.event.pubsub;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import me.philippheuer.twitch4j.model.ChatEmote;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.philippheuer.twitch4j.events.Event;
import me.philippheuer.twitch4j.model.Badges;
import me.philippheuer.twitch4j.model.User;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Whispers extends Event {
    @JsonProperty("body") private String message;
    @JsonProperty("tags") private Sender sender;
    @JsonProperty("recipient") private Reciever reciever;

    @Data
    @EqualsAndHashCode(callSuper = false)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public class Reciever {
        @JsonProperty("id") private User user;
        private String color;
        private String userType;
        private Badges[] badges;
        private ChatEmote[] emotes;

        public void setUser(long id) {
            this.user = getClient().getUserEndpoint().getUser(id).get();
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public class Sender {
        @JsonProperty("login") private User user;
        private String color;
        private String userType;
        private Badges[] badges;

        public void setUser(String login) {
            this.user = getClient().getUserEndpoint().getUserByUserName(login).get();
        }
    }
}
