package edu.byu.cs.tweeter.client.model.service.observer;

public interface FollowButtonObserver extends ServiceObserver {
    void updateFollowButton(boolean value);
}
