package me.philippheuer.twitch4j.api.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.Locale;

public interface IChannel extends IModel {
    long getLongId();
    String getStringId();

    boolean isMature();
    String getStreamTitle();
    Locale getBroadcasterLanguage();
    IGame getGame();
    Locale getLanguage();
    String getLogo();
    String getVideoBanner();
    long getViews();
    String getUrl();

    Collection<IFollow> getFollowers();

    IUser getUser();
    IStream getStream();
    Collection<ITeam> getTeams();
    Collection<IUser> getEditors();
    Collection<ISubscribe> getSubscriptions();
    Collection<ICommunity> getCommunities();
    Collection<IVideo> getVideos();

    boolean isLive();
    boolean isVodcast();

    boolean isFollowing(IUser user);
    boolean isSubscribing(IUser user);

    boolean resetStreamKey();

    boolean startCommercial(@Min(30) @Max(180) int seconds);

    IChannel setCommunities(@Max(3) ICommunity... communities);
    IChannel deleteCommunity(@Max(3) ICommunity... communities);
    boolean clearCommunities();
}
