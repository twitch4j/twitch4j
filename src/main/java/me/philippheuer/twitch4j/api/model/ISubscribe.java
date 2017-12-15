package me.philippheuer.twitch4j.api.model;

public interface ISubscribe extends IModel {
    String getId();
//    SubPlan getPlan();
    String getPlanName();
    IUser getUser();
}
