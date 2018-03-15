package io.twitch4j.api.kraken.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.twitch4j.api.model.IIDModel;
import io.twitch4j.api.model.Model;
import io.twitch4j.impl.api.kraken.model.ChannelBuilder;
import org.immutables.value.Value;

import java.awt.*;
import java.time.Instant;
import java.util.Locale;

@JsonDeserialize(builder = ChannelBuilder.class)
public abstract class Channel extends Model implements IIDModel<Long> {
    @JsonProperty("name")
    public abstract String getUsername();

    public abstract String getDisplayName();

    public abstract String getUri();

    public abstract String getLogo();

    public abstract Locale getLanguage();

    public abstract String getVideoBanner();

    public abstract String getProfileBanner();

    public abstract Color getProfileBannerBackgroundColor();

    public abstract Game getGame();

    @JsonProperty("status")
    public abstract String getStreamTitle();

    public abstract boolean isMature();

    @JsonProperty("broadcaster_language")
    public abstract Locale getBroadcastingLanguage();

    public abstract boolean hasPartner();

    public abstract Instant createdAt();

    public abstract Instant updatedAt();

    @JsonProperty("followers")
    public abstract long getFollowersCount();

    @JsonProperty("views")
    public abstract long getViewersCount();

    @Value.Lazy
    public User getUser() {
        if (getCredential().isPresent()) {
            return getClient().getKrakenApi().userOperation().get(getCredential().get());
        } else {
            return getClient().getKrakenApi().userOperation().getById(getId());
        }
    }
}
