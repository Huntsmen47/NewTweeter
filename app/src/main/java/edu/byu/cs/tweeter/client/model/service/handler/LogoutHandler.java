package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.observer.SimpleTaskObserver;

public class LogoutHandler extends BackgroundTaskHandler<SimpleTaskObserver> {



    public LogoutHandler(SimpleTaskObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, SimpleTaskObserver observer) {
        observer.processLogout();
    }

    @Override
    protected String getErrorMessage() {
        return "Failed to logout";
    }
}
