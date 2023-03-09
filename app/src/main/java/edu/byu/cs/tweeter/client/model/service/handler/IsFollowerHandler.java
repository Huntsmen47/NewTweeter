package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.observer.IsFollowObserver;

public class IsFollowerHandler extends BackgroundTaskHandler<IsFollowObserver> {


    public IsFollowerHandler(IsFollowObserver observer) {
        super(observer);
    }


    @Override
    protected void handleSuccess(Bundle data, IsFollowObserver observer) {
        boolean isFollower = data.getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
        observer.formatFollowButton(isFollower);
    }

    @Override
    protected String getErrorMessage() {
        return "Failed to format follow button";
    }
}
