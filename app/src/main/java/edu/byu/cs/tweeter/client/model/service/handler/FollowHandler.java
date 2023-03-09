package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.observer.FollowButtonObserver;

public class FollowHandler extends BackgroundTaskHandler<FollowButtonObserver> {

    public FollowHandler(FollowButtonObserver mainObserver) {
        super(mainObserver);
    }

    @Override
    protected void handleSuccess(Bundle data, FollowButtonObserver observer) {
        observer.updateFollowButton(false);
    }

    @Override
    protected String getErrorMessage() {
        return "Failed to follow";
    }
}
