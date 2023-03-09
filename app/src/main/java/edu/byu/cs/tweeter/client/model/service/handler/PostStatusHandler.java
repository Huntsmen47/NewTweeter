package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.observer.SimpleTaskObserver;

public class PostStatusHandler extends BackgroundTaskHandler<SimpleTaskObserver> {

    public PostStatusHandler(SimpleTaskObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, SimpleTaskObserver observer) {
        observer.postStatus();
    }

    @Override
    protected String getErrorMessage() {
        return "Failed to post the status";
    }
}
