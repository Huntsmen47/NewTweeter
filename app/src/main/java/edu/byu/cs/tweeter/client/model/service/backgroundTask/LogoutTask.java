package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;

/**
 * Background task that logs out a user (i.e., ends a session).
 */
public class LogoutTask extends AuthenticatedTask {


    public LogoutTask(AuthToken authToken, Handler messageHandler) {
        super(messageHandler,authToken);
    }

    @Override
    protected void processTask() {

    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {

    }
}
