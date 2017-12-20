package me.philippheuer.twitch4j.api.model;

import java.util.Collection;

public interface IGame extends IModel {
    String getStringId();
    long getLongId();
    String getName();
    long getViewersCount();
    long getChannelsCount();
    String getBoxUrl();
    String getLogoUrl();
    long getPopularity();

    Collection<IStream> getStreams();
}
