package edu.byu.cs.tweeter.client.model.service.observer;

public interface SimpleTaskObserver extends ServiceObserver {
    void processLogout();
    void postStatus();
}
