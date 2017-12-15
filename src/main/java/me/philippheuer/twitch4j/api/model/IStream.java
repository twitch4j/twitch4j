package me.philippheuer.twitch4j.api.model;

public interface IStream extends IModel {
    long getLongId();
    String getStringId();
    IGame getGame();
    long getViewers();
    int getVideoHeight();
    int getAverageFps();
    long getDelay();
    boolean isVodcast();
    String getPreviewUrl();
    IChannel getChannel();
}
