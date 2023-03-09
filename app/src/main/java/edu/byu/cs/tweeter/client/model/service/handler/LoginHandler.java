package edu.byu.cs.tweeter.client.model.service.handler;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.observer.AuthenticateObserver;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Message handler (i.e., observer) for LoginTask
 */
public class LoginHandler extends AuthenticateHandler<LoginTask> {

    public LoginHandler(AuthenticateObserver observer) {
        super(observer);
    }

    @Override
    protected String getErrorMessage() {
        return "Failed to login";
    }

    @Override
    public void authenticate(AuthenticateObserver observer, User user) {
        observer.authenticate(user);
    }
}
