package io.twitch4j.api.kraken.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.twitch4j.api.model.IIDModel;
import io.twitch4j.api.model.Model;

import java.util.Locale;

public abstract class Clip extends Model implements IIDModel<String> {
    public abstract long getTrackingId();

    public abstract String getUrl();

    @JsonProperty("embed_url")
    public abstract String getEmbedingUrl();

    @JsonProperty("curator.id")
    public abstract User getCurator();

    @JsonProperty("broadcaster.id")
    public abstract Channel getBroadcaster();

    @JsonProperty("vod.id")
    public abstract Video getVod();

    public abstract String getTitle();

    public abstract Game getGame();

    public abstract Locale getLanguage();

    public abstract int getViews();

    public abstract double getDuration();

    public abstract Thumbnails getThumbnails();


}
