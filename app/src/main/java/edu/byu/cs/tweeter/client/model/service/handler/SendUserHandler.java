package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.observer.PageObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SendUserObserver;
import edu.byu.cs.tweeter.client.presenter.PagedPresenter;
import edu.byu.cs.tweeter.model.domain.User;

public class SendUserHandler extends BackgroundTaskHandler<SendUserObserver> {

    public SendUserHandler(SendUserObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, SendUserObserver observer) {
        User user = (User) data.getSerializable(GetUserTask.USER_KEY);
        observer.sendUser(user);
    }

    @Override
    protected String getErrorMessage() {
        return "Failed to get user";
    }


}
