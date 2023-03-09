package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.observer.FollowButtonObserver;

public class UnfollowHandler extends BackgroundTaskHandler<FollowButtonObserver> {

    public UnfollowHandler(FollowButtonObserver mainObserver) {
        super(mainObserver);
    }

    @Override
    protected void handleSuccess(Bundle data, FollowButtonObserver observer) {
        observer.updateFollowButton(true);
    }

    @Override
    protected String getErrorMessage() {
        return "Failed to unfollow";
    }


}
