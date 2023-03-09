package edu.byu.cs.tweeter.client.model.service.observer;

public interface IsFollowObserver extends ServiceObserver{
    void formatFollowButton(boolean isFollowing);
}
