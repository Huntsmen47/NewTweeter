package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

/**
 * Background task that logs in a user (i.e., starts a session).
 */
public class LoginTask extends AuthenticationTask {


    public LoginTask(String username, String password, Handler messageHandler) {
        super(messageHandler,username,password);
    }



}
