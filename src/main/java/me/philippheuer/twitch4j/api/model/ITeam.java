package me.philippheuer.twitch4j.api.model;

public interface ITeam extends IModel {
    long getLongId();
    String getStringId();

    String getBackground();
    String getBanner();
    String getDisplayName();
    String getInfo();
    String getLogo();
    String getName();
}
